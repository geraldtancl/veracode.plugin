package com.veracode.cliang.sastPlugin.runConfig.runImpl;

import com.veracode.cliang.sastPlugin.apiWrapper.ApplicationOperationWrapper;
import com.veracode.cliang.sastPlugin.apiWrapper.SandboxOperationWrapper;
import com.veracode.cliang.sastPlugin.apiWrapper.ScanOperationWrapper;
import com.veracode.cliang.sastPlugin.objects.config.ScanConfiguration;
import com.veracode.cliang.sastPlugin.objects.raw.appList.AppType;
import com.veracode.cliang.sastPlugin.objects.raw.appList.Applist;
import com.veracode.cliang.sastPlugin.objects.raw.buildInfo.Buildinfo;
import com.veracode.cliang.sastPlugin.objects.raw.sandboxList.SandboxType;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;
import com.veracode.cliang.sastPlugin.view.toolWindows.runConfig.RunConfigToolWindow;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class RunScanConfig extends RunConfigBase {



    public RunScanConfig(ScanConfiguration scanConfig, RunConfigToolWindow consoleTW) {
        super(scanConfig, consoleTW);
    }

    @Override
    protected boolean runConfig() {

        logInfo("Running scan configuration: " + scanConfig.getConfig_name());
        logInfo("");


        try {
            // Step 1: Locate the application
            locateAppByName();

            logInfo("\n\n");

            // Step 2: Creating a scan
            createScan();

            logInfo("\n\n");

            // Step 3: Upload files
            uploadFiles();

            logInfo("\n\n");

            // Step 4: Begin pre-scan
            beginPresanAndStaticScan();

            logInfo("\n\n");

            logInfo("Run config process completed. Proceed to download the scan result using the download config; " +
                    "or with manual download after you receive the scan completion acknowledgement email.");



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

    private void createScan() throws RunConfigException {
        logInfo("Step 2: Create a scan");
        logInfo("=====================");
        logInfo(isSandboxScan()? "This is a sandbox scan." : "This is a policy scan.");

        Calendar cal = Calendar.getInstance();
        String scanName = cal.getTime().toString();
        Buildinfo buildsInfo;

        if (isSandboxScan()) {
            locateSandboxByName();
            buildsInfo = ScanOperationWrapper.createSandboxScan(Long.toString(appId), scanName, Long.toString(sandboxId));
        } else {
            buildsInfo = ScanOperationWrapper.createPolicyScan(Long.toString(appId), scanName);
        }

        if (buildsInfo.getBuild() != null) {
            scanId = buildsInfo.getBuild().getBuildId();
            logInfo("Scan with name " + scanName + " created. ID = " + scanId);
            return;
        }

        throw new RunConfigException("Unable to create a scan.", "create scan");

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

    private void uploadFiles() {
        logInfo("Step 3: Upload files");
        logInfo("====================");
        for (String filePattern: scanConfig.getStatic_config().getUpload_include_patterns()) {
            String _pattern = new String(filePattern);

            _pattern = _pattern.replace("/", File.separator);
            _pattern = _pattern.replace("\\", File.separator);

            if (!_pattern.substring(0, 1).equals(File.separator)) {
                _pattern = File.separator + _pattern;
            }

            String filePath = JetbrainsIdeUtil.getCurrentActiveProject().getBasePath() + _pattern;

            logInfo("Uploading " + filePath);
            ScanOperationWrapper.uploadFile(Long.toString(appId), isSandboxScan()? Long.toString(sandboxId) : null, filePath);
        }
    }

    private void beginPresanAndStaticScan() {
        logInfo("Step 4: Begin Presan");
        logInfo("====================");
        logInfo("Begin presacn process. Static analysis will start after right after it if there is no issue with presacn.");

        if (isSandboxScan()) {
            ScanOperationWrapper.beginPrescanAndStaticScan(Long.toString(appId), Long.toString(sandboxId));
        } else {
            ScanOperationWrapper.beginPrescanAndStaticScan(Long.toString(appId), null);
        }
    }

    /*private void monitorScanStatus() {
        logInfo("Step 5: Wait...");
        logInfo("===============");

        boolean scanCompleted = false;
        Buildinfo buildInfo;
        String scanStatus;

        do {
            logInfo("Getting status update...");
            buildInfo = ScanOperationWrapper.getScanStatus(formatAppIdInString(), formatScanIdInString(), determineSandboxIdValue());
            int totalModule = 0, numPrescan = 0, numScanInProcess = 0, numCompleted = 0;

            for (AnalysisUnitType module: buildInfo.getBuild().getAnalysisUnit()) {
                totalModule++;


            }
        } while (!scanCompleted);
    }*/
}
