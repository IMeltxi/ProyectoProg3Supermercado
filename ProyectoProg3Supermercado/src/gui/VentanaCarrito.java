package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import db.BD;
import domain.Cliente;
import domain.Productos;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

// Asegúrate de mantener tus imports de io.* (ButtonEditor, etc.)
import io.ButtonEditor;
import io.ButtonRenderer;
import io.SpinnerEditor;
import io.TablaTooltips;

public class VentanaCarrito extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;
    private Cliente clienteLogueado;

    String[] columnas = {"Imagen","Producto", "Cantidad", "Precio Unitario", "Precio Total", "Editar"};


    public VentanaCarrito(Cliente cliente) {
    	this.clienteLogueado = cliente;
    	
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(null, columnas) {
        	@Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 5;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
            	if (column == 0) return ImageIcon.class; // Imagen
                if (column == 2 || column == 3 || column == 4) return Double.class; // valores numéricos
                return String.class;
            }

            @Override
            public void removeRow(int row) {
                if (clienteLogueado != null) {
                    try {
                        int idProd = Integer.parseInt(getValueAt(row, 0).toString());
                        BD.eliminarProductoCarrito(clienteLogueado.getidCliente(), idProd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                super.removeRow(row);
            }
        };

        modeloTabla.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2) {
                    recalcularFila(e.getFirstRow());
                }
            }
        });

        tablaCarrito = new TablaTooltips(modeloTabla);
        tablaCarrito.setRowHeight(30);

        add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        
        tablaCarrito.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tablaCarrito.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(tablaCarrito, modeloTabla));
        tablaCarrito.getColumnModel().getColumn(2).setCellEditor(new SpinnerEditor());
        tablaCarrito.setRowHeight(60);

        //cargarDatosBD();
    }
    
    /*
    private void cargarDatosBD() {
        if (clienteLogueado == null) return;
        
        modeloTabla.setRowCount(0); 
        List<Object[]> datos = BD.obtenerCarritoDelCliente(clienteLogueado.getidCliente());
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }
    */

    private void recalcularFila(int row) {
        try {
            int idProd = Integer.parseInt(modeloTabla.getValueAt(row, 0).toString());
            double cantidad = Double.parseDouble(modeloTabla.getValueAt(row, 2).toString());
            double precio = Double.parseDouble(modeloTabla.getValueAt(row, 3).toString());
            double total = cantidad * precio;
            
            if (!modeloTabla.getValueAt(row, 4).equals(total)) {
                modeloTabla.setValueAt(total, row, 4);
                BD.actualizarCantidadCarrito(clienteLogueado.getidCliente(), idProd, (int)cantidad);
            }
        } catch (Exception e) {
        }
    }
    public void agregarProducto(Productos p) {
        String ruta = p.getRutaImagen();
        System.out.println("Ruta imagen: " + ruta);

        ImageIcon icon = null;
        if (ruta != null && new File(ruta).exists()) {
            icon = new ImageIcon(new ImageIcon(ruta)
                    .getImage()
                    .getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        } else {
            System.out.println("❌ Imagen no encontrada: " + ruta);
            icon = new ImageIcon(new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB));
        }

        double cantidad = 1.0;
        double precioUnitario = p.getPrecio();
        double total = cantidad * precioUnitario;

        modeloTabla.addRow(new Object[]{
            icon,               // Columna 0 → Imagen
            p.getNombre(),      // Columna 1 → Nombre
            cantidad,           // Columna 2 → Cantidad (por defecto 1)
            precioUnitario,     // Columna 3 → Precio unitario
            total,              // Columna 4 → Precio total
            "Editar"            // Columna 5 → Botón Editar
        });

        tablaCarrito.revalidate();
        tablaCarrito.repaint();
    }
    
//    public void agregarProducto(String nombre, double precio) {
//        // 1. Buscar si el producto ya existe en la tabla
//        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
//            String nombreEnTabla = (String) modeloTabla.getValueAt(i, 0);
//            if (nombreEnTabla.equalsIgnoreCase(nombre)) {
//                // Si existe, incrementamos la cantidad
//                double cantidadActual = Double.parseDouble(modeloTabla.getValueAt(i, 1).toString());
//                modeloTabla.setValueAt(cantidadActual + 1, i, 1);
//                recalcularFila(i); // Actualizar precio total
//                return; // Salimos del método
//            }
//        }
//
//        modeloTabla.addRow(new Object[]{nombre, 1.0, precio, precio, "Eliminar"});
   
}