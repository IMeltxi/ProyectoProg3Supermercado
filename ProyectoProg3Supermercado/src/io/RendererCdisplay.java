package io;

import javax.swing.DefaultListCellRenderer;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import domain.Cliente;

public class RendererCdisplay extends DefaultListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		// TODO Auto-generated method stub
		 JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		 if (value instanceof Cliente) {
			 Cliente cliente = (Cliente) value;
			 label.setText(cliente.getNombre()+"");
			 label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14)); //tamaño de jlist
		 }
		return label;
	}
}
