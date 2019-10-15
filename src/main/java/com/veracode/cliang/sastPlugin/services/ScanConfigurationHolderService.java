package com.veracode.cliang.sastPlugin.services;

import com.veracode.cliang.sastPlugin.objects.config.ScanConfiguration;

import java.util.Collection;
import java.util.Map;

public class ScanConfigurationHolderService {

    private Map<String, ScanConfiguration> scanConfigurationMap;

    public Collection<ScanConfiguration> getScanConfigs() {
        return scanConfigs;
    }

    public void setScanConfigs(Collection<ScanConfiguration> scanConfigs) {
        this.scanConfigs = scanConfigs;
    }

    private Collection<ScanConfiguration> scanConfigs;

    public Map<String, ScanConfiguration> getScanConfigurationMap() {
        return scanConfigurationMap;
    }

    public void setScanConfigurationMap(Map<String, ScanConfiguration> scanConfigurationMap) {
        this.scanConfigurationMap = scanConfigurationMap;
    }
}
