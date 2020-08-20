package com.veracode.cliang.sastPlugin.view.actions;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.veracode.cliang.sastPlugin.objects.config.ScanConfiguration;
import com.veracode.cliang.sastPlugin.runConfig.repoSupport.CodeRepo;
import com.veracode.cliang.sastPlugin.runConfig.repoSupport.RepositoryFinder;
import com.veracode.cliang.sastPlugin.services.ScanConfigurationHolderService;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;
import com.veracode.cliang.sastPlugin.utils.PluginLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.regex.Pattern;

public abstract class RunAutoActionBase extends AnAction {

    private static final Class c = RunAutoActionBase.class;

    @Override
    public void update(@NotNull AnActionEvent e) {
        PluginLogger.info(c, "update");
        e.getPresentation().setEnabled(false);

        String path = JetbrainsIdeUtil.getCurrentActiveProject().getBasePath() + File.separator + "veracode.config";

        VirtualFile configFile = LocalFileSystem.getInstance().findFileByIoFile(new File(path));

        if (configFile != null) {
            try {
                String configJson = new String(configFile.contentsToByteArray());

                Gson gson = new Gson();
                Type collectionType = new TypeToken<Collection<ScanConfiguration>>(){}.getType();
                Collection<ScanConfiguration> configs = gson.fromJson(configJson, collectionType);

                ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), ScanConfigurationHolderService.class).setScanConfigs(configs);

                if (configs.size() > 0) {
                    e.getPresentation().setEnabled(true);
                }
            } catch (IOException ex) {
                PluginLogger.error(c, ex.getMessage(), ex);
            }
        }
    }

    protected ScanConfiguration selectConfiguration() {
        Collection<ScanConfiguration> scanConfigs = ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), ScanConfigurationHolderService.class).getScanConfigs();

        // Find out which type of repo in use
        CodeRepo repo = RepositoryFinder.findRepoInUsed();
        String branchName;

        if (repo == null) { // No repo in used
            branchName = ""; // This will result in the match into default branch with regex ".*"
        } else {
            branchName = repo.obtainBranchName();
        }

        for (ScanConfiguration scanConfig: scanConfigs) {
            boolean isMatched = Pattern.matches(scanConfig.getBranch_pattern(), branchName);
            PluginLogger.info(c, "Branch name: [" + branchName + "], " + isMatched);
            if (isMatched) {
                return scanConfig;
            }

        }

        // if the code runs to this part, we really don't know how to handle it - this is extremely unlikely
        PluginLogger.info(c, "Run into an unexpected scenario.");


        return null;
    }


}
