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

    String[] columnas = {"Imagen","Producto", "Cantidad", "Precio Unitario", "Precio Total", "Editar","ID"};


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
                        int idProd = Integer.parseInt(getValueAt(row, 6).toString());
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
        
        JPanel panelInferior = new JPanel();
        
//RECURSIVIDAD - CALCULO PUNTOS
        
        // 1. Crear componentes visuales
        JButton btnCalcularPuntos = new JButton("Calcular Puntos (Recursivo)");
        JLabel lblPuntos = new JLabel("Puntos estimados: 0.0");
        lblPuntos.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Un poco de espacio

        // 2. Acción del botón
        btnCalcularPuntos.addActionListener(evt -> {
            try {
                // Instanciamos la clase Compra para usar su lógica
                domain.Compra compraLogica = new domain.Compra();
                
                // Preparamos las estructuras de datos que necesita la clase Compra
                java.util.HashMap<String, Integer> mapaProductosCompra = new java.util.HashMap<>();
                java.util.ArrayList<domain.Productos> listaDetalles = new java.util.ArrayList<>();

                // Recorremos el modelo de la tabla para extraer los datos
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    String nombre = (String) modeloTabla.getValueAt(i, 1);
                    // Aseguramos conversión de tipos (la tabla suele tener Double, la lógica pide int/float)
                    double cantDouble = Double.parseDouble(modeloTabla.getValueAt(i, 2).toString());
                    int cantidad = (int) cantDouble;
                    
                    double precioDouble = Double.parseDouble(modeloTabla.getValueAt(i, 3).toString());
                    float precio = (float) precioDouble;

                    // Llenamos el mapa (necesario para la lógica: if (this.productos.containsKey(nombre)))
                    mapaProductosCompra.put(nombre, cantidad);

                    // Creamos un objeto Producto temporal para pasar a la lista recursiva.
                    // Usamos un constructor básico o setters. Asumiendo el constructor visto en Ventanacomprarecursiva:
                    // Productos(nombre, descripcion, precio, stock, id, imagen)
                    // Ponemos datos dummy en lo que no sea nombre y precio, ya que la recursividad solo usa esos dos.
                    domain.Productos pTemp = new domain.Productos(nombre, "Desc", precio, 100, 0, ""); 
                    listaDetalles.add(pTemp);
                }

                // Pasamos el mapa a la instancia de compra
                compraLogica.setProductos(mapaProductosCompra);

                // 3. LLAMADA AL MÉTODO RECURSIVO ORIGINAL
                double puntosTotales = compraLogica.calcularPuntos(listaDetalles);

                // Actualizamos la etiqueta
                lblPuntos.setText("Puntos estimados: " + String.format("%.2f", puntosTotales));
                
                JOptionPane.showMessageDialog(this, 
                    "¡Cálculo recursivo completado!\nGanarás " + puntosTotales + " puntos con esta compra.",
                    "Fidelidad", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error calculando puntos: " + ex.getMessage());
            }
        });

        // Añadimos los nuevos elementos al panel inferior (antes que el botón comprar)
        panelInferior.add(lblPuntos);
        panelInferior.add(btnCalcularPuntos);
        
     
        
        JButton btnComprar = new JButton("Comprar");

        btnComprar.addActionListener(e -> {
            try {
                if (modeloTabla.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "El carrito está vacío");
                    return;
                }

                File carpeta = new File("facturas");
                if (!carpeta.exists()) carpeta.mkdir();

                File pdf = new File(carpeta, "factura_" + System.currentTimeMillis() + ".pdf");

                io.FacturaPDF.generarFactura(clienteLogueado, tablaCarrito, pdf);

                JOptionPane.showMessageDialog(this, "Factura generada correctamente");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error generando factura");
            }
        });

        panelInferior.add(btnComprar);
        add(panelInferior, BorderLayout.SOUTH);


        tablaCarrito = new TablaTooltips(modeloTabla);
        
        tablaCarrito.getColumnModel().getColumn(6).setMinWidth(0);
        tablaCarrito.getColumnModel().getColumn(6).setMaxWidth(0);
        tablaCarrito.getColumnModel().getColumn(6).setWidth(0);
        
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
            int idProd = Integer.parseInt(modeloTabla.getValueAt(row, 6).toString());
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
            "Editar",			// Columna 5 → Botón Editar
            p.getId()			// Columna 6 → ID (oculto)
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