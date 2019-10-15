package com.veracode.cliang.sastPlugin.objects.config;

public class StaticAnalysisConfig {

    public static final String POLICY_SCAN = "policy";
    public static final String SANDBOX_SCAN = "sandbox";

    private String scan_type;
    private String sandbox_name;
    private String scan_naming;
    private String[] upload_include_patterns;
    private String[] upload_exclude_patterns;

    public String getScan_type() {
        return scan_type;
    }

    public String getScan_naming() {
        return scan_naming;
    }

    public String getSandbox_name() {
        return sandbox_name;
    }

    public String[] getUpload_include_patterns() {
        return upload_include_patterns;
    }

    public String[] getUpload_exclude_patterns() {
        return upload_exclude_patterns;
    }

    /*@Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("scan_type: " + scan_type + "\n");
        str.append("scan_naming: " + scan_naming + "\n");
        str.append("upload_include_patterns size: " + upload_include_patterns.length + "\n");

        return str.toString();
    }*/
}
