package com.veracode.cliang.sastPlugin.view.toolWindows.runConfig;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;

public class RunConfigToolWindow {

    private ToolWindow runConfigTW;
    private String configName;
    private ConsoleView consoleView;
    private Content consoleContent;

    public ToolWindow getRunConfigTW() {
        return runConfigTW;
    }

    public RunConfigToolWindow(ToolWindow runConfigTW, String configName) {
        this.runConfigTW = runConfigTW;
        this.configName = configName;

        init();
    }

    private void init() {
        consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(JetbrainsIdeUtil.getCurrentActiveProject()).getConsole();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        consoleContent = contentFactory.createContent(consoleView.getComponent(), configName, false);
        runConfigTW.getContentManager().addContent(consoleContent);
    }

    public void reInit(String configName) {
        this.configName = configName;
        consoleContent.setDisplayName(configName);
        consoleView.clear();
    }

    public void logInfoMessage(String message) {
        consoleView.print(message, ConsoleViewContentType.LOG_INFO_OUTPUT);
        consoleView.getComponent().revalidate();

    }

    public void logErrorMessage(String message) {
        consoleView.print(message, ConsoleViewContentType.LOG_ERROR_OUTPUT);
        consoleView.getComponent().revalidate();
    }

}
