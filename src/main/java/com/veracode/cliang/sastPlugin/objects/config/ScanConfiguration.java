package com.veracode.cliang.sastPlugin.objects.config;

public class ScanConfiguration {
    private String config_name;
    private String branch_pattern;
    private StaticAnalysisConfig static_config;
    private AppProfile portfolio;

    public String getConfig_name() {
        return config_name;
    }

    public String getBranch_pattern() {
        return branch_pattern;
    }


    public StaticAnalysisConfig getStatic_config() {
        return static_config;
    }

    public AppProfile getPortfolio() {
        return portfolio;
    }

    /*
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Config_name: " + config_name + "\n");
        str.append(static_config + "\n");
        str.append(portfolio + "\n");
        return str.toString();

    }
    */

}
