package com.veracode.cliang.sastPlugin.view.toolWindows.result;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import com.intellij.ui.treeStructure.Tree;
import com.veracode.cliang.sastPlugin.objects.raw.detailedReport.*;
import com.veracode.cliang.sastPlugin.services.ReportHolderService;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;
import com.veracode.cliang.sastPlugin.utils.PluginLogger;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class ResultToolWindow implements TreeSelectionListener {

    private static final Class c = ResultToolWindow.class;

    ToolWindow toolWindow;
    Detailedreport scanReport;
    JPanel findingListingPanel, findingInfoPanel;
    FlawType selectedFlaw;
    boolean showAffectPolicyFindingOnly = false;
    JPanel parentPanel;
    //List<DefaultMutableTreeNode> findingsNotAffectPolicy = new ArrayList<>();

    public ResultToolWindow(ToolWindow toolWindow) {
        this.toolWindow = toolWindow;

        initializeResultToolWindowUI();


    }



    private void initializeResultToolWindowUI() {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content viewBySeverityTab = contentFactory.createContent(constructBySeverityContentView(), "By Severity", false);
        toolWindow.getContentManager().addContent(viewBySeverityTab);
        toolWindow.show(null);
        //PluginLogger.info(c,"Toolwindow content size: " + toolWindow.getContentManager().getContents().length);
    }

    public void refreshResultToolWindowUI() {
        PluginLogger.info(c, "Refreshing tool window.");
        toolWindow.getContentManager().removeAllContents(true);
        PluginLogger.info(c, "Resetting done. Initializing UI.");
        initializeResultToolWindowUI();
        PluginLogger.info(c,"Toolwindow content size: " + toolWindow.getContentManager().getContents().length);
        toolWindow.show(null);
    }

    private void refreshFindingInfoAndWorkAreaUI() {
        System.out.println("Before remove");
        System.out.println(findingInfoPanel.getComponent(0).getSize());
        findingInfoPanel.removeAll();



        GridBagConstraints c = new GridBagConstraints();
        // GridBagConstraint for info panel
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        //c.insets = new Insets(10, 5, 10, 5);
        c.weightx = 1;
        c.weighty = 1;
        // GridBagConstraint for info panel

        FindingInformationSubview subview = new FindingInformationSubview(selectedFlaw, scanReport);
        findingInfoPanel.add(subview, c);

        findingInfoPanel.revalidate();
        findingInfoPanel.repaint();


        System.out.println("After adding");

        System.out.println(findingInfoPanel.getComponent(0).getSize());
        System.out.println(SwingUtilities.isEventDispatchThread());


        System.out.print(findingInfoPanel.isVisible());

//        workAreaPanel.removeAll();
//        workAreaPanel.add(constructWorkArea());
    }

    private void refreshFindingListingBySeverityTabs() {
        findingListingPanel.remove(0);
        GridBagConstraints c = new GridBagConstraints();

        // GridBagConstraint for inner component
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        //c.insets = new Insets(10, 5, 10, 5);
        c.weightx = 0.3;
        c.weighty = 0.9;
        // GridBagConstraint for inner component


        findingListingPanel.add(constructFindingsListingBySeverityTabs(), c, 0);

        findingListingPanel.revalidate();

    }

    private JComponent constructBySeverityContentView() {
        if (!isScanReportAvailable()) {
            return constructNoScanReportView();
        }



        GridBagConstraints c = new GridBagConstraints();

        Border panelBorder;
        TitledBorder titledBorder;

        // Create finding listing panel
        findingListingPanel = new JPanel(new GridBagLayout());

        /*
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(10, 10, 10, 5);
        c.weightx = 0.3;
        c.weighty = 1;
        */

        panelBorder = BorderFactory.createRaisedBevelBorder();
        titledBorder = BorderFactory.createTitledBorder(panelBorder, "Findings", TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
        findingListingPanel.setBorder(titledBorder);
        //findingListingPanel.setBackground(Color.WHITE);

        findingListingPanel.setMinimumSize(new Dimension(400, 0));
        findingListingPanel.setPreferredSize(new Dimension(400, 0));
        findingListingPanel.setMaximumSize(new Dimension(400, Short.MAX_VALUE));

        // GridBagConstraint for inner component
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        //c.insets = new Insets(10, 5, 10, 5);
        c.weightx = 0.3;
        c.weighty = 0.9;
        // GridBagConstraint for inner component


        findingListingPanel.add(constructFindingsListingBySeverityTabs(), c);

        JCheckBox showFindingsAffectedPolicyChkbox = new JBCheckBox("Findings Affected Policy Only", false);
        showFindingsAffectedPolicyChkbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setShowAffectPolicyFindingOnly(true);
                    refreshFindingListingBySeverityTabs();

                    //showFindingsANotffectPolicy(false);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    setShowAffectPolicyFindingOnly(false);
                    refreshFindingListingBySeverityTabs();

                    //showFindingsANotffectPolicy(true);
                }
            }
        });

        // GridBagConstraint for inner component
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        //c.insets = new Insets(10, 5, 10, 5);
        c.weightx = 0.3;
        c.weighty = 0.1;
        // GridBagConstraint for inner component

        findingListingPanel.add(showFindingsAffectedPolicyChkbox, c);

        // -------------------------------------

        // Setup finding information panel
        findingInfoPanel = new JPanel(new GridBagLayout());

        /*
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 4;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(10, 5, 10, 5);
        c.weightx = 0.7;
        c.weighty = 1;
        */




        panelBorder = BorderFactory.createLoweredBevelBorder();
        titledBorder = BorderFactory.createTitledBorder(panelBorder, "Descriptions", TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
        findingInfoPanel.setBorder(titledBorder);
        //findingInfoPanel.setBackground(Color.WHITE);
//        findingInfoPanel.setMinimumSize(new Dimension(1000, 0));
//        findingInfoPanel.setPreferredSize(new Dimension(1000, 0));
//        findingInfoPanel.setMaximumSize(new Dimension(1000, Short.MAX_VALUE));


        // GridBagConstraint for inner info panel
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        //c.insets = new Insets(10, 5, 10, 5);
        c.weightx = 0.7;
        c.weighty = 1;
        // GridBagConstraint for inner info panel

        findingInfoPanel.add(new FindingInformationSubview(selectedFlaw, scanReport), c);



        // Put everything together.
        //JPanel parentPanel = new JPanel(new GridBagLayout());

        parentPanel = new JPanel();
        parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.LINE_AXIS));
//        parentPanel.setBackground(Color.WHITE);
        parentPanel.setAlignmentX(0.0f);

        parentPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        parentPanel.add(findingListingPanel);
        parentPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        parentPanel.add(findingInfoPanel);

        return parentPanel;
    }

    private boolean isScanReportAvailable() {
        // When IDE starts, it seems the project listing has some delay and caused an NPE here. Check if null before getting the project service.
        //if (JetbrainsIdeUtil.getCurrentActiveProject() != null) {
            ReportHolderService reportHolderServie = ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), ReportHolderService.class);
            scanReport = reportHolderServie.getScanReport();
        //}

        return scanReport != null;
    }

    private JComponent constructNoScanReportView() {
        JPanel parent = new JPanel();
        parent.add(new JLabel("Download a scan report..."));

        return parent;
    }

    public JComponent constructFindingsListingBySeverityTabs() {
        //JPanel parentPanel = new JPanel();

//        JBTabs severityTab = new JBTabsImpl(ProjectManager.getInstance().getOpenProjects()[0]);
//        parentPanel.add(severityTab);
//        TabInfo t = new TabInfo(parentPanel);
//        t.setText("Very High");
//        t.setTabColor(Color.RED);
//        severityTab.addTab(t);

        // Setup the findings tree for very high findings
        JPanel veryHighPanel = new JPanel(new GridBagLayout());
        JPanel highPanel = new JPanel(new GridBagLayout());
        JPanel mediumPanel = new JPanel(new GridBagLayout());
        JPanel lowPanel = new JPanel(new GridBagLayout());
        JPanel veryLowPanel = new JPanel(new GridBagLayout());
        JPanel informationalPanel = new JPanel(new GridBagLayout());

        List<JPanel> jPanelList = new ArrayList<>();
        jPanelList.add(veryHighPanel);
        jPanelList.add(highPanel);
        jPanelList.add(mediumPanel);
        jPanelList.add(lowPanel);
        jPanelList.add(veryLowPanel);
        jPanelList.add(informationalPanel);

        List<JScrollPane> paneList = new ArrayList<JScrollPane>();

        for (JPanel jPanel: jPanelList) {
//            jPanel.setBackground(Color.WHITE);
            jPanel.add(new JLabel("0 finding."));

            JScrollPane scrollPane = new JBScrollPane(jPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            paneList.add(scrollPane);
        }

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        //c.insets = new Insets(10, 5, 10, 5);
        c.weightx = 1;
        c.weighty = 1;



        for (SeverityType severity: scanReport.getSeverity()) {
            Tree findingsTree = constructListingTree(severity);


            switch (severity.getLevel()) {
                case 5:
                    veryHighPanel.removeAll();
                    veryHighPanel.add(findingsTree, c);
                    break;
                case 4:
                    highPanel.removeAll();
                    highPanel.add(findingsTree, c);
                    break;
                case 3:
                    mediumPanel.removeAll();
                    mediumPanel.add(findingsTree, c);
                    break;
                case 2:
                    lowPanel.removeAll();
                    lowPanel.add(findingsTree, c);
                    break;
                case 1:
                    veryLowPanel.removeAll();
                    veryLowPanel.add(findingsTree, c);
                case 0:
                    informationalPanel.removeAll();
                    informationalPanel.add(findingsTree, c);
                    break;

            }

        }

        JTabbedPane severityTabs = new JTabbedPane(JBTabbedPane.TOP, JBTabbedPane.SCROLL_TAB_LAYOUT);
        /*
        20210726 - Fall back with JTabbedPane usage. THe Jetbrain's JBTabbedPane object in the latest version throws
        a lot of NPE in the UI. No idea why.
         */
//        JBTabbedPane severityTabs = new JBTabbedPane(JBTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        severityTabs.addTab("VH", paneList.get(0));
        severityTabs.addTab("H", paneList.get(1));
        severityTabs.addTab("M", paneList.get(2));
        severityTabs.addTab("L", paneList.get(3));
        severityTabs.addTab("VL", paneList.get(4));
        severityTabs.addTab("I", paneList.get(5));

        //parentPanel.add(severityTabs);

        return severityTabs;
    }

    private Tree constructListingTree(SeverityType severity) {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(scanReport);
        DefaultMutableTreeNode catNode, findingNode;

        // Category level - such as SQL Injection. Each category may have multiple CWE.
        for (CategoryType vulCategory: severity.getCategory()) {
            catNode = new DefaultMutableTreeNode(vulCategory);
            root.add(catNode);


            for (CweType cwe: vulCategory.getCwe()) {
                for (FlawType finding: cwe.getStaticflaws().getFlaw()) {
                    if (isShowAffectPolicyFindingOnly()) {
                        if (!finding.isAffectsPolicyCompliance()) {
                            continue;
                        }
                    }
                    findingNode = new DefaultMutableTreeNode(finding);
                    catNode.add(findingNode);

                }
            }
        }

        Tree severityTree = new Tree(root);
        //severityTree.setCellRenderer(new FindingTreeCellRenderer(this));
        severityTree.setRootVisible(false);
        //TreeSpeedSearch speedSearch = new TreeSpeedSearch(severityTree);

        severityTree.addTreeSelectionListener(this);


        return severityTree;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        //DefaultMutableTreeNode lastSelectedNode = (DefaultMutableTreeNode) severityTree.getLastSelectedPathComponent();
        DefaultMutableTreeNode lastSelectedNode = (DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();

        if (lastSelectedNode.getUserObject() instanceof FlawType) {
            FlawType finding = (FlawType) lastSelectedNode.getUserObject();
            selectedFlaw = finding;

            refreshFindingInfoAndWorkAreaUI();
            findOpenAndHighlightProgram(finding.getSourcefile(), finding.getLine().intValue());

        }

    }

    private void findOpenAndHighlightProgram(String sourceFileName, int lineNumber) {


        //Project[] projects = ProjectManager.getInstance().getOpenProjects();

        PsiFile[] results = FilenameIndex.getFilesByName(JetbrainsIdeUtil.getCurrentActiveProject(), sourceFileName, GlobalSearchScope.projectScope(JetbrainsIdeUtil.getCurrentActiveProject()));
        PluginLogger.info(c, "Search result: " + results.length);

        if (results.length == 1) {
            // Single result
            openAndHighlight(results[0], lineNumber);

        } else if (results.length == 0) {
            // No respective program found, do nothing.
            System.out.print("No file found, do nothing.");
            new FileNotFoundDialog().showAndGet();
        } else if (results.length > 1) {
            // More than one result found

            /*
                First scenario, the file could come from build. If the project is created from IntelliJ,
                build directory should have been marked "excluded". Below logic is to exclude the file found in
                build directory.
             */
            PsiFile[] _results = results.clone();
            List<PsiFile>  _resultsList = new ArrayList<>();

            for (PsiFile file: _results) {
                if (ProjectFileIndex.getInstance(JetbrainsIdeUtil.getCurrentActiveProject()).isExcluded(file.getVirtualFile())) {

                } else {
                    _resultsList.add(file);
                }
            }

            if (_resultsList.size() == 1) {
                openAndHighlight(_resultsList.get(0), lineNumber);

                return;
            }

            /*
                Second scenario, we are only interested in the file create in source tree.
                If the project is originated from IntelliJ, it should already have the source marked as
                "Sources Root".
             */
            _results = results.clone();
            _resultsList = new ArrayList<>();

            for (PsiFile file: _results) {
                if (ProjectFileIndex.getInstance(JetbrainsIdeUtil.getCurrentActiveProject()).isInSource(file.getVirtualFile())) {
                    _resultsList.add(file);
                }
            }

            if (_resultsList.size() == 1) {
                openAndHighlight(_resultsList.get(0), lineNumber);

                return;
            }

            /*
                Third scenario, it has multiple source with same filename, but in different package.
             */
            _results = results.clone();
            _resultsList = new ArrayList<>();

            String sourcePath = selectedFlaw.getSourcefilepath();

            sourcePath = sourcePath.replace("/", File.separator);
            sourcePath = sourcePath.replace("\\", File.separator);

            PluginLogger.info(c, File.separator);
            PluginLogger.info(c, "Source Path = " + sourcePath);


            for (PsiFile file: _results) {
                if (file.getVirtualFile().getPath().contains(sourcePath)) {
                    PluginLogger.info(c, "Virtual file path = " + file.getVirtualFile().getPath());
                    _resultsList.add(file);
                }
            }

            if (_resultsList.size() == 1) {
                openAndHighlight(_resultsList.get(0), lineNumber);

                return;
            }


        }
    }

    private void openAndHighlight(PsiFile file, int lineNumber) {
//        Project[] projects = ProjectManager.getInstance().getOpenProjects();

        OpenFileDescriptor ofd = new OpenFileDescriptor(JetbrainsIdeUtil.getCurrentActiveProject(), file.getVirtualFile(), lineNumber - 1, 0);
        ofd.navigate(true);

        Editor editor = FileEditorManager.getInstance(JetbrainsIdeUtil.getCurrentActiveProject()).getSelectedTextEditor();
        TextAttributes highlighterStyle = new TextAttributes();
        highlighterStyle.setBackgroundColor(Color.PINK);

        editor.getMarkupModel().removeAllHighlighters();
        editor.getMarkupModel().addLineHighlighter(lineNumber - 1, HighlighterLayer.ERROR - 1, highlighterStyle);
    }



    public void setShowAffectPolicyFindingOnly(boolean showAffectPolicyFindingOnly) {
        this.showAffectPolicyFindingOnly = showAffectPolicyFindingOnly;
    }

    public boolean isShowAffectPolicyFindingOnly() {
        return showAffectPolicyFindingOnly;
    }


}

class FindingTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final Class c = FindingTreeCellRenderer.class;

    ResultToolWindow toolWindow;

    public FindingTreeCellRenderer(ResultToolWindow toolWindow) {
        super();
        this.toolWindow = toolWindow;

    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        PluginLogger.info(c, "Here for : " + value.toString() + ". Object class: " + value.getClass());

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        if (node.getUserObject() instanceof FlawType) {
            PluginLogger.info(c, ". Show affect policy finding only? " + toolWindow.isShowAffectPolicyFindingOnly());
            if (toolWindow.isShowAffectPolicyFindingOnly()) {
                FlawType finding = (FlawType) node.getUserObject();

                if (!finding.isAffectsPolicyCompliance()) {
                    Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                    comp.setVisible(false);
                    comp.setEnabled(false);

                    return comp;
                }
            }
        }


        return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    }
}

class FileNotFoundDialog extends DialogWrapper {

    FileNotFoundDialog() {
        super(JetbrainsIdeUtil.getCurrentActiveProject());
        init();
        setTitle("File Not Found in Project...");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return new JLabel("No program found in the project. Please check if this file belongs to another project.");
    }
}



/*
SAMPLE CODE
       Project project = anActionEvent.getProject();
        File file = new File("/home/namarasiri/IdeaProjects/TestPlugin/src/com/test/Test.java");
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, virtualFile);
        openFileDescriptor.navigate(true);

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        int x =  editor.getDocument().getLineCount();
        editor.getMarkupModel().addLineHighlighter(1, 20, null);//addRangeHighlighter(1, 20, (HighlighterLayer.SELECTION - 100), new TextAttributes(), HighlighterTargetArea.EXACT_RANGE);
 */