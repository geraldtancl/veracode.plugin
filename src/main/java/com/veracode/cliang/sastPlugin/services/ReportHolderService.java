package com.veracode.cliang.sastPlugin.services;

import com.veracode.cliang.sastPlugin.objects.raw.detailedReport.Detailedreport;

import java.util.Vector;

public class ReportHolderService {

    private Detailedreport scanReport = null;

    public Detailedreport getScanReport() {
        return scanReport;
    }

    public void setScanReport(Detailedreport scanReport) {
        this.scanReport = scanReport;
    }
}
