package io;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;

public class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSpinner spinner;

    public SpinnerEditor() {
        spinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 9999.0, 1.0));
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {

        spinner.setValue(value != null ? value : 0.0);
        return spinner;
    }
}
