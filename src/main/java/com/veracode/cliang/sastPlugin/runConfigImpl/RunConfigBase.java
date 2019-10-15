package com.veracode.cliang.sastPlugin.runConfigImpl;

import com.veracode.cliang.sastPlugin.objects.config.ScanConfiguration;
import com.veracode.cliang.sastPlugin.objects.config.StaticAnalysisConfig;
import com.veracode.cliang.sastPlugin.view.toolWindows.runConfig.RunConfigToolWindow;

public abstract class RunConfigBase implements Runnable {
    protected ScanConfiguration scanConfig;
    protected RunConfigToolWindow consoleTW;
    protected long appId, sandboxId, scanId;

    public RunConfigBase(ScanConfiguration scanConfig, RunConfigToolWindow consoleTW) {
        this.scanConfig = scanConfig;
        this.consoleTW = consoleTW;
    }

    @Override
    public void run() {
        runConfig();
    }

    protected abstract boolean runConfig();

    protected boolean isSandboxScan() {
        return scanConfig.getStatic_config().getScan_type().equals(StaticAnalysisConfig.SANDBOX_SCAN);
    }

    protected String formatAppIdInString() {
        return Long.toString(appId);
    }

    protected String formatScanIdInString() {
        return Long.toString(scanId);
    }

    protected String formatSandboxIdInString() {
        return Long.toString(sandboxId);
    }

    protected String determineSandboxIdValue() {
        if (isSandboxScan()) {
            return formatSandboxIdInString();
        } else {
            return null;
        }
    }

    protected void logInfo(String message) {
        consoleTW.logInfoMessage(message + "\n");

    }

    protected void logError(String message) {
        consoleTW.logErrorMessage(message + "\n");
    }
}
