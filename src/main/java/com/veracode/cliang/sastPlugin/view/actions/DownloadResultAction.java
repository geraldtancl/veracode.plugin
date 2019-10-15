package com.veracode.cliang.sastPlugin.view.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.veracode.cliang.sastPlugin.services.ApiCredentialHolderService;
import com.veracode.cliang.sastPlugin.view.dialogs.ResultSelectorDialog;
import com.veracode.cliang.sastPlugin.view.dialogs.SetupApiCredentialDialog;
import org.jetbrains.annotations.NotNull;

public class DownloadResultAction extends AnAction {

    private static final ApiCredentialHolderService API_CREDENTIAL_HOLDER_SERVICE = (ApiCredentialHolderService) ServiceManager.getService(ApiCredentialHolderService.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        if (!API_CREDENTIAL_HOLDER_SERVICE.isCredentialAvailable()) {

            new SetupApiCredentialDialog().showAndGet();

        }

        new ResultSelectorDialog().showAndGet();


    }
}
