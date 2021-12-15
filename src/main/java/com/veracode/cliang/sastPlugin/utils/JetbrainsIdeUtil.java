package com.veracode.cliang.sastPlugin.utils;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.wm.WindowManager;
import org.apache.commons.lang3.SystemUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

    public static String[] loadApiCredential() {
        /* This is changed under feature/use-credential-file. API ID and Key will be read from Veracode credential file instead.
        CredentialAttributes credentialAttributes = createCredentialAttributes(VERACODE_PLUGIN_CREDENTIAL_KEY);
        Credentials credentials = PasswordSafe.getInstance().get(credentialAttributes);

        if (credentials != null) {
            return new String[]{credentials.getUserName(), credentials.getPasswordAsString()};
        }
        */

        final File USER_HOME = SystemUtils.getUserHome();
        final String PATH_SUFFIX = File.separator + ".veracode" + File.separator + "credentials";
        final String CREDENTIAL_FILE_PATH = USER_HOME + PATH_SUFFIX;

        String[] credential = null;
        File credentialFile = new File(CREDENTIAL_FILE_PATH);

        PluginLogger.info(c,"CREDENTIAL FILE PATH: " + CREDENTIAL_FILE_PATH);

        if (credentialFile.exists()) {
            PluginLogger.info(c, "Credential file exists.");

            try (InputStream input = new FileInputStream(credentialFile)) {

                Properties prop = new Properties();

                // load a properties file
                prop.load(input);

                final String API_ID = prop.getProperty("veracode_api_key_id");
                final String API_KEY = prop.getProperty("veracode_api_key_secret");

                if (API_ID.trim().length() > 0 && API_KEY.trim().length() > 0) {
                    PluginLogger.info(c, "Length of API ID and KEY - " + API_ID.length() + " and " + API_KEY.length());

                    credential = new String[] {API_ID, API_KEY};
                }


            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }



        return credential;

    }

    // Update and reset function of credential is no longer needed - change by feature/use-credential-file
    // For changing credential, change the credential file directly.
    /*
    public static void saveApiCredential(String apiId, String apiKey) {
        CredentialAttributes credentialAttributes = createCredentialAttributes(VERACODE_PLUGIN_CREDENTIAL_KEY); // see previous sample
        Credentials credentials = new Credentials(apiId, apiKey);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);
    }

    public static void resetApiCredential() {
        CredentialAttributes credentialAttributes = createCredentialAttributes(VERACODE_PLUGIN_CREDENTIAL_KEY);
        PasswordSafe.getInstance().set(credentialAttributes, null);

    }

     */

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
