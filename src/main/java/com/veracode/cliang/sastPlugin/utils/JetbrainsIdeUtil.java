package com.veracode.cliang.sastPlugin.utils;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.*;
import java.awt.*;

public class JetbrainsIdeUtil {


    private static final String VERACODE_PLUGIN_CREDENTIAL_KEY = "Veracode_Plugin_Credential";

    public static Project getCurrentActiveProject() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Project activeProject = null;
        for (Project project : projects) {
            Window window = WindowManager.getInstance().suggestParentWindow(project);

            if (window != null && window.isActive()) {
                activeProject = project;

            } else System.out.println();
        }

        return activeProject;
    }

    public static void saveApiCredential(String apiId, String apiKey) {
        CredentialAttributes credentialAttributes = createCredentialAttributes(VERACODE_PLUGIN_CREDENTIAL_KEY); // see previous sample
        Credentials credentials = new Credentials(apiId, apiKey);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);
    }

    public static String[] loadApiCredential() {

        CredentialAttributes credentialAttributes = createCredentialAttributes(VERACODE_PLUGIN_CREDENTIAL_KEY);
        Credentials credentials = PasswordSafe.getInstance().get(credentialAttributes);

        if (credentials != null) {
            return new String[]{credentials.getUserName(), credentials.getPasswordAsString()};
        }

        return null;
    }

    public static void resetApiCredential() {
        CredentialAttributes credentialAttributes = createCredentialAttributes(VERACODE_PLUGIN_CREDENTIAL_KEY);
        PasswordSafe.getInstance().set(credentialAttributes, null);

    }

    private static CredentialAttributes createCredentialAttributes(String key) {
        return new CredentialAttributes(CredentialAttributesKt.generateServiceName("Veracode API Login", key));
    }



}
