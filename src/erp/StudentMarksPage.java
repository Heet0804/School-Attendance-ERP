/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package erp;

/**
 *
 * @author Heet Lakhani
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
public class StudentMarksPage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StudentMarksPage.class.getName());
 Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel model;
    private String currentSubject = "";
    /**
     * Creates new form StudentMarksPage
     */
    private int studentGrade;
   public StudentMarksPage() {
    initComponents();
    setLocationRelativeTo(null);
        setupTable();
        loadSubjects();
        populateExamDropdown();
}
private void setupTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only
            }
        };
        
        model.addColumn("Components");
        model.addColumn("Obtained");
        model.addColumn("Total");
        
        tableStudents.setModel(model);
        
        // Set column widths
        tableStudents.getColumnModel().getColumn(0).setPreferredWidth(400);
        tableStudents.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableStudents.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        tableStudents.setRowHeight(30);
    }
private void populateExamDropdown() {
        cmbExam.removeAllItems();
        cmbExam.addItem("Unit 1");
        cmbExam.addItem("Unit 2");
        cmbExam.addItem("Term 1");
        cmbExam.addItem("Final Exam");
        
        // Add listener to reload marks when exam changes
        cmbExam.addActionListener(evt -> {
            if (!currentSubject.isEmpty()) {
                loadMarksForSubject(currentSubject);
            }
        });
    }
private void loadSubjects() {
        try {
            con = erp.DBConnection.connect();
            
            // Get subjects for student's grade
            String query = "SELECT subject_name FROM subjects WHERE grade = ? ORDER BY subject_name";
            pst = con.prepareStatement(query);
            pst.setInt(1, LoginSession.grade);
            
            rs = pst.executeQuery();
            
            // Store existing buttons in array
            JButton[] buttons = {
                subject1, subject2, subject3, subject4, 
                subject5, subject6, subject7, subject8, 
                subject9, subject10, subject11
            };
            
            int buttonIndex = 0;
            
            // Assign subject names to buttons
            while (rs.next() && buttonIndex < 11) {
                final String subjectName = rs.getString("subject_name");
                
                // Set button text
                buttons[buttonIndex].setText(subjectName);
                buttons[buttonIndex].setVisible(true);
                
                // Remove old action listeners
                for (java.awt.event.ActionListener al : buttons[buttonIndex].getActionListeners()) {
                    buttons[buttonIndex].removeActionListener(al);
                }
                
                // Add new action listener
                buttons[buttonIndex].addActionListener(evt -> {
                    currentSubject = subjectName;
                    loadMarksForSubject(subjectName);
                });
                
                buttonIndex++;
            }
            
            // Hide unused buttons
            for (int i = buttonIndex; i < 11; i++) {
                buttons[i].setVisible(false);
            }
            
            if (buttonIndex == 0) {
                JOptionPane.showMessageDialog(this, 
                    "No subjects found for Grade " + LoginSession.grade, 
                    "No Subjects", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
            rs.close();
            pst.close();
            con.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading subjects: " + e.getMessage() + "\n\nPlease check:\n1. Database connection\n2. Subjects exist for your grade", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
private void loadMarksForSubject(String subject) {
        try {
            con = erp.DBConnection.connect();
            
            // Get selected exam type
            String examType = (String) cmbExam.getSelectedItem();
            
            if (examType == null || examType.isEmpty()) {
                return;
            }
            
            // Update header
            lblSubjectTitle.setText(subject + " - " + examType);
            
            // Get marks data for selected exam type
            String query = "SELECT exam_type, marks_obtained, total_marks FROM marks WHERE student_email = ? AND subject = ? AND exam_type = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, LoginSession.email);
            pst.setString(2, subject);
            pst.setString(3, examType);
            
            rs = pst.executeQuery();
            
            // Clear table
            model.setRowCount(0);
            
            int totalObtained = 0;
            int totalMarks = 0;
            boolean hasMarks = false;
            
            if (rs.next()) {
                hasMarks = true;
                String exam = rs.getString("exam_type");
                int obtained = rs.getInt("marks_obtained");
                int total = rs.getInt("total_marks");
                
                model.addRow(new Object[]{exam, obtained, total});
                
                totalObtained = obtained;
                totalMarks = total;
            }
            
            if (!hasMarks) {
                model.addRow(new Object[]{"No marks available", "-", "-"});
                lblTotalMarks.setText("Total Obtained: -");
                lblTotalObtained.setText("Total Marks: -");
                lblPercentage.setText("Percentage: -");
                lblStatus.setText("Status: No Data");
            } else {
                // Calculate percentage
                double percentage = ((double) totalObtained / totalMarks) * 100;
                String status = "";
                
                if (percentage >= 90) status = "Excellent";
                else if (percentage >= 75) status = "Good";
                else if (percentage >= 60) status = "Average";
                else if (percentage >= 40) status = "Pass";
                else status = "Fail";
                
                // Update summary labels
                lblTotalMarks.setText("Total Obtained: " + totalObtained);
                lblTotalObtained.setText("Total Marks: " + totalMarks);
                lblPercentage.setText("Percentage: " + String.format("%.2f%%", percentage));
                lblStatus.setText("Status: " + status);
                
                // Set status color
                if (percentage >= 75) {
                    lblStatus.setForeground(new Color(0, 153, 0)); // Green
                } else if (percentage >= 40) {
                    lblStatus.setForeground(new Color(255, 140, 0)); // Orange
                } else {
                    lblStatus.setForeground(new Color(204, 0, 0)); // Red
                }
            }
            
            rs.close();
            pst.close();
            con.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading marks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        marksoverview = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        sidebarPanel = new javax.swing.JPanel();
        subject9 = new javax.swing.JButton();
        subject1 = new javax.swing.JButton();
        subject2 = new javax.swing.JButton();
        subject3 = new javax.swing.JButton();
        subject4 = new javax.swing.JButton();
        subject5 = new javax.swing.JButton();
        subject6 = new javax.swing.JButton();
        subject7 = new javax.swing.JButton();
        subject10 = new javax.swing.JButton();
        subject8 = new javax.swing.JButton();
        subject11 = new javax.swing.JButton();
        panelMarks = new javax.swing.JPanel();
        lblSubjectTitle = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbExam = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableStudents = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        lblTotalObtained = new javax.swing.JLabel();
        lblTotalMarks = new javax.swing.JLabel();
        lblPercentage = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1200, 700));
        setMinimumSize(new java.awt.Dimension(1200, 700));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        marksoverview.setFont(new java.awt.Font("Segoe UI", 2, 28)); // NOI18N
        marksoverview.setForeground(new java.awt.Color(255, 255, 255));
        marksoverview.setText("Marks Overview");
        jPanel1.add(marksoverview, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jButton1.setBackground(new java.awt.Color(255, 51, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 31, 100, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 80));

        sidebarPanel.setBackground(new java.awt.Color(0, 0, 0));
        sidebarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        subject9.setBackground(new java.awt.Color(153, 153, 255));
        subject9.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        subject9.setText("Subject 9");
        sidebarPanel.add(subject9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 461, 230, 50));

        subject1.setBackground(new java.awt.Color(153, 153, 255));
        subject1.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        subject1.setText("Subject 1");
        sidebarPanel.add(subject1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 230, 50));

        subject2.setBackground(new java.awt.Color(153, 153, 255));
        subject2.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        subject2.setText("Subject 2");
        sidebarPanel.add(subject2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 62, 230, 50));

        subject3.setBackground(new java.awt.Color(153, 153, 255));
        subject3.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        subject3.setText("Subject 3");
        sidebarPanel.add(subject3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 119, 230, 50));

        subject4.setBackground(new java.awt.Color(153, 153, 255));
        subject4.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        subject4.setText("Subject 4");
        sidebarPanel.add(subject4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 176, 230, 50));

        subject5.setBackground(new java.awt.Color(153, 153, 255));
        subject5.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        subject5.setText("Subject 5");
        sidebarPanel.add(subject5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 233, 230, 50));

        subject6.setBackground(new java.awt.Color(153, 153, 255));
        subject6.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        subject6.setText("Subject 6");
        sidebarPanel.add(subject6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 230, 50));

        subject7.setBackground(new java.awt.Color(153, 153, 255));
        subject7.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        subject7.setText("Subject 7");
        sidebarPanel.add(subject7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 347, 230, 50));

        subject10.setBackground(new java.awt.Color(153, 153, 255));
        subject10.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        subject10.setText("Subject 10");
        sidebarPanel.add(subject10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 518, 230, 50));

        subject8.setBackground(new java.awt.Color(153, 153, 255));
        subject8.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        subject8.setText("Subject 8");
        sidebarPanel.add(subject8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 404, 230, 50));

        subject11.setBackground(new java.awt.Color(153, 153, 255));
        subject11.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        subject11.setText("Subject 11");
        sidebarPanel.add(subject11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 575, 230, 40));

        getContentPane().add(sidebarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 250, 620));

        panelMarks.setBackground(new java.awt.Color(255, 255, 255));
        panelMarks.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSubjectTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblSubjectTitle.setText("Select a Subject: ");
        panelMarks.add(lblSubjectTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 300, 40));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel2.setText("Exam:");
        panelMarks.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 80, 30));

        cmbExam.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbExam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Unit Test 1", "Unit Test 2", "Final Exam", "Term 1" }));
        panelMarks.add(cmbExam, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 200, 35));

        tableStudents.setBackground(new java.awt.Color(204, 204, 204));
        tableStudents.setFont(new java.awt.Font("Segoe UI", 2, 16)); // NOI18N
        tableStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Components", "Obtained", "Total"
            }
        ));
        tableStudents.setRowHeight(35);
        jScrollPane1.setViewportView(tableStudents);

        panelMarks.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 880, 350));

        jPanel3.setBackground(new java.awt.Color(153, 153, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalObtained.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalObtained.setText("Total Obtained: ");
        jPanel3.add(lblTotalObtained, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        lblTotalMarks.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalMarks.setText("Total Marks: ");
        jPanel3.add(lblTotalMarks, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, -1, -1));

        lblPercentage.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblPercentage.setText("Percentage: ");
        jPanel3.add(lblPercentage, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblStatus.setText("Status: ");
        jPanel3.add(lblStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, -1, -1));

        panelMarks.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, 880, 120));

        jPanel2.setBackground(new java.awt.Color(0, 0, 102));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelMarks.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 0, 20, 620));

        getContentPane().add(panelMarks, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 950, 620));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
StudentDashboard sd=new StudentDashboard();
sd.setVisible(true);
this.dispose();      // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new StudentMarksPage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbExam;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPercentage;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSubjectTitle;
    private javax.swing.JLabel lblTotalMarks;
    private javax.swing.JLabel lblTotalObtained;
    private javax.swing.JLabel marksoverview;
    private javax.swing.JPanel panelMarks;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JButton subject1;
    private javax.swing.JButton subject10;
    private javax.swing.JButton subject11;
    private javax.swing.JButton subject2;
    private javax.swing.JButton subject3;
    private javax.swing.JButton subject4;
    private javax.swing.JButton subject5;
    private javax.swing.JButton subject6;
    private javax.swing.JButton subject7;
    private javax.swing.JButton subject8;
    private javax.swing.JButton subject9;
    private javax.swing.JTable tableStudents;
    // End of variables declaration//GEN-END:variables
}
