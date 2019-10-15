package com.veracode.cliang.sastPlugin.view.toolWindows.result;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.veracode.cliang.sastPlugin.apiWrapper.CommentMitigationOperationWrapper;
import com.veracode.cliang.sastPlugin.objects.raw.appList.UserType;
import com.veracode.cliang.sastPlugin.objects.raw.detailedReport.*;
import com.veracode.cliang.sastPlugin.objects.raw.mitigationInfo.IssueType;
import com.veracode.cliang.sastPlugin.objects.raw.mitigationInfo.MitigationActionType;
import com.veracode.cliang.sastPlugin.objects.raw.mitigationInfo.Mitigationinfo;
import com.veracode.cliang.sastPlugin.services.UserRightsHolderService;
import com.veracode.cliang.sastPlugin.utils.JetbrainsIdeUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class CommentMitigationSubview extends JPanel {

    private static final String ACTION_COMMENT = "comment";
    private static final String ACTION_MITIGATE_BY_DESIGN = "appdesign";
    private static final String ACTION_MITIGATE_BY_OS = "osenv";
    private static final String ACTION_MITIGATE_BY_NETWORK = "netenv";
    private static final String ACTION_POTENTIAL_FP = "fp";
    private static final String ACTION_APPROVE_MITIGATION = "accepted";
    private static final String ACTION_REJECT_MITIGATION = "rejected";

    // mitigation_status:  One of the following values:  none, proposed, accepted, rejected
    private static final String MITIGATION_STATUS_NONE = "none";
    private static final String MITIGATION_STATUS_PROPOSED = "proposed";
    private static final String MITIGATION_STATUS_accepted = "accepted";
    private static final String MITIGATION_STATUS_REJECTED = "rejected";


    private Detailedreport detailedreport;
    private FlawType selectedFlaw;
    private List<MergedCommentMitigationObject> commentMitigationList;


    JTextArea newComment;



    public CommentMitigationSubview(Detailedreport detailedreport, FlawType selectedFlaw) {
        super();
        /*
            Documentation: Cannot use flow layout because flow layout will wrap the content.
            it seems causing the scroll pane to malfunction, middle part wrap to second row,
            right part missing.
         */
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.setBackground(Color.WHITE);

        this.selectedFlaw = selectedFlaw;
        this.detailedreport = detailedreport;

        mergedCommentAndMitigation();
        constructCommentMitigationView();
    }

    private void constructCommentMitigationView() {

        JComponent commentMitigationListingComponent = constructCommentMitigationListing();
        commentMitigationListingComponent.setMinimumSize(new Dimension(500, 0));
        commentMitigationListingComponent.setMaximumSize(new Dimension(500, Short.MAX_VALUE));

        JComponent newCommentComponent = constructNewComment();
        newCommentComponent.setMinimumSize(new Dimension(300, 0));
        newCommentComponent.setMaximumSize(new Dimension(300, Short.MAX_VALUE));

        JComponent actionListComponent = constructActionsList();

        this.add(commentMitigationListingComponent);
        this.add(Box.createRigidArea(new Dimension(20, 0)));
        this.add(newCommentComponent);
        this.add(Box.createRigidArea(new Dimension(20, 0)));
        this.add(actionListComponent);
        this.add(Box.createHorizontalGlue());


//        this.add(new Box.Filler(minSize, prefferedSize, maxSize));


    }

    private void mergedCommentAndMitigation() {
        commentMitigationList = new ArrayList<>();

        if (selectedFlaw.getAnnotations() != null) {

            for (AnnotationType comment : selectedFlaw.getAnnotations().getAnnotation()) {
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date date = new Date();
                try {
                    date = isoFormat.parse(comment.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                commentMitigationList.add(new MergedCommentMitigationObject(comment.getAction(), comment.getDescription(), comment.getUser(), date, MergedCommentMitigationObject.COMMENT));

            }
        }

        if (selectedFlaw.getMitigations() != null) {
            for (MitigationType mitigation: selectedFlaw.getMitigations().getMitigation()) {
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date date = new Date();
                try {
                    date = isoFormat.parse(mitigation.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                commentMitigationList.add(new MergedCommentMitigationObject(mitigation.getAction(), mitigation.getDescription(), mitigation.getUser(), date, MergedCommentMitigationObject.MITIGATION));
            }

        }

        // Sort according to the chronological order
        Collections.sort(commentMitigationList, (o1, o2) -> {
            if (o1.getCreatedDate().before(o2.createdDate)) {
                return -1;
            } else {
                return 1;
            }
        });
    }

    private JComponent constructCommentMitigationListing() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.PAGE_AXIS));
        //parent.setAlignmentX(0.5f);
        //parent.setAlignmentY(0.0f);
        parent.setBackground(Color.WHITE);


        for (MergedCommentMitigationObject obj: commentMitigationList) {



            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");

            StringBuilder auditTrailText = new StringBuilder();
            auditTrailText.append("On ");
            auditTrailText.append(dateFormat.format(obj.getCreatedDate()));
            auditTrailText.append(", ");
            auditTrailText.append(obj.getUser());
            auditTrailText.append(" ");
            auditTrailText.append(obj.getAction());
            auditTrailText.append(":");

            JTextArea auditTrail = new JBTextArea(auditTrailText.toString());
            auditTrail.setBackground(Color.WHITE);
            auditTrail.setLineWrap(true);
            auditTrail.setWrapStyleWord(true);
            auditTrail.setEditable(false);
            auditTrail.setFocusable(false);
            auditTrail.setFont(new Font("Verdana", Font.BOLD, auditTrail.getFont().getSize() - 1));
            auditTrail.setAlignmentX(0.0f);
            auditTrail.setMaximumSize(new Dimension(480, Short.MAX_VALUE));


//            JPanel auditTrailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//            auditTrailPanel.add(auditTrail);
//            auditTrailPanel.setAlignmentX(0.0f);

//            auditTrail.setBorder(BorderFactory.createCompoundBorder(
//                    BorderFactory.createLineBorder(Color.red),
//                    auditTrail.getBorder()));

            JTextArea comment = new JBTextArea("\"" + obj.textContent + "\"");
            comment.setBackground(Color.WHITE);
            comment.setLineWrap(true);
            comment.setWrapStyleWord(true);
            comment.setEditable(false);
            comment.setFocusable(false);
            comment.setFont(new Font("Verdana", Font.ITALIC, auditTrail.getFont().getSize()));
            comment.setAlignmentX(0.0f);
            comment.setMaximumSize(new Dimension(480, Short.MAX_VALUE));

//            JPanel commentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//            commentPanel.add(comment);
//            commentPanel.setAlignmentX(0.0f);

//            comment.setBorder(BorderFactory.createCompoundBorder(
//                    BorderFactory.createLineBorder(Color.red),
//                    comment.getBorder()));


            // Put everything together
            JPanel listItem = new JPanel();
            listItem.setLayout(new BoxLayout(listItem, BoxLayout.PAGE_AXIS));
            listItem.setBackground(Color.WHITE);
            listItem.setAlignmentX(0.0f);
            listItem.setAlignmentY(0.0f);
//            listItem.setMinimumSize(new Dimension(500, 0));
//            listItem.setMaximumSize(new Dimension(500, Short.MAX_VALUE));

            Border panelBorder = BorderFactory.createLineBorder(Color.BLUE);
            Border margin = new EmptyBorder(5, 5, 5, 5);
            Border compoundBorder = BorderFactory.createCompoundBorder(panelBorder, margin);

            listItem.setBorder(compoundBorder);
            listItem.add(Box.createRigidArea(new Dimension(0, 2)));
            listItem.add(auditTrail);
            listItem.add(Box.createRigidArea(new Dimension(0, 2)));
            listItem.add(comment);
            listItem.add(Box.createRigidArea(new Dimension(0, 2)));
            //listItem.add(Box.createVerticalGlue());

//            listItem.setBorder(BorderFactory.createCompoundBorder(
//                    BorderFactory.createLineBorder(Color.red),
//                    listItem.getBorder()));

            parent.add(listItem);
            parent.add(Box.createRigidArea(new Dimension(0, 2)));

            //System.out.println(auditTrailText.toString() + "===" + obj.textContent);
        }

        parent.add(Box.createVerticalGlue());

        // Make the parent panel scrollable
        JScrollPane scrollableParent = new JBScrollPane(parent);
        scrollableParent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollableParent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //scrollableParent.setMinimumSize(new Dimension(500, 0));


        return scrollableParent;
    }

    private JComponent constructNewComment() {

        // Setup new comment section
        JTextArea instructionLabel = new JTextArea("Key in your comment, then click on any of the action at the right.");
        instructionLabel.setBackground(Color.WHITE);
        instructionLabel.setLineWrap(true);
        instructionLabel.setWrapStyleWord(true);
        instructionLabel.setEditable(false);
        instructionLabel.setFocusable(false);
        instructionLabel.setFont(new Font("Verdana", Font.BOLD, instructionLabel.getFont().getSize()));
        instructionLabel.setAlignmentX(0.0f);
        instructionLabel.setAlignmentY(0.0f);
        instructionLabel.setMinimumSize(new Dimension(300, 50));
        instructionLabel.setPreferredSize(new Dimension(300, 50));
        instructionLabel.setMaximumSize(new Dimension(300, 50));

        newComment = new JBTextArea();
        newComment.setBackground(Color.WHITE);
        newComment.setLineWrap(true);
        newComment.setFont(new Font("Verdana", Font.PLAIN, instructionLabel.getFont().getSize()));
        //newComment.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        Border panelBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        Border margin = new EmptyBorder(10, 10, 10, 10);
        Border compoundBorder = BorderFactory.createCompoundBorder(panelBorder, margin);
        newComment.setBorder(compoundBorder);
        newComment.setAlignmentX(0.0f);
        newComment.setAlignmentY(0.0f);
        newComment.setMinimumSize(new Dimension(300, Short.MAX_VALUE));
        newComment.setPreferredSize(new Dimension(300, Short.MAX_VALUE));
        newComment.setMaximumSize(new Dimension(300, Short.MAX_VALUE));

        JPanel newCommentPanel = new JPanel();
        newCommentPanel.setLayout(new BoxLayout(newCommentPanel, BoxLayout.PAGE_AXIS));
        //parent.setAlignmentX(0.5f);
        //parent.setAlignmentY(0.0f);
        newCommentPanel.setBackground(Color.WHITE);
        newCommentPanel.add(instructionLabel);
        newCommentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        newCommentPanel.add(newComment);
        newCommentPanel.setMinimumSize(new Dimension(300, Short.MAX_VALUE));
        newCommentPanel.setPreferredSize(new Dimension(300, Short.MAX_VALUE));
        newCommentPanel.setMaximumSize(new Dimension(300, Short.MAX_VALUE));

        return newCommentPanel;

    }

    private JComponent constructActionsList() {
        UserType userRights = ServiceManager.getService(JetbrainsIdeUtil.getCurrentActiveProject(), UserRightsHolderService.class).getUserRights();

        JButton addNewCommentBtn = new JButton("Add Comment");
        addNewCommentBtn.setAlignmentX(0.0f);
        addNewCommentBtn.setPreferredSize(new Dimension(Short.MAX_VALUE, 0));
        addNewCommentBtn.addActionListener(e -> {
            submitCommentMitigation(ACTION_COMMENT);
        });

        JButton mitigateByDesignBtn = new JButton("Mitigate By Design");
        mitigateByDesignBtn.setAlignmentX(0.0f);
//        mitigateByDesignBtn.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        mitigateByDesignBtn.addActionListener(e -> {
            submitCommentMitigation(ACTION_MITIGATE_BY_DESIGN);
        });

        JButton mitigateByOsEnvBtn = new JButton("Mitigate by OS Environment");
        mitigateByOsEnvBtn.setAlignmentX(0.0f);
//        mitigateByOsEnvBtn.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        mitigateByOsEnvBtn.addActionListener(e -> {
            submitCommentMitigation(ACTION_MITIGATE_BY_OS);
        });

        JButton mitigateByNetworkEnvBtn = new JButton("Mitigate by Network Environment");
        mitigateByNetworkEnvBtn.setAlignmentX(0.0f);
//        mitigateByNetworkEnvBtn.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        mitigateByNetworkEnvBtn.addActionListener(e -> {
            submitCommentMitigation(ACTION_MITIGATE_BY_NETWORK);
        });

        JButton markPotentialFPBtn = new JButton("Mark Potential False Positive");
        markPotentialFPBtn.setAlignmentX(0.0f);
//        markPotentialFPBtn.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        markPotentialFPBtn.addActionListener(e -> {
            submitCommentMitigation(ACTION_POTENTIAL_FP);
        });

        JButton approveMitigationBtn = new JButton("Approve Mitigation");
        approveMitigationBtn.setAlignmentX(0.0f);
//        approveMitigationBtn.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        approveMitigationBtn.addActionListener(e -> {
            submitCommentMitigation(ACTION_APPROVE_MITIGATION);
        });

        JButton rejectMitigationBtn = new JButton("Reject Mitigation");
        rejectMitigationBtn.setAlignmentX(0.0f);
//        rejectMitigationBtn.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        rejectMitigationBtn.addActionListener(e -> {
            submitCommentMitigation(ACTION_REJECT_MITIGATION);
        });

        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.PAGE_AXIS));
        //parent.setAlignmentX(0.0f);
        //parent.setAlignmentY(0.0f);
        parent.setBackground(Color.WHITE);

        parent.add(Box.createRigidArea(new Dimension(0, 5)));
        parent.add(addNewCommentBtn);
        parent.add(Box.createRigidArea(new Dimension(0, 5)));

        if (selectedFlaw.getMitigationStatus().equals(MITIGATION_STATUS_NONE) ||
            selectedFlaw.getMitigationStatus().equals(MITIGATION_STATUS_REJECTED)) {
            parent.add(mitigateByDesignBtn);
            parent.add(Box.createRigidArea(new Dimension(0, 5)));
            parent.add(mitigateByOsEnvBtn);
            parent.add(Box.createRigidArea(new Dimension(0, 5)));
            parent.add(mitigateByNetworkEnvBtn);
            parent.add(Box.createRigidArea(new Dimension(0, 5)));
            parent.add(markPotentialFPBtn);
            parent.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        if (selectedFlaw.getMitigationStatus().equals((MITIGATION_STATUS_PROPOSED)) &&
            userRights.isApproveMitigations()) {
            parent.add(approveMitigationBtn);
            parent.add(Box.createRigidArea(new Dimension(0, 5)));
            parent.add(rejectMitigationBtn);
            parent.add(Box.createRigidArea(new Dimension(0, 5)));
        }

//        parent.add(Box.createVerticalGlue());

        return parent;
    }

    private void submitCommentMitigation(String actionType) {
        String comment = newComment.getText();

        if (comment.trim().length() == 0) {
            // TODO: write code for popup error dialog
        } else {
            Mitigationinfo result = CommentMitigationOperationWrapper.updateMitigation(Long.toString(detailedreport.getBuildId()), actionType, comment, selectedFlaw.getIssueid().toString());

            if (result.getError() == null || result.getError().size() == 0) {
                // Success
                // Clear data
                newComment.setText("");

                // Update the newly added comment back to the flaw object
                for (IssueType flaw: result.getIssue()) {
                    Collections.sort(flaw.getMitigationAction(), (o1, o2) -> {
                        return o1.getDate().compareTo(o2.getDate());
                    });

                    MitigationActionType newlyAddedAction = flaw.getMitigationAction().get(flaw.getMitigationAction().size() - 1);

                    // Normalize to UTC time
                    String dateString = normalizeToUTC(newlyAddedAction.getDate());

                    if (actionType.equals(ACTION_COMMENT)) {
                        AnnotationType newlyAddedAnnotation = new AnnotationType();
                        newlyAddedAnnotation.setAction(newlyAddedAction.getDesc());
                        newlyAddedAnnotation.setDate(dateString);
                        newlyAddedAnnotation.setDescription(newlyAddedAction.getComment());
                        newlyAddedAnnotation.setUser(newlyAddedAction.getReviewer());

                        if (selectedFlaw.getAnnotations() == null) {
                            selectedFlaw.setAnnotations(new AnnotationListType());
                        }

                        selectedFlaw.getAnnotations().getAnnotation().add(newlyAddedAnnotation);
                    } else {
                        MitigationType newlyAddedMitigation = new MitigationType();
                        newlyAddedMitigation.setAction(newlyAddedAction.getDesc());
                        newlyAddedMitigation.setDate(dateString);
                        newlyAddedMitigation.setDescription(newlyAddedAction.getComment());
                        newlyAddedMitigation.setUser(newlyAddedAction.getReviewer());

                        if (selectedFlaw.getMitigations() == null) {
                            selectedFlaw.setMitigations(new MitigationListType());
                        }

                        selectedFlaw.getMitigations().getMitigation().add(newlyAddedMitigation);
                    }

                    // Update the mitigation status
                    switch (actionType) {
                        case ACTION_MITIGATE_BY_DESIGN:
                        case ACTION_MITIGATE_BY_OS:
                        case ACTION_MITIGATE_BY_NETWORK:
                            selectedFlaw.setMitigationStatus(MITIGATION_STATUS_PROPOSED);
                            break;
                        case ACTION_APPROVE_MITIGATION:
                            selectedFlaw.setMitigationStatus(MITIGATION_STATUS_accepted);
                            break;
                        case ACTION_REJECT_MITIGATION:
                            selectedFlaw.setMitigationStatus(MITIGATION_STATUS_REJECTED);
                            break;
                        default:
                            break;

                    }

                }

                reconstructCommentMitigationView();

            }
        }
    }

    private String normalizeToUTC(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;

        try {
            date = sdf.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR_OF_DAY, 4);
            date = cal.getTime();
            dateString = sdf.format(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return dateString;
    }

    private void reconstructCommentMitigationView() {
        mergedCommentAndMitigation();
        this.removeAll();
        constructCommentMitigationView();
    }

    public List<MergedCommentMitigationObject> getCommentMitigationList() {
        return commentMitigationList;
    }
}

class MergedCommentMitigationObject {
    public static final int COMMENT = 1;
    public static final int MITIGATION = 2;

    String action;
    String textContent;
    String user;
    Date createdDate;
    int type;

    MergedCommentMitigationObject(String action, String textContent, String user, Date createdDate, int type) {
        this.action = action;
        this.textContent = textContent;
        this.user = user;
        this.createdDate = createdDate;
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getUser() {
        return user;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
}
