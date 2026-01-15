package io;

import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.TableModel;


public class TablaTooltips extends JTable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TablaTooltips(TableModel modelo) {
		super(modelo);
	}
	
	@Override
	public String getToolTipText(MouseEvent e) {
		int fila = rowAtPoint(e.getPoint());
		int columna = columnAtPoint(e.getPoint());
		
		if (fila > -1 && columna > -1) {
			Object valor = getValueAt(fila, columna);
			if (valor != null) {
				return valor.toString();
			}
		}
		
		return null;
	}
}

