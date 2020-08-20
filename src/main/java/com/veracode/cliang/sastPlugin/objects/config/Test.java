package com.veracode.cliang.sastPlugin.objects.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.veracode.cliang.sastPlugin.utils.PluginLogger;

import java.lang.reflect.Type;
import java.util.Collection;

public class Test {
    private static final Class c = Test.class;
    public static void main(String[] args) {
        String jsonString = "[\n" +
                "  {\n" +
                "    \"config_name\": \"Policy Scan Config\",\n" +
                "    \"match_pattern\": \"master\",\n" +
                "    \"static_config\": {\n" +
                "      \"scan_type\": \"policy\",\n" +
                "      \"scan_naming\": \"timestamp\",\n" +
                "      \"upload_include_patterns\": [\n" +
                "        \"target/verademo.war\"\n" +
                "      ],\n" +
                "      \"upload_exclude_patterns\": [\n" +
                "      ]\n" +
                "    },\n" +
                "    \"portfolio\": {\n" +
                "      \"app_name\"=\"Verademo_IntelliJ\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"config_name\": \"Sandbox 1 Scan Config\",\n" +
                "    \"match_pattern\": \"feature/.*\",\n" +
                "    \"static_config\": {\n" +
                "      \"scan_type\": \"sandbox\",\n" +
                "      \"scan_naming\": \"timestamp\",\n" +
                "      \"upload_include_patterns\": [\n" +
                "        \"target/verademo.war\"\n" +
                "      ],\n" +
                "      \"upload_exclude_patterns\": [\n" +
                "      ]\n" +
                "    },\n" +
                "    \"portfolio\": {\n" +
                "      \"app_name\"=\"Verademo_IntelliJ\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"config_name\": \"Sandbox 2 Scan Config\",\n" +
                "    \"match_pattern\": \".*\",\n" +
                "    \"static_config\": {\n" +
                "      \"scan_type\": \"sandbox\",\n" +
                "      \"scan_naming\": \"timestamp\",\n" +
                "      \"upload_include_patterns\": [\n" +
                "        \"target/verademo.war\"\n" +
                "      ],\n" +
                "      \"upload_exclude_patterns\": [\n" +
                "      ]\n" +
                "    },\n" +
                "    \"portfolio\": {\n" +
                "      \"app_name\"=\"Verademo_IntelliJ\"\n" +
                "    }\n" +
                "  }\n" +
                "]";

        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<ScanConfiguration>>(){}.getType();
        Collection<ScanConfiguration> configs = gson.fromJson(jsonString, collectionType);

        PluginLogger.info(c, "Size of configs: " + configs.size());

        for (ScanConfiguration config: configs) {
            PluginLogger.info(c, config + "\n\n\n");
        }

    }
}
