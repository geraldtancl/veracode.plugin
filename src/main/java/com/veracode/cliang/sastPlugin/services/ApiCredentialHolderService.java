package com.veracode.cliang.sastPlugin.services;

import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;

public class ApiCredentialHolderService {
    private String apiId;
    private String apiKey;

    public boolean isCredentialAvailable() {
        if (apiId == null || apiKey == null ||
                apiId.trim().length() == 0 || apiKey.trim().length() == 0) {
            // Attempt to load the credential
            loadStoredCredential();

            if (apiId == null || apiKey == null ||
                    apiId.trim().length() == 0 || apiKey.trim().length() == 0) {
                return false;
            }

        }

        return true;
    }

    private void loadStoredCredential() {
        String[] credential = JetbrainsIdeUtil.loadApiCredential();

        if (credential != null) {
            apiId = credential[0];
            apiKey = credential[1];
        }
    }

    public void updateCredential(String apiId, String apiKey) {
        this.apiId = apiId;
        this.apiKey = apiKey;
        JetbrainsIdeUtil.saveApiCredential(apiId, apiKey);
    }

    public void resetCredential() {
        this.apiId = null;
        this.apiKey = null;
        JetbrainsIdeUtil.resetApiCredential();
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
