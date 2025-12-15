package erp;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.JTable;
import java.awt.Color;

public class MarkCellEditor extends DefaultCellEditor {

    private JTextField textField;

    public MarkCellEditor() {
        super(new JTextField());
        textField = (JTextField) getComponent();

        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setFont(new java.awt.Font("Segoe UI", 0, 14));
        
        // Default border
        textField.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {

        Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
        
        textField.setBackground(Color.white);
        return c;
    }

    @Override
    public Object getCellEditorValue() {
        String txt = textField.getText().trim();

        // Numbers-only validation
        if (!txt.matches("\\d+")) {
            textField.setBackground(new Color(250, 219, 216)); // light red
            textField.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(231, 76, 60), 2));  
            return "";  
        }

        textField.setBackground(new Color(213, 244, 230)); // light green
        textField.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(39, 174, 96), 2));  

        return txt;
    }
}
