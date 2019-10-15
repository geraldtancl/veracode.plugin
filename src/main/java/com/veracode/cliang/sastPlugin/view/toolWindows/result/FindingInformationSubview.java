package com.veracode.cliang.sastPlugin.view.toolWindows.result;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.veracode.cliang.sastPlugin.objects.raw.detailedReport.Detailedreport;
import com.veracode.cliang.sastPlugin.objects.raw.detailedReport.FlawType;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FindingInformationSubview extends JBTabbedPane {

    private String findingInfoTemplate;
    private FlawType selectedFlaw;
    private Detailedreport scanReport;

    Map<Integer, String> exploitLevelMap = new HashMap<Integer, String>();
    Map<Integer, String> severityMap = new HashMap<Integer, String>();

    public FindingInformationSubview(FlawType selectedFlaw, Detailedreport scanReport) {
        super();
        this.setTabPlacement(JTabbedPane.BOTTOM);
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        //this.setBackground(Color.WHITE);

        this.selectedFlaw = selectedFlaw;
        this.scanReport = scanReport;

        severityMap.put(0, "Informational");
        severityMap.put(1, "Very Low");
        severityMap.put(2, "Low");
        severityMap.put(3, "Medium");
        severityMap.put(4, "High");
        severityMap.put(5, "Very High");

        exploitLevelMap.put(-2, "Very Unlikely");
        exploitLevelMap.put(-1, "Unlikely");
        exploitLevelMap.put(0, "Not Specified");
        exploitLevelMap.put(1, "Likely");
        exploitLevelMap.put(2, "Very Likely");


        initializeFindingInfoContentTemplate();
        constructFindingInfoView();
    }

    private void initializeFindingInfoContentTemplate() {

        InputStream inputStream = FindingInformationSubview.class.getResourceAsStream("/" + "InfoHTML");

        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }

            findingInfoTemplate = resultStringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void constructFindingInfoView() {
        //JPanel parent = new JPanel();

        // Setting up tab content for description
        if (selectedFlaw == null) {
//            JPanel parent = new JPanel();
//            parent.setBackground(Color.WHITE);
//            return new JLabel("Choose a flaw first");
            return;
            //return this;
        }

        JComponent findingWriteUp = constructFindingInfoWriteUp();


        // Setting up the tab content for comment / mitigation
        CommentMitigationSubview commentMitigationSubview = new CommentMitigationSubview(scanReport, selectedFlaw);


        // Put everything into a tab
        //JTabbedPane findingInfoTabs = new JBTabbedPane(JBTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);

        this.addTab("Description", findingWriteUp);
        this.addTab("Comments / Mitigations (" + commentMitigationSubview.getCommentMitigationList().size() + ")", commentMitigationSubview);



        //parent.add(infoPane);

//        return findingInfoTabs;
    }

    private JComponent constructFindingInfoWriteUp() {
        String findingInfoContent = findingInfoTemplate;
        findingInfoContent = findingInfoContent.replace("%cweId%", selectedFlaw.getCweid().toString());
        findingInfoContent = findingInfoContent.replace("%cweDescription%", selectedFlaw.getCategoryname());
        findingInfoContent = findingInfoContent.replace("%severity%", severityMap.get(selectedFlaw.getSeverity()));
        findingInfoContent = findingInfoContent.replace("%attackVector%", selectedFlaw.getType());
        findingInfoContent = findingInfoContent.replace("%exploitability%", exploitLevelMap.get(selectedFlaw.getExploitLevel().intValue()));
        findingInfoContent = findingInfoContent.replace("%module%", selectedFlaw.getModule());
        findingInfoContent = findingInfoContent.replace("%flawStatus%", selectedFlaw.getMitigationStatus());
        findingInfoContent = findingInfoContent.replace("%location%", selectedFlaw.getSourcefile() + ":" + selectedFlaw.getLine().toString());
        findingInfoContent = findingInfoContent.replace("%description%", selectedFlaw.getDescription());
        findingInfoContent = findingInfoContent.replace("%function%", selectedFlaw.getFunctionprototype());


        JTextPane infoPane = new JTextPane();
        infoPane.setBackground(Color.WHITE);
        infoPane.setContentType("text/html");
        infoPane.setText(findingInfoContent);

        System.out.println(findingInfoContent);

        JScrollPane scrollInfoPane = new JBScrollPane(infoPane);
        scrollInfoPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollInfoPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        return scrollInfoPane;
    }
}
