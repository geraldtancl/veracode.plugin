package com.veracode.cliang.sastPlugin.view.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.veracode.cliang.sastPlugin.objects.config.ScanConfiguration;
import com.veracode.cliang.sastPlugin.runConfig.runImpl.RunConfigBase;
import com.veracode.cliang.sastPlugin.runConfig.runImpl.RunScanConfig;
import com.veracode.cliang.sastPlugin.services.ApiCredentialHolderService;
import com.veracode.cliang.sastPlugin.services.RunConfigToolWindowHolderService;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;
import com.veracode.cliang.sastPlugin.view.dialogs.SetupApiCredentialDialog;
import com.veracode.cliang.sastPlugin.view.toolWindows.runConfig.RunConfigToolWindow;
import com.veracode.cliang.sastPlugin.view.toolWindows.runConfig.RunConfigToolWindowFactory;
import org.jetbrains.annotations.NotNull;


public class RunAutoScanAction extends RunAutoActionBase {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final ApiCredentialHolderService API_CREDENTIAL_HOLDER_SERVICE = (ApiCredentialHolderService) ServiceManager.getService(ApiCredentialHolderService.class);

        if (!API_CREDENTIAL_HOLDER_SERVICE.isCredentialAvailable()) {

            new SetupApiCredentialDialog().showAndGet();

        }

        ScanConfiguration scanConfig = selectConfiguration();

        if (scanConfig != null) {
            RunConfigToolWindow runConfigToolWindow;
            RunConfigToolWindowHolderService runConfigToolWindowHolderService = ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), RunConfigToolWindowHolderService.class);

            if (runConfigToolWindowHolderService.getRunConfigToolWindow() == null) {
                RunConfigToolWindowFactory.createRunConfigToolWindow("Scan");
            }

            runConfigToolWindow = runConfigToolWindowHolderService.getRunConfigToolWindow();
            RunConfigBase configRunner = new RunScanConfig(scanConfig, runConfigToolWindow);

            runConfigToolWindow.getRunConfigTW().activate(null);
            ApplicationManager.getApplication().executeOnPooledThread(configRunner);
        }
    }


}
