package com.veracode.cliang.sastPlugin.view.dialogs;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import com.intellij.openapi.wm.*;
import com.intellij.ui.components.JBRadioButton;
import com.veracode.cliang.sastPlugin.apiWrapper.ApplicationOperationWrapper;
import com.veracode.cliang.sastPlugin.apiWrapper.ResultOperationWrapper;
import com.veracode.cliang.sastPlugin.apiWrapper.SandboxOperationWrapper;
import com.veracode.cliang.sastPlugin.objects.raw.appList.AppType;
import com.veracode.cliang.sastPlugin.objects.raw.appList.Applist;
import com.veracode.cliang.sastPlugin.objects.raw.buildList.BuildType;
import com.veracode.cliang.sastPlugin.objects.raw.buildList.Buildlist;
import com.veracode.cliang.sastPlugin.objects.raw.detailedReport.Detailedreport;
import com.veracode.cliang.sastPlugin.objects.raw.sandboxList.SandboxType;
import com.veracode.cliang.sastPlugin.objects.raw.sandboxList.Sandboxlist;
import com.veracode.cliang.sastPlugin.services.ReportHolderService;
import com.veracode.cliang.sastPlugin.services.ResultToolWindowHolderService;
import com.veracode.cliang.sastPlugin.services.UserRightsHolderService;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;
import com.veracode.cliang.sastPlugin.utils.PluginLogger;
import com.veracode.cliang.sastPlugin.view.toolWindows.result.ResultToolWindowFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;


public class ResultSelectorDialog extends DialogWrapper {

    private static final Class c = ResultSelectorDialog.class;

    private static final int POLICY_SCAN_TYPE = 1;
    private static final int SANDBOX_SCAN_TYPE = 2;

    Applist appListingParentObj = null;
    Buildlist buildListingParentObj = null;
    HashMap<String, Long> applicationsMap = null;
    HashMap<String, Long> scansMap = null;
    HashMap<String, Long> sandboxesMap = null;

    private String selectedAppId = "";
    private String selectedScanId = "";
    private String selectedSandboxId = "";

    private int scanType;

    private JLabel sandboxLabel;
    private JComboBox<String> sandboxComboBox;
    private ComboBox<String> scanComboBox;

    public ResultSelectorDialog() {
        super(true); // use current window as parent
        init();
        setTitle("Choose a scan result...");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {


        JPanel waitPanel = new JPanel();
        waitPanel.setSize(0, 200);
//        waitPanel.setOpaque(true);
        JLabel label = new JLabel("Working on it right now, hold on a sec...");
        label.setFont(new Font("Serif", Font.BOLD, 16));
        label.setBackground(Color.WHITE);
        label.setForeground(Color.BLUE);
        waitPanel.add(label);
//        waitPanel.repaint();
        JBPopup workingPopup = JBPopupFactory.getInstance().createComponentPopupBuilder(waitPanel, waitPanel).setCancelOnClickOutside(false).createPopup();
        workingPopup.showInFocusCenter();


        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        // Setup the radio buttons for choosing between policy scan vs sandbox scan
        JRadioButton policyScanRadioBtn = new JBRadioButton("Policy Scan");
        policyScanRadioBtn.setActionCommand("Policy Scan");
        policyScanRadioBtn.setSelected(true);
        scanType = POLICY_SCAN_TYPE;

        JRadioButton sandboxScanRadioBtn = new JBRadioButton("Sandbox Scan");
        sandboxScanRadioBtn.setActionCommand("Sandbox Scan");

        ButtonGroup scanTypeBtnGroup = new ButtonGroup();
        scanTypeBtnGroup.add(policyScanRadioBtn);
        scanTypeBtnGroup.add(sandboxScanRadioBtn);

        JPanel scanTypePanel = new JPanel();
        scanTypePanel.add(policyScanRadioBtn);
        scanTypePanel.add(sandboxScanRadioBtn);

        dialogPanel.add(scanTypePanel);

        policyScanRadioBtn.addActionListener(e -> {
            sandboxLabel.setVisible(false);
            sandboxComboBox.setVisible(false);
            scanType = POLICY_SCAN_TYPE;
        });

        sandboxScanRadioBtn.addActionListener(e -> {
            sandboxLabel.setVisible(true);
            sandboxComboBox.setVisible(true);
            scanType = SANDBOX_SCAN_TYPE;

            refreshSandboxListing();
        });


        // Setup app selector panel
        JPanel scanResultPanel = new JPanel();
        scanResultPanel.setLayout(new GridLayout(0, 2));
        scanResultPanel.add(new JLabel("Application"));

        loadAppListingMap();
        ComboBox<String> appComboBox = new ComboBox<>();

        for (AppType app: appListingParentObj.getApp()) {
            appComboBox.addItem(app.getAppName());
        }

        scanResultPanel.add(appComboBox);

        if (appListingParentObj.getApp() != null && appListingParentObj.getApp().size() > 0) {
            selectedAppId = Long.toString(appListingParentObj.getApp().get(0).getAppId());
            loadScanListingMap(selectedAppId);
        } else { // No app profile. In this case, initialize the build listing obj to prevent error.
            buildListingParentObj = new Buildlist();
        }

        // Setup sandbox selector panel
        sandboxLabel = new JLabel("Sandbox");
        scanResultPanel.add(sandboxLabel);
        sandboxLabel.setVisible(false);

        sandboxComboBox = new ComboBox();
        scanResultPanel.add(sandboxComboBox);
        sandboxComboBox.setVisible(false);

        sandboxComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectedSandboxId = Long.toString(sandboxesMap.get(e.getItem().toString()));
                refreshScansListing();
                PluginLogger.info(c, "DEBUG: Sandbox select event -> Value selected = " + selectedSandboxId);
            }
        });

        // Setup scan selector panel
        scanResultPanel.add(new JLabel("Scan"));
        scanComboBox = new ComboBox();

        for (BuildType scan: buildListingParentObj.getBuild()) {
            scanComboBox.addItem(scan.getVersion());
        }

        scanResultPanel.add(scanComboBox);

        if (buildListingParentObj.getBuild() != null && buildListingParentObj.getBuild().size() > 0) {
            selectedScanId = Long.toString(buildListingParentObj.getBuild().get(0).getBuildId());
        }

        appComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                   // workingPopup.showInCenterOf(dialogPanel);

                    selectedAppId = Long.toString(applicationsMap.get(e.getItem().toString()));

                    if (scanType == SANDBOX_SCAN_TYPE) {
                        refreshSandboxListing();
                    } else {
                        loadScanListingMap(selectedAppId);
                        scanComboBox.removeAllItems();

                        for (BuildType scan : buildListingParentObj.getBuild()) {
                            scanComboBox.addItem(scan.getVersion());
                        }
                    }

                    PluginLogger.info(c, "DEBUG: Application combobox select event -> Value selected = " + selectedAppId);
                    //workingPopup.cancel();
                }
            }
        });

        scanComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectedScanId = Long.toString(scansMap.get(e.getItem().toString()));

                PluginLogger.info(c, "DEBUG: Scan combobox select event -> Value selected = " + selectedScanId);
            }
        });

        dialogPanel.add(scanResultPanel);

        workingPopup.cancel();

        return dialogPanel;
    }

    private void refreshSandboxListing() {
        sandboxComboBox.removeAllItems();
        selectedSandboxId = null;

        if (selectedAppId != null && selectedAppId.trim().length() > 0) {
            Sandboxlist sandboxes = SandboxOperationWrapper.getSandboxListing(selectedAppId);

            // If an app profile has no sandbox
            if (sandboxes == null) {
                sandboxes = new Sandboxlist();
            }

            sandboxesMap = new HashMap<>();

            for (SandboxType sandbox : sandboxes.getSandbox()) {
                sandboxesMap.put(sandbox.getSandboxName(), sandbox.getSandboxId());
                sandboxComboBox.addItem(sandbox.getSandboxName());
            }

            if (sandboxes.getSandbox() != null && sandboxes.getSandbox().size() > 0) {
                selectedSandboxId = Long.toString(sandboxes.getSandbox().get(0).getSandboxId());
                refreshScansListing();
            }
        }
    }

    private void refreshScansListing() {
        loadScanListingMap(selectedAppId);
        scanComboBox.removeAllItems();

        for (BuildType scan: buildListingParentObj.getBuild()) {
            scanComboBox.addItem(scan.getVersion());
        }
    }

    private void loadAppListingMap() {
        appListingParentObj = ApplicationOperationWrapper.getApplicationListing();

        // Set user rights information into the holder service
        UserRightsHolderService userRightsHolderService = ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), UserRightsHolderService.class);
        userRightsHolderService.setUserRights(appListingParentObj.getUser());

        List<AppType> applicationsList = appListingParentObj.getApp();

        applicationsList.sort(new Comparator<AppType>() {
            @Override
            public int compare(AppType o1, AppType o2) {
                PluginLogger.info(c, "Comparing: " + o1.getAppName() + ", " + o2.getAppName());

                String o1Date, o2Date;

                if (o1.getPolicyUpdatedDate() == null || o1.getPolicyUpdatedDate().trim().equals("")) {
                    o1Date = "1900-01-01";
                } else {
                    o1Date = o1.getPolicyUpdatedDate();
                }

                if (o2.getPolicyUpdatedDate() == null || o2.getPolicyUpdatedDate().trim().equals("")) {
                    o2Date = "1900-01-01";
                } else {
                    o2Date = o2.getPolicyUpdatedDate();
                }

//                if (o1.getPolicyUpdatedDate() == null || o1.getPolicyUpdatedDate().trim().equals("")) {
//                    //return 9999;
//                    return o2.getPolicyUpdatedDate().compareTo("1900-01-01");
//                } else if (o2.getPolicyUpdatedDate() == null || o2.getPolicyUpdatedDate().trim().equals("")) {
//                    //return -9999;
//                    return "1900-01-01".compareTo(o1.getPolicyUpdatedDate());
//                } else {
//                    return o2.getPolicyUpdatedDate().compareTo(o1.getPolicyUpdatedDate());
//                }
                return o2Date.compareTo(o1Date);
            }
        });

        applicationsMap = new HashMap<>();

        for (AppType app: applicationsList) {
            applicationsMap.put(app.getAppName(), app.getAppId());
        }

    }

    private void loadScanListingMap(String appId) {
        if (scanType == POLICY_SCAN_TYPE) {
            buildListingParentObj = ApplicationOperationWrapper.getScansListing(appId);
        } else {
            buildListingParentObj = ApplicationOperationWrapper.getScansListing(appId, selectedSandboxId);
        }

        List<BuildType> scanList = buildListingParentObj.getBuild();

        if (scanType == POLICY_SCAN_TYPE) {
            scanList.sort(new Comparator<BuildType>() {
                @Override
                public int compare(BuildType o1, BuildType o2) {
//                    if (o1.getPolicyUpdatedDate() == null || o1.getPolicyUpdatedDate().trim().equals("")) {
//                        return 9999;
//                    } else if (o2.getPolicyUpdatedDate() == null || o2.getPolicyUpdatedDate().trim().equals("")) {
//                        return -9999;
//                    } else {
//                        return o2.getPolicyUpdatedDate().compareTo(o1.getPolicyUpdatedDate());
//                    }
                    String o1Date, o2Date;

                    if (o1.getPolicyUpdatedDate() == null || o1.getPolicyUpdatedDate().trim().equals("")) {
                        o1Date = "1900-01-01";
                    } else {
                        o1Date = o1.getPolicyUpdatedDate();
                    }

                    if (o2.getPolicyUpdatedDate() == null || o2.getPolicyUpdatedDate().trim().equals("")) {
                        o2Date = "1900-01-01";
                    } else {
                        o2Date = o2.getPolicyUpdatedDate();
                    }

                    return o2Date.compareTo(o1Date);
                }
            });
        } else {
            scanList.sort(new Comparator<BuildType>() {
                @Override
                public int compare(BuildType o1, BuildType o2) {
                    return o1.getBuildId() < o2.getBuildId()? 1 : -1;
                }
            });
        }

        scansMap = new HashMap<>();

        for (BuildType scan: scanList) {
            scansMap.put(scan.getVersion(), scan.getBuildId());

            //PluginLogger.info(c, "Scan name: " + scan.getVersion() + ", build id: " + scan.getBuildId() + ", policy updated date: " + scan.getPolicyUpdatedDate());
        }
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action downloadAction = new DownloadAction(this);
        downloadAction.putValue(DialogWrapper.DEFAULT_ACTION, true);

        Action cancelAction = new CancelAction(this);

        Action[] actions = {downloadAction, cancelAction};

        return actions;
    }

    public String getSelectedAppId() {
        return selectedAppId;
    }

    public String getSelectedScanId() {
        return selectedScanId;
    }

    class DownloadAction extends AbstractAction {
        ResultSelectorDialog currDialog = null;

        DownloadAction(ResultSelectorDialog currDialog) {
            super("Download");
            this.currDialog = currDialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            PluginLogger.info(c, "Download button is clicked.");
            PluginLogger.info(c, "Selected Scan ID: " + currDialog.getSelectedScanId());

            Detailedreport scanReport = ResultOperationWrapper.downloadFullResult(currDialog.getSelectedScanId());

            // Set it to service
            ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), ReportHolderService.class).setScanReport(scanReport);




            // Wake result tool window up
            ToolWindow resultTW = ToolWindowManager.getInstance(JetbrainsIdeUtil.getCurrentActiveProject()).getToolWindow("Veracode SAST");
            ResultToolWindowHolderService resultTWHolderSvc = (ResultToolWindowHolderService) ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), ResultToolWindowHolderService.class);

            if (resultTWHolderSvc.getResultToolWindow() == null) {
                // This means the tool window has not been initialized yet. By calling show(), it will force the tool window content to be initialized.
//                resultTW.show(null);
                ResultToolWindowFactory.createResultToolWindow();
            } else {
                resultTWHolderSvc.getResultToolWindow().refreshResultToolWindowUI();
            }


            currDialog.close(0);
        }
    }

    class CancelAction extends AbstractAction {

        ResultSelectorDialog currDialog = null;

        CancelAction(ResultSelectorDialog currDialog) {
            super("Cancel");
            this.currDialog = currDialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currDialog.close(0);
        }
    }

}
