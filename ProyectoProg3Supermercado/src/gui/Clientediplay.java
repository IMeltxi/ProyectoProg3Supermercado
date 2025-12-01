package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import db.BD;
import domain.Cliente;
import io.*;

public class Clientediplay extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] headers = { "ID","TIPO CLIENTE","Nombre", "Apellido","Correo","FECHA NACIMIENTO","CONTRASEÑA","PUNTOS" };
    private JTable tabla;
    private DefaultTableModel tablaModelo;
    private Button Eliminar, Editar, btnAnadir;
    private JPanel pSur, pCentro;

    public Clientediplay() {
        setSize(400, 400);
        setLayout(new BorderLayout());
        
        pCentro = new JPanel(new GridBagLayout());
        pSur =new JPanel(new GridBagLayout());
        
        add(pCentro, BorderLayout.CENTER);
        add(pSur, BorderLayout.SOUTH);

        // Obtener lista de clientes desde la base de datos
       ArrayList<Cliente> listaUsuariosBD = BD.obtenerCliente();

        DefaultListModel<Cliente> modeloUsuarios = new DefaultListModel<>();
        for (Cliente cliente : listaUsuariosBD) {
            modeloUsuarios.addElement(cliente);
        }
        JList<Cliente> listausuarios = new JList<>(modeloUsuarios);
        listausuarios.setFixedCellWidth(200);
        listausuarios.setCellRenderer(new RendererCdisplay());
         
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
        
        btnAnadir = new Button("Añadir");
        btnAnadir.setBackground(Color.BLUE.darker());
        btnAnadir.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        btnAnadir.setForeground(Color.white);
        btnAnadir.setPreferredSize(new Dimension(150, 35));
        
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
        botonesPanel.add(btnAnadir);

        gbc.gridx = 0; // 
        gbc.gridy = 1; // 
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.weightx = 0; // Los botones no se expanden horizontalmente
        gbc.weighty = 0; // Los botones no se expanden verticalmente
      
        pCentro.add(botonesPanel, gbc);
        
        btnAnadir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoGestion(null); 
            }
        });

        Editar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    try {
                        // Recogemos datos de la tabla EN ORDEN CORRECTO
                        int id = (int) tabla.getValueAt(fila, 0);
                        String tipoStr = tabla.getValueAt(fila, 1).toString();
                        String nom = tabla.getValueAt(fila, 2).toString();
                        String ape = tabla.getValueAt(fila, 3).toString();
                        String email = tabla.getValueAt(fila, 4).toString();
                        
                        Object fechaObj = tabla.getValueAt(fila, 5); 
                        String fechaStr = fechaObj.toString(); // LocalDate a String

                        String pass = tabla.getValueAt(fila, 6).toString();
                        int pts = (int) tabla.getValueAt(fila, 7);

                        // Constructor para editar
                        Cliente c = new Cliente(
                            Cliente.tipoCliente.valueOf(tipoStr), 
                            id, nom, ape, fechaStr, email, pass, pts
                        );
                        mostrarDialogoGestion(c);
                        
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al leer fila: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciona un cliente para editar.");
                }
            }
        });
        
        Eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    int id = (int) tabla.getValueAt(fila, 0);
                    int opt = JOptionPane.showConfirmDialog(null, "¿Eliminar cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (opt == JOptionPane.YES_OPTION) {
                        try {
                            BD.EliminarUsuario(id); // Llamada a BD
                            ((DefaultTableModel)tabla.getModel()).removeRow(fila);
                            JOptionPane.showMessageDialog(null, "Eliminado.");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error al eliminar: " + ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciona un cliente para eliminar.");
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

        // Obtener lista de clientes desde la base de datos
        ArrayList<Cliente> clientes = BD.obtenerCliente();

        // Agregar los usuarios al modelo de la tabla
        for (Cliente cliente : clientes) {
            Object[] fila = {
            		cliente.getidCliente(), 
            	cliente.getTipoCliente(),
            	cliente.getNombre(),
            	cliente.getContrasena(),                      
                cliente.getApellido(),                
                cliente.getEmail(),
                cliente.getFechNac(),
                cliente.getPuntos()
                
            };
            tablaModelo.addRow(fila);
        }
    }
    
    private void mostrarDialogoGestion(Cliente cExistente) {
        JDialog d = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Gestión Cliente", true);
        d.setSize(400, 400);
        d.setLayout(new GridLayout(8, 2));
        d.setLocationRelativeTo(this);

        JTextField tNom = new JTextField(cExistente != null ? cExistente.getNombre() : "");
        JTextField tApe = new JTextField(cExistente != null ? cExistente.getApellido() : "");
        JTextField tMail = new JTextField(cExistente != null ? cExistente.getEmail() : "");
        JTextField tFec = new JTextField(cExistente != null ? cExistente.getFechNac().toString() : "2000-01-01");
        JTextField tPass = new JTextField(cExistente != null ? cExistente.getContrasena() : "");
        JSpinner sPuntos = new JSpinner(new SpinnerNumberModel(cExistente != null ? cExistente.getPuntos() : 0, 0, 99999, 1));
        JComboBox<Cliente.tipoCliente> cbTipo = new JComboBox<>(Cliente.tipoCliente.values());
        if(cExistente != null) cbTipo.setSelectedItem(cExistente.getTipoCliente());

        d.add(new JLabel("Nombre:")); d.add(tNom);
        d.add(new JLabel("Apellido:")); d.add(tApe);
        d.add(new JLabel("Email:")); d.add(tMail);
        d.add(new JLabel("F. Nac (yyyy-MM-dd):")); d.add(tFec);
        d.add(new JLabel("Contraseña:")); d.add(tPass);
        d.add(new JLabel("Puntos:")); d.add(sPuntos);
        d.add(new JLabel("Tipo:")); d.add(cbTipo);

        JButton btnSave = new JButton("Guardar");
        btnSave.addActionListener(ev -> {
            try {
                String n = tNom.getText(); String a = tApe.getText();
                String m = tMail.getText(); String fStr = tFec.getText();
                String p = tPass.getText(); int pts = (int) sPuntos.getValue();
                Cliente.tipoCliente t = (Cliente.tipoCliente) cbTipo.getSelectedItem();

                if (cExistente == null) {
                    Cliente nuevo = new Cliente(t, 0, n, a, fStr, m, p, pts);
                    BD.insertarCliente(nuevo);
                } else {
                    cExistente.setNombre(n); cExistente.setApellido(a);
                    cExistente.setEmail(m); cExistente.setContrasena(p);
                    cExistente.setFechNac(LocalDate.parse(fStr)); 
                    cExistente.setPuntos(pts); cExistente.setTipoCliente(t);
                    BD.actualizarCliente(cExistente);
                }
                cargarDatosEnTabla(); 
                d.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Error (Revisa fecha yyyy-MM-dd): " + ex.getMessage());
            }
        });
        d.add(new JLabel("")); d.add(btnSave);
        d.setVisible(true);
    }

}


