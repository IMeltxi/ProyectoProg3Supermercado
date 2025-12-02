package io;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

    private JButton button;
    private JTable table;

    public ButtonEditor(JTable table, DefaultTableModel model) {
        this.table = table;

        button = new JButton("Eliminar");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	fireEditingStopped();
            	
                int row = table.getSelectedRow();
                if (row != -1) {
                    model.removeRow(row); 
                }
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return "Eliminar";
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return button;
    }
}

