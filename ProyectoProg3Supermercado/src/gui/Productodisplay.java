package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import db.BD;
import domain.Productos;

public class Productodisplay extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] headers = { "ID","NOMBRE","PRECIO", "STOCK","DESCRIPCION", };
    private JTable tabla;
    private DefaultTableModel tablaModelo;
    private JPanel pSur;
    private Button Eliminar, Editar, Guardar, Nueva_Producto;
   
    
    public Productodisplay() {
    	
    	setSize(400, 400);
        setLayout(new BorderLayout());
        
        //pCentro = new JPanel(new BorderLayout());
        pSur =new JPanel(new BorderLayout());
        
        this.add(pSur, BorderLayout.SOUTH);
       
        //List<Producto> listaproductos = db.obtenerProductos();
        
       
        //opciones de usuario
        Guardar = new Button("Guardar");
        Guardar.setBackground(Color.BLUE.darker());
        Guardar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        Guardar.setForeground(Color.white);
        Guardar.setPreferredSize(new Dimension(150, 35));
        
        Editar = new Button("Editar");
        Editar.setBackground(Color.BLUE.darker());
        Editar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        Editar.setForeground(Color.white);
        Editar.setPreferredSize(new Dimension(150, 35));
        
        Eliminar = new Button("Eliminar");
        Eliminar.setBackground(Color.BLUE.darker());
        Eliminar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        Eliminar.setForeground(Color.white);
        Eliminar.setPreferredSize(new Dimension(150, 35));
        
        tablaModelo = new DefaultTableModel(headers, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no serán editables
            }
        };
        cargarDatosEnTabla();
        tabla = new JTable(tablaModelo);
        tabla.setRowHeight(30);
        tabla.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        tabla.getTableHeader().setReorderingAllowed(false); 
       
        JScrollPane scrollPane = new JScrollPane(tabla);
        this.add(scrollPane, BorderLayout.CENTER);
        
        //opciones de usuario
        Guardar = new Button("Guardar");
        Guardar.setBackground(Color.BLUE.darker());
        Guardar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        Guardar.setForeground(Color.white);
        Guardar.setPreferredSize(new Dimension(150, 35));
        
        Editar = new Button("Editar");
        Editar.setBackground(Color.BLUE.darker());
        Editar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        Editar.setForeground(Color.white);
        Editar.setPreferredSize(new Dimension(150, 35));
        
        Eliminar = new Button("Eliminar");
        Eliminar.setBackground(Color.BLUE.darker());
        Eliminar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        Eliminar.setForeground(Color.white);
        Eliminar.setPreferredSize(new Dimension(150, 35));
        
        Nueva_Producto = new Button("Nuevo Producto");
        Nueva_Producto.setBackground(Color.BLUE.darker());
        Nueva_Producto.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        Nueva_Producto.setForeground(Color.white);
        Nueva_Producto.setPreferredSize(new Dimension(150, 35));
        
        JPanel botonesPanel = new JPanel();
        
        //botonesPanel.add(Guardar);
        botonesPanel.add(Editar);
        botonesPanel.add(Eliminar);
        botonesPanel.add(Nueva_Producto);
        
        pSur.add(botonesPanel);
        
        Nueva_Producto.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				mostrarDialogoProducto(null);
		
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
       
        Editar.addActionListener(new ActionListener() {
			
        	boolean editable = false;
        	
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				editable = !editable; 
				
				tablaModelo = new DefaultTableModel(headers, 0) {
		            /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
		            public boolean isCellEditable(int row, int column) {
		                return editable; // 
		            }
		        };
		        cargarDatosEnTabla(); // Refrescar datos con la nueva configuración
		        tabla.setModel(tablaModelo);
		        
		        
			}
			});
        
        Eliminar.addActionListener(new ActionListener() {

        	@Override
        	public void actionPerformed(ActionEvent e) {
        	    int filaSeleccionada = tabla.getSelectedRow();

        	    if (filaSeleccionada >= 0) {

        	        int idContenido = (int) tabla.getValueAt(filaSeleccionada, 0);

        	        int confirmacion = JOptionPane.showConfirmDialog(null,
        	                "¿Está seguro de que desea eliminar este contenido?",
        	                "Confirmar Eliminación",
        	                JOptionPane.YES_NO_OPTION);

        	        if (confirmacion == JOptionPane.YES_OPTION) {

        	            try {
        	                BD.EliminarProducto(idContenido);

        	                // Quitar fila
        	                ((DefaultTableModel) tabla.getModel()).removeRow(filaSeleccionada);

        	                JOptionPane.showMessageDialog(null, "Contenido eliminado correctamente.");
        	            } catch (Exception ex) {
        	                JOptionPane.showMessageDialog(null,
        	                        "Error al eliminar el contenido: " + ex.getMessage(),
        	                        "Error",
        	                        JOptionPane.ERROR_MESSAGE);
        	            }
        	        }
        	    } else {
        	        JOptionPane.showMessageDialog(null,
        	                "Debe seleccionar un contenido para eliminar.",
        	                "Advertencia",
        	                JOptionPane.WARNING_MESSAGE);
        	    }
        	}

        });
        
        Editar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = tabla.getSelectedRow();
                if (r >= 0) {
                    try {
                        // Recuperamos datos de la tabla para rellenar la ventana
                        int id = (int) tabla.getValueAt(r, 0);
                        String nom = tabla.getValueAt(r, 1).toString();
                        float prec = Float.parseFloat(tabla.getValueAt(r, 2).toString());
                        int stock = (int) tabla.getValueAt(r, 3);
                        String desc = tabla.getValueAt(r, 4).toString();

                        // Creamos objeto temporal
                        Productos p = new Productos(nom, desc, prec, stock, id);
                        mostrarDialogoProducto(p);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error leyendo fila.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciona un producto.");
                }
            }
        });
        
        revalidate();
        repaint();
        setVisible(true);
    }
    
    private void  cargarDatosEnTabla() {
        // Limpiar la tabla antes de agregar los datos
        tablaModelo.setRowCount(0);
        
     
        // Obtener lista de usuarios desde la base de datos
        List<Productos> productos = BD.obtenerProductos();

        // Agregar los usuarios al modelo de la tabla
        for (Productos producto : productos) {
            Object[] fila = {
            		producto.getId(),
            		producto.getNombre(),
            		producto.getPrecio(),
            		producto.getStock(),
            		producto.getDescripcion(),
            		
            		
            		
            };
            tablaModelo.addRow(fila);
        }
    }

    private void mostrarDialogoProducto(Productos p) {
        JDialog d = new JDialog((JFrame)null, "Gestión Producto", true);
        d.setSize(350, 300);
        d.setLocationRelativeTo(this);
        d.setLayout(new GridLayout(5, 2));

        JTextField tNom = new JTextField(p != null ? p.getNombre() : "");
        JTextField tDesc = new JTextField(p != null ? p.getDescripcion() : "");
        
        JSpinner sPrec = new JSpinner(new SpinnerNumberModel(p != null ? p.getPrecio() : 0.0, 0.0, 10000.0, 0.5));
        JSpinner sStock = new JSpinner(new SpinnerNumberModel(p != null ? p.getStock() : 0, 0, 1000, 1));

        d.add(new JLabel("Nombre:")); d.add(tNom);
        d.add(new JLabel("Precio:")); d.add(sPrec);
        d.add(new JLabel("Stock:")); d.add(sStock);
        d.add(new JLabel("Descripción:")); d.add(tDesc);

        JButton btnSave = new JButton("Guardar");
        btnSave.addActionListener(ev -> {
            try {
                String n = tNom.getText();
                String de = tDesc.getText();
                float pr = ((Number)sPrec.getValue()).floatValue();
                int st = (int)sStock.getValue();

                if (p == null) {
                    Productos nuevo = new Productos(n, de, pr, st, 0);
                    BD.insertarProducto(nuevo);
                } else {
                    p.setNombre(n); p.setDescripcion(de);
                    p.setPrecio(pr); p.setStock(st);
                    BD.actualizarProducto(p);
                }
                cargarDatosEnTabla(); // Refrescar tabla
                d.dispose(); // Cerrar ventana
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Error en datos: " + ex.getMessage());
            }
        });

        d.add(new JLabel(""));
        d.add(btnSave);
        d.setVisible(true);
    }
    

}
