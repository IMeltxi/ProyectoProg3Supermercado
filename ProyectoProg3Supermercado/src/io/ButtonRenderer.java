package io;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
    	 //setIcon(new ImageIcon(getClass().getResource("fotos_png/Papelera.png")));
         //setBorder(null);
         //setFocusPainted(false);
         //setContentAreaFilled(false);
    	setText("Eliminar");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        return this;
    }
}
