package com.veracode.cliang.sastPlugin.runConfig.runImpl;

public class RunConfigException extends Exception {
    private String stepName;

    public RunConfigException(String description, String stepName) {
        super(description);
        this.stepName = stepName;
    }

    public String getStepName() {
        return stepName;
    }
}
