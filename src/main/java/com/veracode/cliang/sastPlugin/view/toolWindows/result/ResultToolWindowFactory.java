package com.veracode.cliang.sastPlugin.view.toolWindows.result;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.veracode.cliang.sastPlugin.services.ResultToolWindowHolderService;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;


public class ResultToolWindowFactory {

    public static void createResultToolWindow() {
        ToolWindow toolWindow = ToolWindowManager.getInstance(JetbrainsIdeUtil.getCurrentActiveProject())
                .registerToolWindow("Veracode SAST", false, ToolWindowAnchor.BOTTOM);

        ResultToolWindow resultToolWindow = new ResultToolWindow(toolWindow);
        ResultToolWindowHolderService resultTWHolderService = ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), ResultToolWindowHolderService.class);
        resultTWHolderService.setResultToolWindow(resultToolWindow);

    }


}
