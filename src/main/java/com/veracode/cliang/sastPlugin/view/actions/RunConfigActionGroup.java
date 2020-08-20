package com.veracode.cliang.sastPlugin.view.actions;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.veracode.cliang.sastPlugin.objects.config.ScanConfiguration;
import com.veracode.cliang.sastPlugin.services.ScanConfigurationHolderService;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;
import com.veracode.cliang.sastPlugin.utils.PluginLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class RunConfigActionGroup extends ActionGroup {

    private static final Class c = RunConfigActionGroup.class;

    public static final String PREFIX_RUN_SCAN = "Scan with";
    public static final String PREFIX_DOWNLOAD = "Download";

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent e) {

        String path = JetbrainsIdeUtil.getCurrentActiveProject().getBasePath() + File.separator + "veracode.config";

        VirtualFile configFile = LocalFileSystem.getInstance().findFileByIoFile(new File(path));

        if (configFile != null) {
            try {
                String configJson = new String(configFile.contentsToByteArray());

                Gson gson = new Gson();
                Type collectionType = new TypeToken<Collection<ScanConfiguration>>(){}.getType();
                Collection<ScanConfiguration> configs = gson.fromJson(configJson, collectionType);

                if (configs.size() > 0) {
                    List<AnAction> actions = new ArrayList<>();
                    Map<String, ScanConfiguration> configMap = new HashMap<>();

                    // Construct scan actions
                    String displayName;

                    for (ScanConfiguration scanConfig: configs) {
                        displayName = "Scan with \"" + scanConfig.getConfig_name() + "\"";
                        actions.add(new RunScanConfigAction(displayName, displayName, null));
                        configMap.put(displayName, scanConfig);
                    }

                    actions.add(new Separator("Download Latest Result"));

                    // Construct download actions
                    for (ScanConfiguration scanConfig: configs) {
                        displayName = "Download \"" + scanConfig.getConfig_name() + "\"";
                        actions.add(new RunScanConfigAction(displayName, displayName, null));
                        configMap.put(displayName, scanConfig);
                    }

                    // Put the hashmap into project service
                    ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), ScanConfigurationHolderService.class).setScanConfigurationMap(configMap);

                    return actions.toArray(new AnAction[actions.size()]);
                }
            } catch (IOException ex) {
                PluginLogger.error(c, ex.getMessage(), ex);
            }
        }


        return new AnAction[0];
    }
}
