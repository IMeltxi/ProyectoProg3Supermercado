package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
				// TODO Auto-generated method stub
				//setVisible(false);
				
				//new Nuevo_productodisplay();
		
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

}
