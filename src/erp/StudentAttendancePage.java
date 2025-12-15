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
import java.awt.Color;
import java.awt.Font;

public class StudentAttendancePage extends javax.swing.JFrame {
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    private JButton[] subjectButtons;
    private String currentSubject = "";
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StudentAttendancePage.class.getName());

    /**
     * Creates new form StudentAttendancePage
     */ 
    public StudentAttendancePage() {
        initComponents();
        setLocationRelativeTo(null);
        lblStudentName.setText(LoginSession.name);
        
        // Initialize subject buttons array
        subjectButtons = new JButton[11];
        
        // Load subjects dynamically
        loadSubjects();
    }
private void loadSubjects() {
    try {
        con = erp.DBConnection.connect(); // or .connect() - whichever works
        
        // Get subjects for student's grade
        String query = "SELECT subject_name FROM subjects WHERE grade = ? ORDER BY subject_name";
        pst = con.prepareStatement(query);
        pst.setInt(1, LoginSession.grade);
        
        rs = pst.executeQuery();
        
        // Store existing buttons in array
        JButton[] buttons = {
            btnSubject1, btnSubject2, btnSubject3, btnSubject4, 
            btnSubject5, btnSubject6, btnSubject7, btnSubject8, 
            btnSubject9, btnSubjec10, btnSubjec11
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
                loadAttendanceForSubject(subjectName);
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
            "Error loading subjects: " + e.getMessage() + "\n\nPlease check:\n1. Database connection\n2. Subjects exist for your grade\n3. Column name is 'subject_name'", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
private void loadAttendanceForSubject(String subject) {
        try {
            con = erp.DBConnection.connect();
            
            // Update header
            lblSubjectName.setText(subject);
            
            // Get attendance data
            String query = "SELECT COUNT(*) as total, SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) as present FROM attendance WHERE student_email = ? AND subject = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, LoginSession.email);
            pst.setString(2, subject);
            
            rs = pst.executeQuery();
            
            if (rs.next()) {
                int total = rs.getInt("total");
                int present = rs.getInt("present");
                int absent = total - present;
                
                if (total > 0) {
                    double percentage = ((double) present / total) * 100;
                    
                    // Update UI
                    lblPercentage.setText(String.format("%.0f%%", percentage));
                    lblStatus.setText(percentage >= 75 ? "Good Standing" : "Below Required");
                    lblPresent.setText("Present: " + present);
                    lblAbsent.setText("Absent: " + absent);
                    lblMinimum.setText("Minimum Required: 75%");
                    
                    // Update progress bar (if you have one)
                    progressBar.setValue((int) percentage);
                    
                    // Set color based on percentage
                    if (percentage >= 75) {
                        lblStatus.setForeground(new Color(0, 153, 0)); // Green
                    } else {
                        lblStatus.setForeground(new Color(204, 0, 0)); // Red
                    }
                    
                } else {
                    lblPercentage.setText("0%");
                    lblStatus.setText("No Data");
                    lblPresent.setText("Present: 0");
                    lblAbsent.setText("Absent: 0");
                    lblMinimum.setText("Minimum Required: 75%");
                    progressBar.setValue(0);
                }
            }
            
            rs.close();
            pst.close();
            con.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        headerPanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblStudentName = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        sidebarPanel = new javax.swing.JPanel();
        btnSubject6 = new javax.swing.JButton();
        btnSubject1 = new javax.swing.JButton();
        btnSubject2 = new javax.swing.JButton();
        btnSubject3 = new javax.swing.JButton();
        btnSubject4 = new javax.swing.JButton();
        btnSubject5 = new javax.swing.JButton();
        btnSubject7 = new javax.swing.JButton();
        btnSubject8 = new javax.swing.JButton();
        btnSubject9 = new javax.swing.JButton();
        btnSubjec10 = new javax.swing.JButton();
        btnSubjec11 = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();
        lblSubjectName = new javax.swing.JLabel();
        lblPercentage = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblBreakdownTitle = new javax.swing.JLabel();
        lblPresent = new javax.swing.JLabel();
        lblAbsent = new javax.swing.JLabel();
        lblMinimum = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 700));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerPanel.setBackground(new java.awt.Color(0, 0, 102));
        headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 3, 26)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Attendance");
        headerPanel.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 160, -1));

        lblStudentName.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblStudentName.setForeground(new java.awt.Color(255, 255, 255));
        lblStudentName.setText("Student Name");
        headerPanel.add(lblStudentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 15, -1, -1));

        btnLogout.setBackground(new java.awt.Color(255, 0, 0));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        headerPanel.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 40, 100, 30));

        getContentPane().add(headerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 70));

        sidebarPanel.setBackground(new java.awt.Color(0, 0, 0));
        sidebarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSubject6.setBackground(new java.awt.Color(153, 153, 255));
        btnSubject6.setFont(new java.awt.Font("Segoe UI", 2, 16)); // NOI18N
        btnSubject6.setText("Elective");
        btnSubject6.setBorderPainted(false);
        btnSubject6.setFocusPainted(false);
        sidebarPanel.add(btnSubject6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 230, 50));

        btnSubject1.setBackground(new java.awt.Color(153, 153, 255));
        btnSubject1.setFont(new java.awt.Font("Segoe UI", 2, 16)); // NOI18N
        btnSubject1.setText("Computer Networks");
        btnSubject1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSubject1.setBorderPainted(false);
        btnSubject1.setFocusPainted(false);
        btnSubject1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubject1ActionPerformed(evt);
            }
        });
        sidebarPanel.add(btnSubject1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 230, 50));

        btnSubject2.setBackground(new java.awt.Color(153, 153, 255));
        btnSubject2.setFont(new java.awt.Font("Segoe UI", 2, 16)); // NOI18N
        btnSubject2.setText("Web Technology");
        btnSubject2.setBorderPainted(false);
        btnSubject2.setFocusPainted(false);
        sidebarPanel.add(btnSubject2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 62, 230, 50));

        btnSubject3.setBackground(new java.awt.Color(153, 153, 255));
        btnSubject3.setFont(new java.awt.Font("Segoe UI", 2, 16)); // NOI18N
        btnSubject3.setText("DBMS");
        btnSubject3.setBorderPainted(false);
        btnSubject3.setFocusPainted(false);
        sidebarPanel.add(btnSubject3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 119, 230, 50));

        btnSubject4.setBackground(new java.awt.Color(153, 153, 255));
        btnSubject4.setFont(new java.awt.Font("Segoe UI", 2, 16)); // NOI18N
        btnSubject4.setText("Maths");
        btnSubject4.setBorderPainted(false);
        btnSubject4.setFocusPainted(false);
        sidebarPanel.add(btnSubject4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 176, 230, 50));

        btnSubject5.setBackground(new java.awt.Color(153, 153, 255));
        btnSubject5.setFont(new java.awt.Font("Segoe UI", 2, 16)); // NOI18N
        btnSubject5.setText("Python");
        btnSubject5.setBorderPainted(false);
        btnSubject5.setFocusPainted(false);
        sidebarPanel.add(btnSubject5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 233, 230, 50));

        btnSubject7.setBackground(new java.awt.Color(153, 153, 255));
        btnSubject7.setFont(new java.awt.Font("Segoe UI", 2, 16)); // NOI18N
        btnSubject7.setText("Subject 7");
        sidebarPanel.add(btnSubject7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 347, 230, 50));

        btnSubject8.setBackground(new java.awt.Color(153, 153, 255));
        btnSubject8.setFont(new java.awt.Font("Segoe UI", 2, 16)); // NOI18N
        btnSubject8.setText("Subject 8");
        sidebarPanel.add(btnSubject8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 404, 230, 50));

        btnSubject9.setBackground(new java.awt.Color(153, 153, 255));
        btnSubject9.setFont(new java.awt.Font("Segoe UI", 2, 16)); // NOI18N
        btnSubject9.setText("Subject 9");
        btnSubject9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubject9ActionPerformed(evt);
            }
        });
        sidebarPanel.add(btnSubject9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 461, 230, 50));

        btnSubjec10.setBackground(new java.awt.Color(153, 153, 255));
        btnSubjec10.setFont(new java.awt.Font("Segoe UI", 2, 16)); // NOI18N
        btnSubjec10.setText("Subject 10");
        sidebarPanel.add(btnSubjec10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 518, 230, 50));

        btnSubjec11.setBackground(new java.awt.Color(153, 153, 255));
        btnSubjec11.setFont(new java.awt.Font("Segoe UI", 2, 16)); // NOI18N
        btnSubjec11.setText("Subject 11");
        sidebarPanel.add(btnSubjec11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 575, 230, 50));

        getContentPane().add(sidebarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 250, 630));

        contentPanel.setBackground(new java.awt.Color(255, 255, 255));
        contentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSubjectName.setFont(new java.awt.Font("Segoe UI", 2, 22)); // NOI18N
        lblSubjectName.setText("Computer Networks");
        contentPanel.add(lblSubjectName, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        lblPercentage.setFont(new java.awt.Font("Segoe UI", 3, 48)); // NOI18N
        lblPercentage.setText("85%");
        contentPanel.add(lblPercentage, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        lblStatus.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        lblStatus.setText("Good Standing");
        contentPanel.add(lblStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 150, 35));

        lblBreakdownTitle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblBreakdownTitle.setText("Attendance BreakDown");
        contentPanel.add(lblBreakdownTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));

        lblPresent.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblPresent.setText("Present: 34");
        contentPanel.add(lblPresent, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        lblAbsent.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblAbsent.setText("Absent: 6");
        contentPanel.add(lblAbsent, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, -1, -1));

        lblMinimum.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        lblMinimum.setText("Minimum Required : 75%");
        contentPanel.add(lblMinimum, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, -1, -1));

        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnBack.setText("Back to Dashboard");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        contentPanel.add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 550, 200, 40));
        contentPanel.add(progressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 480, 20));

        jPanel1.setBackground(new java.awt.Color(0, 0, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        contentPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 0, 20, 630));

        jPanel2.setBackground(new java.awt.Color(0, 0, 102));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        contentPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 610, 930, 20));

        getContentPane().add(contentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 70, 950, 630));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
StudentDashboard sd=new StudentDashboard();
sd.setVisible(true);
this.dispose();/// TODO add your handling code here:
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnSubject1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubject1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSubject1ActionPerformed

    private void btnSubject9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubject9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSubject9ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new StudentAttendancePage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSubjec10;
    private javax.swing.JButton btnSubjec11;
    private javax.swing.JButton btnSubject1;
    private javax.swing.JButton btnSubject2;
    private javax.swing.JButton btnSubject3;
    private javax.swing.JButton btnSubject4;
    private javax.swing.JButton btnSubject5;
    private javax.swing.JButton btnSubject6;
    private javax.swing.JButton btnSubject7;
    private javax.swing.JButton btnSubject8;
    private javax.swing.JButton btnSubject9;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblAbsent;
    private javax.swing.JLabel lblBreakdownTitle;
    private javax.swing.JLabel lblMinimum;
    private javax.swing.JLabel lblPercentage;
    private javax.swing.JLabel lblPresent;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStudentName;
    private javax.swing.JLabel lblSubjectName;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JPanel sidebarPanel;
    // End of variables declaration//GEN-END:variables
}
