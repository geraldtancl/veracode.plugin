package com.veracode.cliang.sastPlugin.runConfig.runImpl;

import com.veracode.cliang.sastPlugin.apiWrapper.ApplicationOperationWrapper;
import com.veracode.cliang.sastPlugin.apiWrapper.SandboxOperationWrapper;
import com.veracode.cliang.sastPlugin.apiWrapper.ScanOperationWrapper;
import com.veracode.cliang.sastPlugin.objects.config.ScanConfiguration;
import com.veracode.cliang.sastPlugin.objects.raw.appList.AppType;
import com.veracode.cliang.sastPlugin.objects.raw.appList.Applist;
import com.veracode.cliang.sastPlugin.objects.raw.buildInfo.Buildinfo;
import com.veracode.cliang.sastPlugin.objects.raw.sandboxList.SandboxType;
import com.veracode.cliang.sastPlugin.utils.DirectoryZipper;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;
import com.veracode.cliang.sastPlugin.utils.PluginLogger;
import com.veracode.cliang.sastPlugin.view.toolWindows.runConfig.RunConfigToolWindow;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class RunScanConfig extends RunConfigBase {

    private static final Class c = RunScanConfig.class;

    private File projectInTemp = null, zipOutput = null;

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

            if (scanId != 0) {
                PluginLogger.info(c, formatScanIdInString());
                if (isSandboxScan()) {
                    ScanOperationWrapper.deleteSandboxScan(formatAppIdInString(), formatSandboxIdInString());
                } else {
                    ScanOperationWrapper.deletePolicyScan(formatAppIdInString());
                }
            }
            return false;
        } catch (Exception e) {
            logError(e.getMessage());
        } finally {
            cleanUp();
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

    private void uploadFiles() throws RunConfigException {
        logInfo("Step 3: Upload files");
        logInfo("====================");
        logInfo("Copying the project into temp directory...");

        projectInTemp = JetbrainsIdeUtil.copyActiveProjectToTemp();

        if (projectInTemp != null) {
            logInfo("The folder path to the project in temp is " + projectInTemp.getAbsolutePath());
        } else {
            logError("Unsuccessful copying of project.");
            throw new RunConfigException("Unsuccessful copying project.", "upload files");
        }


        List<Pattern> includeFilePatterns = createFilePatternList(scanConfig.getStatic_config().getUpload_include_patterns());
        List<Pattern> excludeFilePatterns = createFilePatternList(scanConfig.getStatic_config().getUpload_exclude_patterns());

        if (includeFilePatterns.size() == 0) {
            throw new RunConfigException("No include pattern specify!", "Upload files step - matching files");
        }

        logInfo("The following files will be included for upload:");
        matchFilesForUpload(projectInTemp, projectInTemp, includeFilePatterns, excludeFilePatterns);

        if (FileUtils.listFiles(projectInTemp, null, true).size() == 0) {
            throw new RunConfigException("There are no file to be uploaded.", "Upload files step - matching files");
        }

        // Create a zip of the project which after the files are matched
        zipOutput = new File(projectInTemp.getParentFile().getAbsolutePath() + File.separator + projectInTemp.getName() + ".zip");
        DirectoryZipper dirZipper = new DirectoryZipper(projectInTemp, zipOutput);
        dirZipper.createZip();

        logInfo("Zip file to be uploaded: " + zipOutput.getAbsolutePath() + "; size: " + zipOutput.length() / (1024 * 1024) + " MB");

        logInfo("Uploading " + zipOutput.getAbsolutePath());
        ScanOperationWrapper.uploadFile(Long.toString(appId), isSandboxScan()? Long.toString(sandboxId) : null, zipOutput.getAbsolutePath());




        /*for (String filePattern: scanConfig.getStatic_config().getUpload_include_patterns()) {
            String _pattern = new String(filePattern);

            _pattern = _pattern.replace("/", File.separator);
            _pattern = _pattern.replace("\\", File.separator);

            if (!_pattern.substring(0, 1).equals(File.separator)) {
                _pattern = File.separator + _pattern;
            }

            String filePath = JetbrainsIdeUtil.getCurrentActiveProject().getBasePath() + _pattern;

            logInfo("Uploading " + filePath);
            ScanOperationWrapper.uploadFile(Long.toString(appId), isSandboxScan()? Long.toString(sandboxId) : null, filePath);
        }*/
    }

    private void beginPresanAndStaticScan() {
        logInfo("Step 4: Begin Presan");
        logInfo("====================");
        logInfo("Begin prescan process. Static analysis will start after right after it if there is no issue with presacn.");

        if (isSandboxScan()) {
            ScanOperationWrapper.beginPrescanAndStaticScan(Long.toString(appId), Long.toString(sandboxId));
        } else {
            ScanOperationWrapper.beginPrescanAndStaticScan(Long.toString(appId), null);
        }
    }

    private List<Pattern> createFilePatternList(String[] patternStrings) {
        List<Pattern> patternList = new ArrayList<>();

        for (String filePattern: patternStrings) {
            String _pattern = new String(filePattern);

            _pattern = _pattern.replace("/", File.separator);
            _pattern = _pattern.replace("\\", File.separator);

            patternList.add(Pattern.compile(_pattern));
        }

        return patternList;
    }

    private void matchFilesForUpload(File root, File projectRoot, List<Pattern> includeFilePatterns, List<Pattern> excludeFilePatterns) {
        File[] projectFileList = root.listFiles();
        //List<File> finalFileList = new ArrayList<>();

        // Matching files
        for (File file: projectFileList) {
            if (file.isDirectory()) {
                // recursive call
                matchFilesForUpload(file, projectRoot, includeFilePatterns, excludeFilePatterns);
            }

            String relPath = extractRelativePath(projectRoot, file);

            boolean _include = false;

            for (Pattern incP: includeFilePatterns) {
                // Check if this file matches include pattern
                if (incP.matcher(relPath).matches()) {
                    _include = true;

                    // Check if this file matches exclude pattern
                    for (Pattern excP: excludeFilePatterns) {
                        if (excP.matcher(relPath).matches()) {
                            // This file is NOT needed! Mark for deletion!

                            _include = false;

                            break;
                        }
                    }

                    break;
                }
            }

            if (_include) {
                // This file is needed
                //finalFileList.add(file);
                logInfo("Include: " + relPath);
            } else {
                file.delete();
                //logInfo("Exclude: " + relPath + ", delete: " + (file.delete()? "Deleted" : "Not deleted"));
            }


        }

    }

    private String extractRelativePath(File base, File file) {
        int rootLength = base.getAbsolutePath().length();
        String absFileName = file.getAbsolutePath();
        String relFileName = absFileName.substring(rootLength + 1);

        return relFileName;
    }

    private void cleanUp() {
        logInfo("\n\n");
        logInfo("Perform clean up...");

        // Clean up the file created
        logInfo("Removing temporary file created...");

        if (zipOutput != null) {
            FileUtils.deleteQuietly(zipOutput);
        }

        if (projectInTemp != null) {
            FileUtils.deleteQuietly(projectInTemp);
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
