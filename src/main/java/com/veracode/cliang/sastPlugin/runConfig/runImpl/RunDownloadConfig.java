package com.veracode.cliang.sastPlugin.runConfig.runImpl;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.veracode.cliang.sastPlugin.apiWrapper.ApplicationOperationWrapper;
import com.veracode.cliang.sastPlugin.apiWrapper.ResultOperationWrapper;
import com.veracode.cliang.sastPlugin.apiWrapper.SandboxOperationWrapper;
import com.veracode.cliang.sastPlugin.objects.config.ScanConfiguration;
import com.veracode.cliang.sastPlugin.objects.raw.appList.AppType;
import com.veracode.cliang.sastPlugin.objects.raw.appList.Applist;
import com.veracode.cliang.sastPlugin.objects.raw.buildList.BuildType;
import com.veracode.cliang.sastPlugin.objects.raw.buildList.Buildlist;
import com.veracode.cliang.sastPlugin.objects.raw.detailedReport.Detailedreport;
import com.veracode.cliang.sastPlugin.objects.raw.sandboxList.SandboxType;
import com.veracode.cliang.sastPlugin.services.ReportHolderService;
import com.veracode.cliang.sastPlugin.services.ResultToolWindowHolderService;
import com.veracode.cliang.sastPlugin.services.UserRightsHolderService;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;
import com.veracode.cliang.sastPlugin.view.toolWindows.result.ResultToolWindowFactory;
import com.veracode.cliang.sastPlugin.view.toolWindows.runConfig.RunConfigToolWindow;

import java.util.Comparator;
import java.util.List;

public class RunDownloadConfig extends RunConfigBase {

    //long appId, sandboxId, scanId;

    public RunDownloadConfig(ScanConfiguration scanConfig, RunConfigToolWindow consoleTW) {
        super(scanConfig, consoleTW);
    }

    @Override
    public boolean runConfig() {
        logInfo("Running download configuration: " + scanConfig.getConfig_name());
        logInfo("");


        try {
            // Step 1: Locate the application
            locateAppByName();

            logInfo("\n\n");

            // Step 2: Get the latest scan
            getLatestScanId();

            logInfo("\n\n");

            // Step 3: Download result
            downloadResult();

            logInfo("\n\n");

            logInfo("Download config process completed.");



        } catch (RunConfigException e) {
            logError(e.getMessage() + " @ " + e.getStepName());
            return false;
        } catch (Exception e) {
            logError(e.getMessage());
        }



        return true;
    }


    private void locateAppByName() throws RunConfigException {
        logInfo("STEP 1: Locating application profile");
        logInfo("====================================");
        logInfo("Application profile name: " + scanConfig.getPortfolio().getApp_name());

        String appName = scanConfig.getPortfolio().getApp_name();

        Applist applications = ApplicationOperationWrapper.getApplicationListing();

        // Set user rights information into the holder service
        UserRightsHolderService userRightsHolderService = ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), UserRightsHolderService.class);
        userRightsHolderService.setUserRights(applications.getUser());

        for (AppType app: applications.getApp()) {
            if (app.getAppName().equals(appName.trim())) {
                logInfo("Found " + appName + " with ID = " + app.getAppId() + ".");
                appId = app.getAppId();
                return;
            }
        }

        //logError("No matched application profile found.");
        throw new RunConfigException("No matched application profile found.", "locate application profile.");
    }

    private void getLatestScanId() throws RunConfigException {
        logInfo("STEP 2: Obtain the latest completed scan ID");
        logInfo("===========================================");
        logInfo("Getting the list of scans...");
        logInfo(isSandboxScan()? "This is a sandbox scan." : "This is a policy scan.");

        Buildlist buildListingParentObj;

        if (isSandboxScan()) {
            locateSandboxByName();
            buildListingParentObj = ApplicationOperationWrapper.getScansListing(formatAppIdInString(), formatSandboxIdInString());
        } else {
            buildListingParentObj = ApplicationOperationWrapper.getScansListing(formatAppIdInString());
        }

        List<BuildType> scanList = buildListingParentObj.getBuild();



        if (isSandboxScan()) {
            scanList.sort(new Comparator<BuildType>() {
                @Override
                public int compare(BuildType o1, BuildType o2) {
                    return o1.getBuildId() < o2.getBuildId()? 1 : -1;
                }
            });

            if (scanList.size() != 0) {
                scanId = scanList.get(0).getBuildId();
                logInfo("The latest scan is \"" + scanList.get(0).getVersion() + "\", with build ID = " + scanId);
                return;
            }
        } else { // Policy scan
            scanList.sort(new Comparator<BuildType>() {
                @Override
                public int compare(BuildType o1, BuildType o2) {
                    if (o1.getPolicyUpdatedDate() == null || o1.getPolicyUpdatedDate().trim().equals("")) {
                        return 9999;
                    } else if (o2.getPolicyUpdatedDate() == null || o2.getPolicyUpdatedDate().trim().equals("")) {
                        return -9999;
                    } else {
                        return o2.getPolicyUpdatedDate().compareTo(o1.getPolicyUpdatedDate());
                    }
                }
            });

            if (scanList.size() != 0 && scanList.get(0).getPolicyUpdatedDate() != null &&
                    !scanList.get(0).getPolicyUpdatedDate().trim().equals("")) {
                scanId = scanList.get(0).getBuildId();
                logInfo("The latest scan is \"" + scanList.get(0).getVersion() + "\", with build ID = " + scanId);
                return;
            }
        }

        throw new RunConfigException("No completed scan is found.", "Obtain latest scan");
    }

    private void downloadResult() throws RunConfigException {
        logInfo("STEP 3: Downloading scan result");
        logInfo("===============================");


        Detailedreport scanReport = ResultOperationWrapper.downloadFullResult(formatScanIdInString());

        // Set it to service
        ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), ReportHolderService.class).setScanReport(scanReport);

        ApplicationManager.getApplication().invokeLater(new ResultToolWindowRunner());
/*
        // Wake result tool window up
        ToolWindow resultTW = ToolWindowManager.getInstance(JetbrainsIdeUtil.getCurrentActiveProject()).getToolWindow("Veracode SAST");
        ResultToolWindowHolderService resultTWHolderSvc = (ResultToolWindowHolderService) ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), ResultToolWindowHolderService.class);

        if (resultTWHolderSvc.getResultToolWindow() == null) {
            //ResultToolWindowFactory.createResultToolWindow();
            ApplicationManager.getApplication().invokeLater(new ResultToolWindowFactory());
        } else {
            resultTWHolderSvc.getResultToolWindow().refreshResultToolWindowUI();
        }*/


    }

    private void locateSandboxByName() throws RunConfigException {
        logInfo("Locating sandbox...");
        List<SandboxType> sandboxes = SandboxOperationWrapper.getSandboxListing(Long.toString(appId)).getSandbox();

        for (SandboxType sandbox: sandboxes) {
            if (scanConfig.getStatic_config().getSandbox_name().trim().equals(sandbox.getSandboxName().trim())) {
                logInfo("Found " + sandbox.getSandboxName() + " with ID = " + sandbox.getSandboxId() + ".");
                sandboxId = sandbox.getSandboxId();
                return;
            }
        }

        throw new RunConfigException("Sandbox with name " + scanConfig.getStatic_config().getSandbox_name() + " is not found.",
                "locate sandbox.");
    }


    /*
        Implement Runnable interface for intializing the tool window from Download config.
        As the config process is running as a background process, it cannot create tool window as the tool window
        can only be created by an UI thread. Thus, using invokeLater so that when the background process finishes and the
        control is returned back to UI thread, the tool window will be created.
    */
    class ResultToolWindowRunner implements Runnable {

        @Override
        public void run() {

            // Wake result tool window up
            ToolWindow resultTW = ToolWindowManager.getInstance(JetbrainsIdeUtil.getCurrentActiveProject()).getToolWindow("Veracode SAST");
            ResultToolWindowHolderService resultTWHolderSvc = (ResultToolWindowHolderService) ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), ResultToolWindowHolderService.class);

            if (resultTWHolderSvc.getResultToolWindow() == null) {
                ResultToolWindowFactory.createResultToolWindow();
                //ApplicationManager.getApplication().invokeLater(new ResultToolWindowFactory());
            } else {
                resultTWHolderSvc.getResultToolWindow().refreshResultToolWindowUI();
            }

        }
    }

}
