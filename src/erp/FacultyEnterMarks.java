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
import java.awt.Color;
import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;
public class FacultyEnterMarks extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FacultyEnterMarks.class.getName());
Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel model;
    /**
     * Creates new form FacultyEnterMarks
     */
public FacultyEnterMarks() {
    initComponents();
    setLocationRelativeTo(null);
    // Populate dropdowns
        populateGradeDropdown();
        populateDivisionDropdown();
        populateExamTypeDropdown();
        
        // Initialize table
        setupTable();
        
        // Add listeners for grade selection
        addGradeListener();
        
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
private void populateExamTypeDropdown() {
        cmbExamType.removeAllItems();
        cmbExamType.addItem("--Select Exam Type--");
        cmbExamType.addItem("Unit 1");
        cmbExamType.addItem("Unit 2");
        cmbExamType.addItem("Term 1");
        cmbExamType.addItem("Final Exam");
    }
 private void addGradeListener() {
        cmbGrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadSubjectsForSelectedGrade();
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
            return;
        }
        
        try {
            con = erp.DBConnection.connect();
            
            // Query to get all subjects for the selected grade
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
            if (cmbSubject.getItemCount() == 1) {
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
 private void setupTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only Marks Obtained (column 4) and Total Marks (column 5) are editable
                return column == 4 || column == 5;
            }
        };
        
        // Add columns
        model.addColumn("Student Name");
        model.addColumn("Grade");
        model.addColumn("Division");
        model.addColumn("Roll Number");
        model.addColumn("Marks Obtained");
        model.addColumn("Total Marks");
        model.addColumn("Percentage");
        
        StudentMarks.setModel(model);
        
        // Set column widths
        StudentMarks.getColumnModel().getColumn(0).setPreferredWidth(150); // Name
        StudentMarks.getColumnModel().getColumn(1).setPreferredWidth(60);  // Grade
        StudentMarks.getColumnModel().getColumn(2).setPreferredWidth(70);  // Division
        StudentMarks.getColumnModel().getColumn(3).setPreferredWidth(100); // Roll Number
        StudentMarks.getColumnModel().getColumn(4).setPreferredWidth(120); // Marks Obtained
        StudentMarks.getColumnModel().getColumn(5).setPreferredWidth(100); // Total Marks
        StudentMarks.getColumnModel().getColumn(6).setPreferredWidth(100); // Percentage
        
        // Set row height
        StudentMarks.setRowHeight(30);
        
        // Center align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        StudentMarks.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        StudentMarks.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        StudentMarks.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        StudentMarks.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        StudentMarks.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        StudentMarks.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
    }
 private void updateCounters() {
        int total = model.getRowCount();
        int filled = 0;
        int pending = 0;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String marksObtained = (String) model.getValueAt(i, 4);
            String totalMarks = (String) model.getValueAt(i, 5);
            
            if (marksObtained != null && !marksObtained.trim().isEmpty() && 
                totalMarks != null && !totalMarks.trim().isEmpty()) {
                filled++;
            } else {
                pending++;
            }
        }
        
        jLabel2.setText("Total Students: " + total + " | Marks Filled: " + filled + " | Pending: " + pending);
    }
 private void calculatePercentage(int row) {
        try {
            String marksObtainedStr = (String) model.getValueAt(row, 4);
            String totalMarksStr = (String) model.getValueAt(row, 5);
            
            if (marksObtainedStr != null && !marksObtainedStr.trim().isEmpty() &&
                totalMarksStr != null && !totalMarksStr.trim().isEmpty()) {
                
                double marksObtained = Double.parseDouble(marksObtainedStr.trim());
                double totalMarks = Double.parseDouble(totalMarksStr.trim());
                
                // Validation
                if (marksObtained < 0 || totalMarks <= 0) {
                    model.setValueAt("Invalid", row, 6);
                    return;
                }
                
                if (marksObtained > totalMarks) {
                    model.setValueAt("Invalid", row, 6);
                    JOptionPane.showMessageDialog(this, 
                        "Marks obtained cannot be greater than total marks!\nStudent: " + model.getValueAt(row, 0), 
                        "Invalid Marks", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Calculate percentage
                double percentage = (marksObtained / totalMarks) * 100;
                model.setValueAt(String.format("%.2f%%", percentage), row, 6);
            } else {
                model.setValueAt("", row, 6);
            }
            
        } catch (NumberFormatException e) {
            model.setValueAt("Invalid", row, 6);
        }
    }
 public void addTableModelListener() {
        model.addTableModelListener(new javax.swing.event.TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent e) {
                if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    
                    // If marks obtained or total marks changed, recalculate percentage
                    if (column == 4 || column == 5) {
                        calculatePercentage(row);
                        updateCounters();
                    }
                }
            }
        });
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backButton = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        StudentMarks = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblGrade = new javax.swing.JLabel();
        cmbGrade = new javax.swing.JComboBox<>();
        lblDivision = new javax.swing.JLabel();
        cmbDivision = new javax.swing.JComboBox<>();
        lblSubject = new javax.swing.JLabel();
        cmbSubject = new javax.swing.JComboBox<>();
        lblExamType = new javax.swing.JLabel();
        cmbExamType = new javax.swing.JComboBox<>();
        btnLoad = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 700));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        backButton.setBackground(new java.awt.Color(0, 0, 102));
        backButton.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Enter Marks");
        backButton.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jButton1.setBackground(new java.awt.Color(255, 51, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setText("<-- Back To Dashboard");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        backButton.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 20, -1, -1));

        getContentPane().add(backButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 80));

        mainPanel.setBackground(new java.awt.Color(204, 204, 204));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel3.setText("Enter Student Marks:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("--> Note: Enter marks obtained and total marks for each student. Marks will be validated automatically.");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        StudentMarks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student Name", "Grade", "Division", "Roll Number", "Marks Obtained", "Total Marks", "Percentage"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(StudentMarks);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 1120, 220));

        mainPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 1140, 330));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel1.setText("Select Class & Exam Details");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 320, -1));

        lblGrade.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGrade.setText("Grade:");
        jPanel2.add(lblGrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        cmbGrade.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbGrade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Select Grade--", "Grade 1", "Grade 2", "Grade 3", "Grade 4", "Grade 5", "Grade 6", "Grade 7", "Grade 8", "Grade 9", "Grade 10" }));
        jPanel2.add(cmbGrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        lblDivision.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDivision.setText("Division: ");
        jPanel2.add(lblDivision, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, -1, -1));

        cmbDivision.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Select Division--", "Division A", "Division B", "Division C", "Division D" }));
        jPanel2.add(cmbDivision, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, -1, -1));

        lblSubject.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSubject.setText("Subject: ");
        jPanel2.add(lblSubject, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, -1, -1));

        cmbSubject.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbSubject.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Select Subject--" }));
        cmbSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSubjectActionPerformed(evt);
            }
        });
        jPanel2.add(cmbSubject, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 80, -1, -1));

        lblExamType.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblExamType.setText("Exam Type:");
        jPanel2.add(lblExamType, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 50, -1, -1));

        cmbExamType.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbExamType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Select Exam Type--", "Unit 1", "Unit 2", "Final Exam", "Term 1" }));
        cmbExamType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbExamTypeActionPerformed(evt);
            }
        });
        jPanel2.add(cmbExamType, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 80, -1, -1));

        btnLoad.setBackground(new java.awt.Color(102, 204, 255));
        btnLoad.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        btnLoad.setText("Load Students");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });
        jPanel2.add(btnLoad, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 70, 200, 40));

        mainPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 15, 1140, 130));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSave.setBackground(new java.awt.Color(0, 204, 0));
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save Marks");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel3.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 15, 160, 45));

        btnClear.setBackground(new java.awt.Color(153, 153, 153));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("Clear All");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        jPanel3.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 15, 150, 45));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setText("Total Students: 0 | Marks Filled: 0 | Pending: 0");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        mainPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 530, 1140, 70));

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 1200, 620));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
FacultyDashboard fd=new FacultyDashboard();
fd.setVisible(true);
this.dispose();// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmbSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSubjectActionPerformed

    private void cmbExamTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbExamTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbExamTypeActionPerformed

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
String grade = (String) cmbGrade.getSelectedItem();
        String division = (String) cmbDivision.getSelectedItem();
        String subject = (String) cmbSubject.getSelectedItem();
        String examType = (String) cmbExamType.getSelectedItem();
        
        // Validation
        if (grade.equals("--Select Grade--") || division.equals("--Select Division--") || 
            subject.equals("--Select Subject--") || examType.equals("--Select Exam Type--")) {
            JOptionPane.showMessageDialog(this, "Please select Grade, Division, Subject, and Exam Type!", "Error", JOptionPane.ERROR_MESSAGE);
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
            
            // Add students to table and check if marks already exist
            while (rs.next()) {
                String studentEmail = rs.getString("email");
                String studentName = rs.getString("name");
                int studentGrade = rs.getInt("grade");
                String studentDivision = rs.getString("division");
                String rollNumber = rs.getString("roll_no");
                
                // Check if marks already exist for this student
                String checkQuery = "SELECT marks_obtained, total_marks FROM marks WHERE student_email = ? AND subject = ? AND exam_type = ?";
                PreparedStatement pstCheck = con.prepareStatement(checkQuery);
                pstCheck.setString(1, studentEmail);
                pstCheck.setString(2, subject);
                pstCheck.setString(3, examType);
                
                ResultSet rsCheck = pstCheck.executeQuery();
                
                String marksObtained = "";
                String totalMarks = "";
                String percentage = "";
                
                if (rsCheck.next()) {
                    // Marks already exist - load them
                    marksObtained = String.valueOf(rsCheck.getInt("marks_obtained"));
                    totalMarks = String.valueOf(rsCheck.getInt("total_marks"));
                    
                    // Calculate percentage
                    double perc = (Double.parseDouble(marksObtained) / Double.parseDouble(totalMarks)) * 100;
                    percentage = String.format("%.2f%%", perc);
                }
                
                model.addRow(new Object[]{
                    studentName,
                    studentGrade,
                    studentDivision,
                    rollNumber,
                    marksObtained,  // Empty or existing marks
                    totalMarks,     // Empty or existing total
                    percentage      // Empty or calculated
                });
                
                rsCheck.close();
                pstCheck.close();
            }
            
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No students found for Grade " + grade + " Division " + division, "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "✅ Loaded " + model.getRowCount() + " students successfully!\n\nEnter marks and click 'Save Marks'.", 
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
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
String grade = (String) cmbGrade.getSelectedItem();
        String division = (String) cmbDivision.getSelectedItem();
        String subject = (String) cmbSubject.getSelectedItem();
        String examType = (String) cmbExamType.getSelectedItem();
        
        // Validation
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No students loaded! Click 'Load Students' first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if at least one student has marks entered
        boolean hasMarks = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            String marksObtained = (String) model.getValueAt(i, 4);
            String totalMarks = (String) model.getValueAt(i, 5);
            
            if (marksObtained != null && !marksObtained.trim().isEmpty() && 
                totalMarks != null && !totalMarks.trim().isEmpty()) {
                hasMarks = true;
                break;
            }
        }
        
        if (!hasMarks) {
            JOptionPane.showMessageDialog(this, "Please enter marks for at least one student!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            con = erp.DBConnection.connect();
            
            int savedCount = 0;
            int skippedCount = 0;
            int updatedCount = 0;
            
            for (int i = 0; i < model.getRowCount(); i++) {
                String marksObtainedStr = (String) model.getValueAt(i, 4);
                String totalMarksStr = (String) model.getValueAt(i, 5);
                String percentage = (String) model.getValueAt(i, 6);
                
                // Skip if marks not entered
                if (marksObtainedStr == null || marksObtainedStr.trim().isEmpty() || 
                    totalMarksStr == null || totalMarksStr.trim().isEmpty()) {
                    skippedCount++;
                    continue;
                }
                
                // Skip if percentage is Invalid
                if (percentage.equals("Invalid")) {
                    skippedCount++;
                    continue;
                }
                
                try {
                    int marksObtained = Integer.parseInt(marksObtainedStr.trim());
                    int totalMarks = Integer.parseInt(totalMarksStr.trim());
                    
                    // Validation
                    if (marksObtained < 0 || totalMarks <= 0 || marksObtained > totalMarks) {
                        skippedCount++;
                        continue;
                    }
                    
                    String rollNumber = (String) model.getValueAt(i, 3);
                    
                    // Get student email
                    String emailQuery = "SELECT email FROM users WHERE roll_no = ? AND grade = ? AND division = ?";
                    PreparedStatement pstEmail = con.prepareStatement(emailQuery);
                    pstEmail.setString(1, rollNumber);
                    pstEmail.setInt(2, Integer.parseInt(grade));
                    pstEmail.setString(3, division);
                    
                    ResultSet rsEmail = pstEmail.executeQuery();
                    
                    if (rsEmail.next()) {
                        String studentEmail = rsEmail.getString("email");
                        
                        // Check if marks already exist
                        String checkQuery = "SELECT id FROM marks WHERE student_email = ? AND subject = ? AND exam_type = ?";
                        PreparedStatement pstCheck = con.prepareStatement(checkQuery);
                        pstCheck.setString(1, studentEmail);
                        pstCheck.setString(2, subject);
                        pstCheck.setString(3, examType);
                        
                        ResultSet rsCheck = pstCheck.executeQuery();
                        
                        if (rsCheck.next()) {
                            // Update existing marks
                            String updateQuery = "UPDATE marks SET marks_obtained = ?, total_marks = ?, teacher_email = ? WHERE student_email = ? AND subject = ? AND exam_type = ?";
                            PreparedStatement pstUpdate = con.prepareStatement(updateQuery);
                            pstUpdate.setInt(1, marksObtained);
                            pstUpdate.setInt(2, totalMarks);
                            pstUpdate.setString(3, LoginSession.email);
                            pstUpdate.setString(4, studentEmail);
                            pstUpdate.setString(5, subject);
                            pstUpdate.setString(6, examType);
                            
                            pstUpdate.executeUpdate();
                            pstUpdate.close();
                            updatedCount++;
                        } else {
                            // Insert new marks
                            String insertQuery = "INSERT INTO marks (student_email, subject, exam_type, marks_obtained, total_marks, teacher_email) VALUES (?, ?, ?, ?, ?, ?)";
                            PreparedStatement pstInsert = con.prepareStatement(insertQuery);
                            pstInsert.setString(1, studentEmail);
                            pstInsert.setString(2, subject);
                            pstInsert.setString(3, examType);
                            pstInsert.setInt(4, marksObtained);
                            pstInsert.setInt(5, totalMarks);
                            pstInsert.setString(6, LoginSession.email);
                            
                            pstInsert.executeUpdate();
                            pstInsert.close();
                            savedCount++;
                        }
                        
                        rsCheck.close();
                        pstCheck.close();
                    }
                    
                    rsEmail.close();
                    pstEmail.close();
                    
                } catch (NumberFormatException e) {
                    skippedCount++;
                }
            }
            
            con.close();
            
            String message = "✅ Marks saved successfully!\n\n";
            if (savedCount > 0) message += "New marks entered: " + savedCount + "\n";
            if (updatedCount > 0) message += "Marks updated: " + updatedCount + "\n";
            if (skippedCount > 0) message += "Skipped (invalid/empty): " + skippedCount + "\n";
            message += "\nSubject: " + subject + "\nExam Type: " + examType + "\nGrade: " + grade + " | Division: " + division;
            
            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear table after saving
            model.setRowCount(0);
            updateCounters();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving marks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No students loaded!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to clear all entered marks?", 
            "Clear All", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            // Clear marks, total marks, and percentage for all rows
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt("", i, 4);  // Marks Obtained
                model.setValueAt("", i, 5);  // Total Marks
                model.setValueAt("", i, 6);  // Percentage
            }
            updateCounters();
            
            JOptionPane.showMessageDialog(this, "All marks cleared!", "Cleared", JOptionPane.INFORMATION_MESSAGE);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnClearActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FacultyEnterMarks().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable StudentMarks;
    private javax.swing.JPanel backButton;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbDivision;
    private javax.swing.JComboBox<String> cmbExamType;
    private javax.swing.JComboBox<String> cmbGrade;
    private javax.swing.JComboBox<String> cmbSubject;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDivision;
    private javax.swing.JLabel lblExamType;
    private javax.swing.JLabel lblGrade;
    private javax.swing.JLabel lblSubject;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}
