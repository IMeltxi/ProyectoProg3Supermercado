package gui;

import javax.swing.AbstractCellEditor;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import io.ButtonEditor;
import io.ButtonRenderer;
import io.SpinnerEditor;
import io.TablaTooltips;

import java.awt.BorderLayout;
import java.awt.Component;



public class VentanaCarrito extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTable tablaCarrito;
	private DefaultTableModel modeloTabla;
	
	String[] columnas = {"Producto", "Cantidad", "Precio Unitario", "Precio Total", "editar"};
	
	Object[][] datos = {
			{"Manzanas", 2, 1.50},
			{"Pan", 1, 2.00},
			{"Leche", 3, 0.80}
	};
	
	

	public VentanaCarrito() {
		setLayout(new BorderLayout());
		
		
		modeloTabla = new DefaultTableModel(datos, columnas) {
			public Class getColumnClass (int column) {
				for (int i = 1; i < columnas.length; i++) {
					if (column == i) {
						return Double.class;
					}else {
						return String.class;
					}
				}
				return null;
			}
		};
		
		modeloTabla.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();

                if (col == 1) {  
                    recalcularFila(row);
                }
            }
        });
		
		for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            recalcularFila(i);
        }
		
		
		tablaCarrito = new TablaTooltips(modeloTabla);
		tablaCarrito.setRowHeight(30);
		
		add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
		
		tablaCarrito.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
		tablaCarrito.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(tablaCarrito, modeloTabla));
		
		
		tablaCarrito.getColumnModel().getColumn(1).setCellEditor(new SpinnerEditor());

		
		
		
	}
	
	
	
	private void recalcularFila(int row) {
        
        double cantidad = Double.parseDouble(modeloTabla.getValueAt(row, 1).toString());
        double precio = Double.parseDouble(modeloTabla.getValueAt(row, 2).toString());

        double total = cantidad * precio;
        modeloTabla.setValueAt(total, row, 3);

        
    }
	
	
}
