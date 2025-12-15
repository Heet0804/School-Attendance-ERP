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
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;
public class FacultyMarksAttendance extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FacultyMarksAttendance.class.getName());
/*
     * Creates new form FacultyMarksAttendance
     */
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel model;
    public FacultyMarksAttendance() {
        initComponents();
        setLocationRelativeTo(null);
        populateGradeDropdown();
        populateDivisionDropdown();
        // Set today's date
        setTodaysDate();        
        // Initialize table
        setupTable();      
        // Add listeners for grade and division change
        addGradeDivisionListeners();       
        // Enable real-time counter updates
        addTableModelListener();
    }
    private void populateGradeDropdown() {
        cmbGrade.removeAllItems();
        cmbGrade.addItem("--Select Grade--");
        for (int i = 1; i <= 10; i++) {
            cmbGrade.addItem(String.valueOf(i));
        }
    }
    private void populateDivisionDropdown() {
        cmbDivision.removeAllItems();
        cmbDivision.addItem("--Select Division--");
        cmbDivision.addItem("A");
        cmbDivision.addItem("B");
        cmbDivision.addItem("C");
        cmbDivision.addItem("D");
    }
    private void addGradeDivisionListeners() {
        // Only grade change should load subjects
        cmbGrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadSubjectsForSelectedGrade();
            }
        });
        
        // Optional: Clear table when division changes
        cmbDivision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Clear table when division changes
                model.setRowCount(0);
                updateCounters();
            }
        });
    }
    private void loadSubjectsForSelectedGrade() {
        String grade = (String) cmbGrade.getSelectedItem();
        
        // Clear subject dropdown
        cmbSubject.removeAllItems();
        cmbSubject.addItem("--Select Subject--");
        
        // Clear table
        model.setRowCount(0);
        updateCounters();
        
        // Check if grade is selected
        if (grade.equals("--Select Grade--")) {
            return; // Don't load subjects yet
        }
        
        try {
            con = erp.DBConnection.connect();
            
            // Query to get all subjects for the selected grade from subjects table
            String query = "SELECT subject_name FROM subjects WHERE grade = ? ORDER BY subject_name";
            pst = con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(grade));
            
            rs = pst.executeQuery();
            
            // Add subjects to dropdown
            while (rs.next()) {
                String subject = rs.getString("subject_name");
                if (subject != null && !subject.trim().isEmpty()) {
                    cmbSubject.addItem(subject.trim());
                }
            }
            
            // Check if no subjects found
            if (cmbSubject.getItemCount() == 1) { // Only "--Select Subject--" exists
                JOptionPane.showMessageDialog(this, 
                    "No subjects found for Grade " + grade + "!\nPlease add subjects in the database.", 
                    "No Subjects", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
            rs.close();
            pst.close();
            con.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading subjects: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void setTodaysDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        txtDate.setText(sdf.format(new java.util.Date()));
        txtDate.setEditable(false); // Make read-only
    }
    
    // SETUP TABLE WITH STATUS DROPDOWN
    private void setupTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only Status column (column 4) is editable
                return column == 4;
            }
        };
        
        // Add columns
        model.addColumn("Student Name");
        model.addColumn("Grade");
        model.addColumn("Division");
        model.addColumn("Roll Number");
        model.addColumn("Status");
        
        tableStudents.setModel(model);
        
        // Add Status dropdown to Status column
        JComboBox<String> statusCombo = new JComboBox<>();
        statusCombo.addItem("--Select--");
        statusCombo.addItem("Present");
        statusCombo.addItem("Absent");
        
        tableStudents.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusCombo));
        
        // Apply color renderer
        tableStudents.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Get status from Status column (column 4)
                String status = (String) table.getValueAt(row, 4);
                
                if (status != null) {
                    if (status.equals("Present")) {
                        c.setBackground(new Color(144, 238, 144)); // Light green
                        c.setForeground(Color.BLACK);
                    } else if (status.equals("Absent")) {
                        c.setBackground(new Color(255, 102, 102)); // Light red
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                
                // Keep selection color visible
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                
                return c;
            }
        });
        
        // Set row height for better visibility
        tableStudents.setRowHeight(30);
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
        lblTitle = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        panelSelection = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblGrade = new javax.swing.JLabel();
        cmbGrade = new javax.swing.JComboBox<>();
        lblDivision = new javax.swing.JLabel();
        cmbDivision = new javax.swing.JComboBox<>();
        lblSubject = new javax.swing.JLabel();
        cmbSubject = new javax.swing.JComboBox<>();
        lblDate = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        panelTable = new javax.swing.JPanel();
        lblStudentAttendanceList = new javax.swing.JLabel();
        lblInfoBanner = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableStudents = new javax.swing.JTable();
        panelAction = new javax.swing.JPanel();
        lblSummary = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1200, 700));
        setMinimumSize(new java.awt.Dimension(1200, 700));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Mark Attendance");
        jPanel1.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 210, -1));

        backButton.setBackground(new java.awt.Color(255, 0, 0));
        backButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        backButton.setText("<--Back To Dashboard");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        jPanel1.add(backButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 20, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 80));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelSelection.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel1.setText("Select Class Details: ");
        panelSelection.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 6, 240, -1));

        lblGrade.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGrade.setText("Grade: ");
        panelSelection.add(lblGrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        cmbGrade.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        panelSelection.add(cmbGrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        lblDivision.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDivision.setText("Division:");
        panelSelection.add(lblDivision, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, -1, -1));

        cmbDivision.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        panelSelection.add(cmbDivision, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, -1));

        lblSubject.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSubject.setText("Subject: ");
        panelSelection.add(lblSubject, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, -1, -1));

        cmbSubject.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbSubject.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Select Subject--" }));
        cmbSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSubjectActionPerformed(evt);
            }
        });
        panelSelection.add(cmbSubject, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, -1, -1));

        lblDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDate.setText("Date:");
        panelSelection.add(lblDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 50, -1, -1));

        txtDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDate.setText("Today's Date");
        panelSelection.add(txtDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 80, -1, -1));

        jButton1.setBackground(new java.awt.Color(0, 204, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jButton1.setText("Load Students");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panelSelection.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 70, 200, 40));

        jPanel2.add(panelSelection, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 15, 1140, 130));

        panelTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelTable.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblStudentAttendanceList.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        lblStudentAttendanceList.setText("Student Attendance List: ");
        panelTable.add(lblStudentAttendanceList, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, -1, -1));

        lblInfoBanner.setBackground(new java.awt.Color(51, 204, 255));
        lblInfoBanner.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblInfoBanner.setForeground(new java.awt.Color(255, 0, 0));
        lblInfoBanner.setText("--> Note: Select Present (P) or Absent (A) for each student. Changes are color-coded.");
        panelTable.add(lblInfoBanner, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 1100, 20));

        tableStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student Name", "Grade", "Division", "Roll Number", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableStudents);

        panelTable.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 1120, 220));

        jPanel2.add(panelTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 1140, 330));

        panelAction.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSummary.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblSummary.setText("Total Students: 0 | Present: 0 | Absent: 0");
        panelAction.add(lblSummary, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        btnClear.setBackground(new java.awt.Color(153, 153, 153));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("Clear All");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        panelAction.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 15, 150, 45));

        btnSave.setBackground(new java.awt.Color(51, 204, 0));
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save Attendance");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        panelAction.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 15, 160, 45));

        jPanel2.add(panelAction, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 530, 1140, 70));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 1200, 620));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
       FacultyDashboard fd=new FacultyDashboard();
       fd.setVisible(true);
this.dispose();       
// TODO add your handling code here:
    }//GEN-LAST:event_backButtonActionPerformed

    private void cmbSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSubjectActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
String grade = (String) cmbGrade.getSelectedItem();
        String division = (String) cmbDivision.getSelectedItem();
        String subject = (String) cmbSubject.getSelectedItem();
        
        // Validation
        if (grade.equals("--Select Grade--") || division.equals("--Select Division--") || subject.equals("--Select Subject--")) {
            JOptionPane.showMessageDialog(this, "Please select Grade, Division, and Subject!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            con = erp.DBConnection.connect();
            
            // Query to get all students in selected grade and division
            String query = "SELECT name, grade, division, roll_no, email FROM users WHERE role = 'student' AND grade = ? AND division = ? ORDER BY roll_no";
            pst = con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(grade));
            pst.setString(2, division);
            
            rs = pst.executeQuery();
            
            // Clear existing rows
            model.setRowCount(0);
            
            // Add students to table
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getInt("grade"),
                    rs.getString("division"),
                    rs.getString("roll_no"),
                    "--Select--" // Default status
                });
            }
            
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No students found for Grade " + grade + " Division " + division, "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "✅ Loaded " + model.getRowCount() + " students successfully!\n\nNow mark attendance and click 'Save Attendance'.", 
                    "Students Loaded", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Update counters
            updateCounters();
            
            rs.close();
            pst.close();
            con.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
            // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
String grade = (String) cmbGrade.getSelectedItem();
        String division = (String) cmbDivision.getSelectedItem();
        String subject = (String) cmbSubject.getSelectedItem();
        String date = txtDate.getText();
        
        // Validation
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No students loaded! Click 'Load Students' first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if all statuses are selected
        for (int i = 0; i < model.getRowCount(); i++) {
            String status = (String) model.getValueAt(i, 4);
            if (status == null || status.equals("--Select--")) {
                JOptionPane.showMessageDialog(this, "Please mark attendance for all students!\n\nStudent: " + model.getValueAt(i, 0), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        try {
            con = erp.DBConnection.connect();
            
            // Check if attendance already exists for this date and subject
            String checkQuery = "SELECT COUNT(*) FROM attendance WHERE date = ? AND subject = ?";
            PreparedStatement pstCheck = con.prepareStatement(checkQuery);
            pstCheck.setString(1, date);
            pstCheck.setString(2, subject);
            ResultSet rsCheck = pstCheck.executeQuery();
            
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                int choice = JOptionPane.showConfirmDialog(this, 
                    "⚠️ Attendance for " + subject + " on " + date + " already exists!\n\nDo you want to overwrite it?", 
                    "Attendance Exists", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE);
                
                if (choice != JOptionPane.YES_OPTION) {
                    rsCheck.close();
                    pstCheck.close();
                    con.close();
                    return;
                }
                
                // Delete existing attendance for this date and subject
                String deleteQuery = "DELETE FROM attendance WHERE date = ? AND subject = ?";
                PreparedStatement pstDelete = con.prepareStatement(deleteQuery);
                pstDelete.setString(1, date);
                pstDelete.setString(2, subject);
                pstDelete.executeUpdate();
                pstDelete.close();
            }
            
            rsCheck.close();
            pstCheck.close();
            
            // Insert attendance for each student
            String insertQuery = "INSERT INTO attendance (student_email, subject, date, status, teacher_email) VALUES (?, ?, ?, ?, ?)";
            pst = con.prepareStatement(insertQuery);
            
            int savedCount = 0;
            
            for (int i = 0; i < model.getRowCount(); i++) {
                String studentName = (String) model.getValueAt(i, 0);
                String rollNumber = (String) model.getValueAt(i, 3);
                String status = (String) model.getValueAt(i, 4);
                
                // Get student email from database using roll_number, grade, and division
                String emailQuery = "SELECT email FROM users WHERE roll_no = ? AND grade = ? AND division = ?";
                PreparedStatement pstEmail = con.prepareStatement(emailQuery);
                pstEmail.setString(1, rollNumber);
                pstEmail.setInt(2, Integer.parseInt(grade));
                pstEmail.setString(3, division);
                
                ResultSet rsEmail = pstEmail.executeQuery();
                
                if (rsEmail.next()) {
                    String studentEmail = rsEmail.getString("email");
                    
                    pst.setString(1, studentEmail);
                    pst.setString(2, subject);
                    pst.setString(3, date);
                    pst.setString(4, status);
                    pst.setString(5, LoginSession.email); // Faculty email
                    
                    pst.executeUpdate();
                    savedCount++;
                }
                
                rsEmail.close();
                pstEmail.close();
            }
            
            pst.close();
            con.close();
            
            JOptionPane.showMessageDialog(this, 
                "✅ Attendance saved successfully!\n\n" +
                "Total Students: " + savedCount + "\n" +
                "Date: " + date + "\n" +
                "Subject: " + subject + "\n" +
                "Grade: " + grade + " | Division: " + division + "\n" +
                "Marked By: " + LoginSession.name, 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear table after saving
            model.setRowCount(0);
            updateCounters();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No students loaded!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to clear all attendance marks?", 
            "Clear All", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            // Reset all status to "--Select--"
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt("--Select--", i, 4);
            }
            updateCounters();
            tableStudents.repaint(); // Refresh colors
            
            JOptionPane.showMessageDialog(this, "All attendance marks cleared!", "Cleared", JOptionPane.INFORMATION_MESSAGE);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnClearActionPerformed
private void updateCounters() {
        int total = model.getRowCount();
        int present = 0;
        int absent = 0;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String status = (String) model.getValueAt(i, 4);
            if (status != null) {
                if (status.equals("Present")) {
                    present++;
                } else if (status.equals("Absent")) {
                    absent++;
                }
            }
        }
        
        lblSummary.setText("Total Students: " + total + " | Present: " + present + " | Absent: " + absent);
    }
public void addTableModelListener() {
        model.addTableModelListener(new javax.swing.event.TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent e) {
                updateCounters();
                tableStudents.repaint(); // Refresh colors when status changes
            }
        });
    }
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
        java.awt.EventQueue.invokeLater(() -> new FacultyMarksAttendance().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbDivision;
    private javax.swing.JComboBox<String> cmbGrade;
    private javax.swing.JComboBox<String> cmbSubject;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDivision;
    private javax.swing.JLabel lblGrade;
    private javax.swing.JLabel lblInfoBanner;
    private javax.swing.JLabel lblStudentAttendanceList;
    private javax.swing.JLabel lblSubject;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panelAction;
    private javax.swing.JPanel panelSelection;
    private javax.swing.JPanel panelTable;
    private javax.swing.JTable tableStudents;
    private javax.swing.JTextField txtDate;
    // End of variables declaration//GEN-END:variables
}
