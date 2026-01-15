package gui;

import javax.swing.*;

import db.BD;
import domain.Cliente;

import java.awt.*;

public class RegistroSesion extends JFrame {

    public RegistroSesion() {
        // Configuración básica de la ventana
        setTitle("Registro");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        
        
        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        
      

        // Panel central (formulario)
        JPanel panelCentro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("MercaDeusto");
        lblTitulo.setFont(new Font("Comic Sans MS", Font.BOLD, 100));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelCentro.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        panelCentro.add(new JLabel("CorreoElectronico:"), gbc);

        gbc.gridx = 1;
        JTextField txtUsuario = new JTextField(15);
        panelCentro.add(txtUsuario, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panelCentro.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        JPasswordField txtContrasena = new JPasswordField(15);
        panelCentro.add(txtContrasena, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panelCentro.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        JTextField txtNombre = new JTextField(15);
        panelCentro.add(txtNombre, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panelCentro.add(new JLabel("Apellido:"), gbc);

        gbc.gridx = 1;
        JTextField txtApellido = new JTextField(15);
        panelCentro.add(txtApellido, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panelCentro.add(new JLabel("Fecha Nacimiento (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        JTextField txtFecha = new JTextField(15);
        panelCentro.add(txtFecha, gbc);
		// =====================

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton btnRegistrar = new JButton("Registrarse");
        panelCentro.add(btnRegistrar, gbc);
        
        btnRegistrar.addActionListener(e -> {
            
            // Obtener datos
            String email = txtUsuario.getText();
            String contrasena = new String(txtContrasena.getPassword());
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String fechaNac = txtFecha.getText();
            
            

            if (email.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debes rellenar todos los campos.");
                return;
            }

            // Abrimos conexión si no está abierta
            BD.conectar();
            
            int ID = BD.obtenerCliente().size() + 1;
            // Crear cliente (campos mínimos)
            Cliente nuevo = new Cliente(
                    Cliente.tipoCliente.NORMAL,
                    ID,                     
                    nombre,               
                    apellido,             
                    fechaNac,          
                    email,
                    contrasena,
                    0                       // puntos
            );

            // Insertar en BD
            BD.insertarCliente(nuevo);

            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
            
            IniciarSesion inicioSesion = new IniciarSesion();
            inicioSesion.setVisible(true);
            this.dispose();
            
        });


        gbc.gridy++;
        JButton btnEmpleado = new JButton("Empleado");
        panelCentro.add(btnEmpleado, gbc);

        // Agregar el panel al principal
        panel.add(panelCentro, BorderLayout.CENTER);

        // Añadir panel principal al frame
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistroSesion ventana = new RegistroSesion();
            ventana.setVisible(true);
        });
    }
}
