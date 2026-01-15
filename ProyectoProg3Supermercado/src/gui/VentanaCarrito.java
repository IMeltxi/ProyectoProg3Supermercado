package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import db.BD;
import domain.Cliente;
import domain.Compra;
import domain.Productos;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Asegúrate de mantener tus imports de io.* (ButtonEditor, etc.)
import io.ButtonEditor;
import io.ButtonRenderer;
import io.FacturaPDF;
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
        	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
        

        
        
     
        JPanel panelInferior = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        // 1. CheckBox para canjear puntos (100 puntos = 1 Euro)
        double valorPuntos = clienteLogueado.getPuntos() / 100.0;
        JCheckBox chkCanjear = new JCheckBox("Canjear mis " + clienteLogueado.getPuntos() + " puntos (-" + String.format("%.2f", valorPuntos) + "€)");
        
        // 2. Etiqueta informativa
        JLabel lblPuntosGanar = new JLabel("(Ganarás puntos con esta compra)");
        lblPuntosGanar.setForeground(java.awt.Color.BLUE);
        lblPuntosGanar.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // 3. Botón Comprar
        JButton btnComprar = new JButton("Comprar Ahora");
        btnComprar.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        btnComprar.setBackground(new java.awt.Color(50, 200, 50)); // Color verde
        btnComprar.setForeground(java.awt.Color.WHITE);

        // Añadimos componentes al panel (Solo mostramos el check si tiene puntos)
        if (valorPuntos > 0) {
            panelInferior.add(chkCanjear);
        }
        panelInferior.add(lblPuntosGanar);
        panelInferior.add(btnComprar);
        
        // Añadimos el panel a la ventana
        add(panelInferior, BorderLayout.SOUTH);

        
        
        btnComprar.addActionListener(e -> {
            try {
                if (modeloTabla.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "El carrito está vacío");
                    return;
                }

                // A) Calcular Total del Carrito
                double totalCarrito = 0.0;
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    totalCarrito += Double.parseDouble(modeloTabla.getValueAt(i, 4).toString());
                }

                // B) Calcular Descuento (Si marca la casilla)
                double descuento = 0.0;
                int puntosGastados = 0;

                if (chkCanjear.isSelected()) {
                    double maxDescPuntos = clienteLogueado.getPuntos() / 100.0;
                    descuento = Math.min(maxDescPuntos, totalCarrito);
                    puntosGastados = (int) (descuento * 100);
                }

                // C) Calcular Puntos NUEVOS (Usando tu lógica recursiva)
                Compra compraLogica = new Compra();
                HashMap<String, Integer> mapaProductos = new HashMap<>();
                ArrayList<Productos> lista = new ArrayList<>();
                List<Object[]> itemsParaBD = new ArrayList<>();
                
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                	int id = (int) modeloTabla.getValueAt(i, 6);
                     String nombre = modeloTabla.getValueAt(i, 1).toString();
                     int cant = ((Double) modeloTabla.getValueAt(i, 2)).intValue();
                     float prec = ((Double) modeloTabla.getValueAt(i, 3)).floatValue();
                     
                     mapaProductos.put(nombre, cant);
                     // Creamos producto temporal para la recursividad
                     lista.add(new domain.Productos(nombre, "", prec, 100, id, ""));
                     
                     itemsParaBD.add(new Object[]{id, nombre, (double)cant, (double)prec});
                }
                compraLogica.setProductos(mapaProductos);
                
                // NOTA: Recuerda que si el precio es < 10, tu recursividad devuelve 0.
                int puntosGanados = (int) compraLogica.calcularPuntos(lista);


                // D) Generar PDF (Pasando el descuento)
                File carpeta = new File("facturas");
                if (!carpeta.exists()) carpeta.mkdir();
                File pdf = new File(carpeta, "factura_" + System.currentTimeMillis() + ".pdf");
                
                // IMPORTANTE: Asegúrate de haber actualizado FacturaPDF.java como te dije en el paso anterior
                FacturaPDF.generarFactura(clienteLogueado, tablaCarrito, pdf, descuento);

                
                // E) PROCESAR COMPRA EN BASE DE DATOS (Stock, Historial y Vaciar Carrito)
                double importePagado = totalCarrito - descuento;
                boolean exitoBD = BD.procesarCompra(clienteLogueado.getidCliente(), itemsParaBD, importePagado);

                if (exitoBD) {
                    // 6. Actualizar puntos si la transacción fue exitosa
                    int saldoFinal = clienteLogueado.getPuntos() - puntosGastados + puntosGanados;
                    clienteLogueado.setPuntos(saldoFinal);
                    BD.actualizarPuntosCliente(clienteLogueado.getidCliente(), saldoFinal);

                    // 7. Mostrar resumen y limpiar interfaz
                    String mensaje = String.format("Compra realizada con éxito.\n\n" +
                                     "Total: %.2f €\n" +
                                     "PAGADO: %.2f €\n\n" +
                                     "Nuevo saldo: %d puntos", 
                                     totalCarrito, importePagado, saldoFinal);
                    
                    JOptionPane.showMessageDialog(this, mensaje, "Compra Finalizada", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Limpiar la tabla visualmente
                    modeloTabla.setRowCount(0);
                    chkCanjear.setSelected(false);
                    if (saldoFinal <= 0) chkCanjear.setVisible(false);
                    
                } else {
                    JOptionPane.showMessageDialog(this, "Hubo un error al procesar la compra en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error crítico: " + ex.getMessage());
            }

        });


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