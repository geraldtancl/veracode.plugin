package com.veracode.cliang.sastPlugin.view.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.components.ServiceManager;
import com.veracode.cliang.sastPlugin.objects.config.ScanConfiguration;
import com.veracode.cliang.sastPlugin.runConfigImpl.RunConfigBase;
import com.veracode.cliang.sastPlugin.runConfigImpl.RunDownloadConfig;
import com.veracode.cliang.sastPlugin.runConfigImpl.RunScanConfig;
import com.veracode.cliang.sastPlugin.services.ApiCredentialHolderService;
import com.veracode.cliang.sastPlugin.services.RunConfigToolWindowHolderService;
import com.veracode.cliang.sastPlugin.services.ScanConfigurationHolderService;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;
import com.veracode.cliang.sastPlugin.view.dialogs.SetupApiCredentialDialog;
import com.veracode.cliang.sastPlugin.view.toolWindows.runConfig.RunConfigToolWindow;
import com.veracode.cliang.sastPlugin.view.toolWindows.runConfig.RunConfigToolWindowFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class RunScanConfigAction extends AnAction {

    public RunScanConfigAction(String text, String description, Icon icon) {
        super(text, description, icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final ApiCredentialHolderService API_CREDENTIAL_HOLDER_SERVICE = (ApiCredentialHolderService) ServiceManager.getService(ApiCredentialHolderService.class);

        if (!API_CREDENTIAL_HOLDER_SERVICE.isCredentialAvailable()) {

            new SetupApiCredentialDialog().showAndGet();

        }

        String configName = e.getPresentation().getText();

        ScanConfigurationHolderService scanConfigurationHolderService = ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), ScanConfigurationHolderService.class);
        ScanConfiguration scanConfig = scanConfigurationHolderService.getScanConfigurationMap().get(configName);

        RunConfigToolWindow runConfigToolWindow;
        RunConfigToolWindowHolderService runConfigToolWindowHolderService = ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), RunConfigToolWindowHolderService.class);

        if (runConfigToolWindowHolderService.getRunConfigToolWindow() == null) {
            RunConfigToolWindowFactory.createRunConfigToolWindow(configName);
        }

        runConfigToolWindow = runConfigToolWindowHolderService.getRunConfigToolWindow();

        RunConfigBase configRunner;

        if (configName.startsWith(RunConfigActionGroup.PREFIX_RUN_SCAN)) {
            configRunner = new RunScanConfig(scanConfig, runConfigToolWindow);
        } else {
            configRunner = new RunDownloadConfig(scanConfig, runConfigToolWindow);

        }
        //configRunner.runConfig();
        runConfigToolWindow.getRunConfigTW().activate(null);
        ApplicationManager.getApplication().executeOnPooledThread(configRunner);
        //new Thread(configRunner).run();
        //ApplicationManager.getApplication().invokeLater(configRunner, ModalityState.defaultModalityState());
    }
}
