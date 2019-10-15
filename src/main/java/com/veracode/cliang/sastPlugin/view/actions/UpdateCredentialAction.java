package com.veracode.cliang.sastPlugin.view.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.veracode.cliang.sastPlugin.view.dialogs.SetupApiCredentialDialog;
import org.jetbrains.annotations.NotNull;

public class UpdateCredentialAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        new SetupApiCredentialDialog().showAndGet();
    }
}
