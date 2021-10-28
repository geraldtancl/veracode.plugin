package com.veracode.cliang.sastPlugin.utils;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.wm.WindowManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class JetbrainsIdeUtil {

    private static final Class c = JetbrainsIdeUtil.class;

    private static final String VERACODE_PLUGIN_CREDENTIAL_KEY = "Veracode_Plugin_Credential";

    public static Project getCurrentActiveProject() {

        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Project activeProject = null;
        for (Project project : projects) {
            Window window = WindowManager.getInstance().suggestParentWindow(project);

            if (window != null && window.isActive()) {
                activeProject = project;

            } else {
                PluginLogger.info(c, "No active project");
            }
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

    public static File copyActiveProjectToTemp() {
        PluginLogger.info(c, "Temp Directory: " + FileUtil.getTempDirectory());

        try {
            File tempDir = new File(FileUtil.getTempDirectory());

            if (tempDir.exists()) {

                // Create a new folder to prevent overwriting

                File copyDestFolder = new File(tempDir, getCurrentActiveProject().getName() + "-" + System.currentTimeMillis());
                PluginLogger.info(c, copyDestFolder.mkdir()? "Child dir created":"Child dir not created");
                System.out.println(getCurrentActiveProject());
                System.out.println("Current Active Project Base Path: " + getCurrentActiveProject().getBasePath());
                System.out.println("Before copy");

                FileUtil.copyDir(new File(getCurrentActiveProject().getBasePath()), copyDestFolder);
                return copyDestFolder;

            }
        } catch (IOException e) {
            PluginLogger.error(c, e.getMessage(), e);

        } catch (Exception e) {
            System.out.println("Caught exception.");
            System.out.println(e);
        }

        return null;
    }

    /*public static VirtualFile[] getAllFilesInCurrActiveProject() {
        //ProjectRootManager prm = ProjectRootManager.getInstance(getCurrentActiveProject());
        ProjectFileIndex pfi = ProjectFileIndex.getInstance(getCurrentActiveProject());
        pfi.iterateContent();
        return null;
    }
*/
}
