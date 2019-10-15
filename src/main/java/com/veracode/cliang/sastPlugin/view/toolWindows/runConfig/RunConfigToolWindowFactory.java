package com.veracode.cliang.sastPlugin.view.toolWindows.runConfig;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.veracode.cliang.sastPlugin.services.ResultToolWindowHolderService;
import com.veracode.cliang.sastPlugin.services.RunConfigToolWindowHolderService;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;
import com.veracode.cliang.sastPlugin.view.toolWindows.result.ResultToolWindow;

//public class ResultToolWindowFactory implements ToolWindowFactory {
public class RunConfigToolWindowFactory {

    public static void createRunConfigToolWindow(String configName) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(JetbrainsIdeUtil.getCurrentActiveProject())
                .registerToolWindow("Veracode Run Config", false, ToolWindowAnchor.BOTTOM);

        RunConfigToolWindow runConfigTW = new RunConfigToolWindow(toolWindow, configName);

        RunConfigToolWindowHolderService runConfigToolWindowHolderService = ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), RunConfigToolWindowHolderService.class);

        runConfigToolWindowHolderService.setRunConfigToolWindow(runConfigTW);


    }


}
