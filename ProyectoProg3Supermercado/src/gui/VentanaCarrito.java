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
                    
                    if (maxDescPuntos >= totalCarrito) {
                        // Si tiene puntos de sobra, la compra sale gratis
                        descuento = totalCarrito;
                        puntosGastados = (int) (totalCarrito * 100);
                    } else {
                        // Si no, gasta todos los puntos
                        descuento = maxDescPuntos;
                        puntosGastados = clienteLogueado.getPuntos();
                    }
                }

                // C) Calcular Puntos NUEVOS (Usando tu lógica recursiva)
                domain.Compra compraLogica = new domain.Compra();
                java.util.HashMap<String, Integer> mapa = new java.util.HashMap<>();
                java.util.ArrayList<domain.Productos> lista = new java.util.ArrayList<>();
                
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                     String nombre = modeloTabla.getValueAt(i, 1).toString();
                     int cant = (int) Double.parseDouble(modeloTabla.getValueAt(i, 2).toString());
                     float prec = (float) Double.parseDouble(modeloTabla.getValueAt(i, 3).toString());
                     
                     mapa.put(nombre, cant);
                     // Creamos producto temporal para la recursividad
                     lista.add(new domain.Productos(nombre, "", prec, 100, 0, ""));
                }
                compraLogica.setProductos(mapa);
                
                // NOTA: Recuerda que si el precio es < 10, tu recursividad devuelve 0.
                int puntosGanados = (int) compraLogica.calcularPuntos(lista);


                // D) Generar PDF (Pasando el descuento)
                java.io.File carpeta = new java.io.File("facturas");
                if (!carpeta.exists()) carpeta.mkdir();
                java.io.File pdf = new java.io.File(carpeta, "factura_" + System.currentTimeMillis() + ".pdf");
                
                // IMPORTANTE: Asegúrate de haber actualizado FacturaPDF.java como te dije en el paso anterior
                io.FacturaPDF.generarFactura(clienteLogueado, tablaCarrito, pdf, descuento);


                // E) ACTUALIZAR BASE DE DATOS Y CLIENTE
                int saldoFinal = clienteLogueado.getPuntos() - puntosGastados + puntosGanados;

                // Actualizar memoria
                clienteLogueado.setPuntos(saldoFinal);
                // Actualizar BD (Asegúrate de haber puesto el método en BD.java)
                BD.actualizarPuntosCliente(clienteLogueado.getidCliente(), saldoFinal);
                
                // Mensaje resumen
                String mensaje = String.format("Compra realizada con éxito.\n\n" +
                                 "Total: %.2f €\n" +
                                 "Descuento: -%.2f €\n" +
                                 "--------------------\n" +
                                 "PAGADO: %.2f €\n\n" +
                                 "Puntos usados: %d\n" +
                                 "Puntos ganados: %d\n" +
                                 "Nuevo saldo: %d puntos", 
                                 totalCarrito, descuento, (totalCarrito - descuento), 
                                 puntosGastados, puntosGanados, saldoFinal);
                                 
                JOptionPane.showMessageDialog(this, mensaje, "Compra Finalizada", JOptionPane.INFORMATION_MESSAGE);

                // Actualizar la interfaz para la próxima compra
                double nuevoValor = saldoFinal / 100.0;
                chkCanjear.setText("Canjear mis " + saldoFinal + " puntos (-" + String.format("%.2f", nuevoValor) + "€)");
                chkCanjear.setSelected(false);
                if (saldoFinal > 0 && !chkCanjear.isShowing()) {
                    panelInferior.add(chkCanjear, 0); // Añadir el check si antes no estaba y ahora tenemos puntos
                    panelInferior.revalidate();
                }

                // Opcional: Limpiar carrito visual
                // modeloTabla.setRowCount(0);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error en la compra: " + ex.getMessage());
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