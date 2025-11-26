package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import db.BD;
import domain.Cliente;

public class Clientediplay extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] headers = { "TIPO CLIENTE","Contraseña","Nombre", "Apellido","Correo","FECHA NACIMIENTO","PUNTOS" };
    private JTable tabla;
    private DefaultTableModel tablaModelo;
    private Button Eliminar, Editar;
    private JPanel pSur, pCentro;

    public Clientediplay() {
        setSize(400, 400);
        setLayout(new BorderLayout());
        
        pCentro = new JPanel(new GridBagLayout());
        pSur =new JPanel(new GridBagLayout());
        
        add(pCentro, BorderLayout.CENTER);
        add(pSur, BorderLayout.SOUTH);

        // Obtener lista de usuarios desde la base de datos
       ArrayList<Cliente> listaUsuariosBD = BD.obtenerUsuarios();

        DefaultListModel<Cliente> modeloUsuarios = new DefaultListModel<>();
        for (Cliente cliente : listaUsuariosBD) {
            modeloUsuarios.addElement(cliente);
        }
        JList<Cliente> listausuarios = new JList<>(modeloUsuarios);
        listausuarios.setFixedCellWidth(200);
        listausuarios.setCellRenderer(new RendererUdisplay());
         
        // Scroll para el JList
        JScrollPane scroll = new JScrollPane(listausuarios);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.WEST);

        // Crear el modelo de tabla con las cabeceras
        
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
           
        tabla = new JTable(tablaModelo);
        tabla.setRowHeight(30);
        tabla.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        tabla.getTableHeader().setReorderingAllowed(false); 
        cargarDatosEnTabla();
        
      
        
        
        // Listener para actualizar la tabla al seleccionar un usuario en el JList
        /*listausuarios.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Usuario usuario = listausuarios.getSelectedValue();
                    if (usuario != null) {
                    	updatedatostabla(usuario);
                    }
                }
            }
        });*/
        
        
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
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margen alrededor de los componentes

        // Configuración para la JTable
        gbc.gridx = 0; // Columna 0
        gbc.gridy = 0; // Fila 0
        gbc.gridwidth = 2; // La tabla ocupa dos columnas
        gbc.weightx = 1.0; // Peso horizontal (se expande en ancho)
        gbc.weighty = 1.0; // Peso vertical (se expande en alto)
        gbc.fill = GridBagConstraints.BOTH; // La tabla ocupa todo el espacio disponible
        pCentro.add(new JScrollPane(tabla), gbc);

        // Configuración para los botones
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        botonesPanel.add(Editar);
        botonesPanel.add(Eliminar);

        gbc.gridx = 0; // 
        gbc.gridy = 1; // 
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.weightx = 0; // Los botones no se expanden horizontalmente
        gbc.weighty = 0; // Los botones no se expanden verticalmente
      
        pCentro.add(botonesPanel, gbc);

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
		                return editable; // Todas las celdas no serán editables
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
                    // Obtener el ID del usuario seleccionado
                    String NombreUsuario = (String) tabla.getValueAt(filaSeleccionada, 0);             
                    
                    Usuario usuarioeliminar = new Usuario(NombreUsuario, null, null, null, null, null, null);
                    
                    // Confirmar
                    int confirmacion = JOptionPane.showConfirmDialog(null, 
                            "¿Está seguro de que desea eliminar este usuario?", 
                            "Confirmar Eliminación", 
                            JOptionPane.YES_NO_OPTION);

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        // Llamar al método delete con el objeto Usuario
						BD.EliminarUsuario(usuarioeliminar);
						

						// Eliminar la fila del modelo de la tabla
						((DefaultTableModel) tabla.getModel()).removeRow(filaSeleccionada);
						JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un usuario para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
	    
        revalidate();
        repaint();
        setVisible(true);
    }

   
    private void cargarDatosEnTabla() {
        // Limpiar la tabla antes de agregar los datos
        tablaModelo.setRowCount(0);

        // Obtener lista de usuarios desde la base de datos
        ArrayList<Usuario> usuarios = BD.obtenerUsuarios();

        // Agregar los usuarios al modelo de la tabla
        for (Usuario usuario : usuarios) {
            Object[] fila = {
                usuario.getNombreUsuario(),
                usuario.getContrasenia(),
                usuario.getNombre(),
                usuario.getApellido(),  
                usuario.getCorreo(),
                usuario.getTelefono(),
                usuario.getPlan()
                
            };
            tablaModelo.addRow(fila);
        }
    }

}
