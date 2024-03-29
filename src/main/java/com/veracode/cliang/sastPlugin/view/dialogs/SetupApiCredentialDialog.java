package com.veracode.cliang.sastPlugin.view.dialogs;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.veracode.cliang.sastPlugin.services.ApiCredentialHolderService;
import com.veracode.cliang.sastPlugin.utils.PluginLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SetupApiCredentialDialog extends DialogWrapper {

    private static final Class c = SetupApiCredentialDialog.class;

    private static final ApiCredentialHolderService API_CREDENTIAL_HOLDER_SERVICE = (ApiCredentialHolderService) ServiceManager.getService(ApiCredentialHolderService.class);

    JTextArea apiIdTextArea;
    JTextArea apiKeyTextArea;

    public SetupApiCredentialDialog() {
        super(true); // use current window as parent
        init();
        setTitle("Setup your API Credential...");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        final String INSTRUCTION = "Oops! We could not locate any credential! \n\n" +
                "This plugin is leveraging Veracode credential files. " +
                "Please confirm the credential file is located at [User Home]/.veracode/credentials. \n\n" +
                "For more information, please refer to https://docs.veracode.com/r/c_configure_api_cred_file";

        JPanel parentPanel = new JPanel();
        JTextPane setupInstructionTextPane = new JTextPane();
        setupInstructionTextPane.setText(INSTRUCTION);

        parentPanel.add(setupInstructionTextPane);
        // Update and reset function of credential is no longer needed - change by feature/use-credential-file
        // For changing credential, change the credential file directly.
        /*
        parentPanel.setLayout(new GridLayout(0, 2));

        parentPanel.add(new JBLabel("API ID"));

        apiIdTextArea = new JBTextArea();
        apiIdTextArea.setMinimumSize(new Dimension(200, 0));
        apiIdTextArea.setMaximumSize(new Dimension(200, 100));
        apiIdTextArea.setLineWrap(true);
        apiIdTextArea.setWrapStyleWord(false);

        if (API_CREDENTIAL_HOLDER_SERVICE.isCredentialAvailable()) {
            apiIdTextArea.setText(API_CREDENTIAL_HOLDER_SERVICE.getApiId());
        }

        parentPanel.add(apiIdTextArea);

        parentPanel.add(new JLabel("API Key"));

        apiKeyTextArea = new JBTextArea();
        apiKeyTextArea.setLineWrap(true);
        apiKeyTextArea.setWrapStyleWord(false);
        JScrollPane apiKeyScrollPane = new JBScrollPane(apiKeyTextArea);
        apiKeyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        apiKeyScrollPane.setMinimumSize(new Dimension(200, 0));
        apiKeyScrollPane.setPreferredSize(new Dimension(200, 100));
        apiKeyScrollPane.setMaximumSize(new Dimension(200, 100));

        if (API_CREDENTIAL_HOLDER_SERVICE.isCredentialAvailable()) {
            apiKeyTextArea.setText(API_CREDENTIAL_HOLDER_SERVICE.getApiKey());
        }

        parentPanel.add(apiKeyScrollPane);

        PluginLogger.info(c, "API ID = " + API_CREDENTIAL_HOLDER_SERVICE.getApiId());

         */


        return parentPanel;
    }

    @Override
    protected void doOKAction() {

        super.doOKAction();
    }

    // Update and reset function of credential is no longer needed - change by feature/use-credential-file
    // For changing credential, change the credential file directly.
    /*

    @Override
    protected void doOKAction() {
        API_CREDENTIAL_HOLDER_SERVICE.updateCredential(apiIdTextArea.getText(), apiKeyTextArea.getText());
        super.doOKAction();
    }

    @NotNull
    @Override
    protected Action[] createLeftSideActions() {
        Action clearCredentialAction = new ClearCredentialAction(this);
        return new Action[]{clearCredentialAction};
    }

    class ClearCredentialAction extends AbstractAction {

        SetupApiCredentialDialog dialog;

        ClearCredentialAction(SetupApiCredentialDialog dialog) {
            super("Clear Credential");
            this.dialog = dialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final ApiCredentialHolderService apiCredentialHolderService = (ApiCredentialHolderService) ServiceManager.getService(ApiCredentialHolderService.class);
            apiCredentialHolderService.resetCredential();
            dialog.close(0);
        }
    }

     */
}
