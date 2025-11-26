package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.BorderLayout;

// Asegúrate de mantener tus imports de io.* (ButtonEditor, etc.)
import io.ButtonEditor;
import io.ButtonRenderer;
import io.SpinnerEditor;
import io.TablaTooltips;

public class VentanaCarrito extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;

    String[] columnas = {"Producto", "Cantidad", "Precio Unitario", "Precio Total", "editar"};

    // Quitamos los datos hardcodeados para que empiece vacío (opcional)
    Object[][] datos = {};

    public VentanaCarrito() {
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(datos, columnas) {
            // ... (Tu código existente de getColumnClass) ...
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

        // ... (Tu código existente del listener y configuración de tabla) ...
        modeloTabla.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                // Validación para evitar bucles infinitos al actualizar desde código
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1) {
                    recalcularFila(e.getFirstRow());
                }
            }
        });

        tablaCarrito = new TablaTooltips(modeloTabla);
        tablaCarrito.setRowHeight(30);

        add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        // Configuración de renderers y editors (MANTENER TU CÓDIGO)
        tablaCarrito.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        tablaCarrito.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(tablaCarrito, modeloTabla));
        tablaCarrito.getColumnModel().getColumn(1).setCellEditor(new SpinnerEditor());
    }

    private void recalcularFila(int row) {
        try {
            double cantidad = Double.parseDouble(modeloTabla.getValueAt(row, 1).toString());
            double precio = Double.parseDouble(modeloTabla.getValueAt(row, 2).toString());
            double total = cantidad * precio;
            
            // Evitamos disparar el listener de nuevo innecesariamente
            if (!modeloTabla.getValueAt(row, 3).equals(total)) {
                modeloTabla.setValueAt(total, row, 3);
            }
        } catch (NumberFormatException e) {
            // Manejo de error si la conversión falla
        }
    }

    // --- NUEVO MÉTODO PARA AÑADIR PRODUCTOS
    public void agregarProducto(String nombre, double precio) {
        // 1. Buscar si el producto ya existe en la tabla
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String nombreEnTabla = (String) modeloTabla.getValueAt(i, 0);
            if (nombreEnTabla.equalsIgnoreCase(nombre)) {
                // Si existe, incrementamos la cantidad
                double cantidadActual = Double.parseDouble(modeloTabla.getValueAt(i, 1).toString());
                modeloTabla.setValueAt(cantidadActual + 1, i, 1);
                recalcularFila(i); // Actualizar precio total
                return; // Salimos del método
            }
        }

        modeloTabla.addRow(new Object[]{nombre, 1.0, precio, precio, "Eliminar"});
    }
}