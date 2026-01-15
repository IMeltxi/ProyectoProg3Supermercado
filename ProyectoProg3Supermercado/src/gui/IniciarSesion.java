package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import domain.Cliente;
import domain.Productos;
import db.BD; 

public class IniciarSesion extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField correoField;
    private JPasswordField contrasenaField;

    private List<Cliente> clientes;
    private List<Productos> productos;

    public IniciarSesion() {
        
        JFrame vActual = this;

        // Configuración de ventana
        setTitle("Iniciar Sesión");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelprincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel tituloLabel = new JLabel("MERCADEUSTO", JLabel.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 48));
        tituloLabel.setBorder(new EmptyBorder(200, 0, 40, 0));
        add(tituloLabel, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Inicio de Sesión");
        panelBotones.setBorder(BorderFactory.createCompoundBorder(titledBorder, new EmptyBorder(60, 200, 60, 200)));
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        JLabel correoLabel = new JLabel("Correo:");
        correoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        correoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(correoLabel);

        correoField = new JTextField(20);
        correoField.setMaximumSize(new Dimension(400, 30));
        correoField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(correoField);

        panelBotones.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel contrasenaLabel = new JLabel("Contraseña:");
        contrasenaLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        contrasenaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(contrasenaLabel);

        contrasenaField = new JPasswordField(20);
        contrasenaField.setMaximumSize(new Dimension(400, 30));
        contrasenaField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(contrasenaField);

        panelBotones.add(Box.createRigidArea(new Dimension(0, 50)));

        JButton botonInicioSesion = new JButton("Iniciar Sesión");
        botonInicioSesion.setFont(new Font("Arial", Font.BOLD, 16));
        botonInicioSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(botonInicioSesion);

        panelBotones.add(Box.createRigidArea(new Dimension(0, 50)));

        JLabel registroEtiqueta = new JLabel("Regístrate si no tienes cuenta");
        registroEtiqueta.setFont(new Font("Arial", Font.ITALIC, 14));
        registroEtiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(registroEtiqueta);

        JButton botonRegistrate = new JButton("Registrarse");
        botonRegistrate.setFont(new Font("Arial", Font.BOLD, 16));
        botonRegistrate.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(botonRegistrate);

        // Acción login
        botonInicioSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuarioPuesto = correoField.getText();
                String passwordStr = new String(contrasenaField.getPassword());

                // 1. Caso ADMIN
                if (usuarioPuesto.equals("admin") && passwordStr.equals("admin123")) {
                    vActual.setVisible(false);
                    new Ventanagestion(vActual, clientes, productos);
                } 
                // 2. Caso CLIENTE
                else {
                    Cliente clienteEncontrado = null;
                    List<Cliente> usuariosBD = BD.obtenerCliente();
                    
                    for (Cliente c : usuariosBD) {
                        // Verificamos email y contraseña
                        if (c.getEmail() != null && c.getEmail().equals(usuarioPuesto) && 
                            c.getContrasena() != null && c.getContrasena().equals(passwordStr)) {
                            clienteEncontrado = c;
                            break;
                        }
                    }

                    if (clienteEncontrado != null) {
                        vActual.setVisible(false);
                        // Abrimos la ventana principal de la tienda para el usuario
                        new VentanaPrincipal(clienteEncontrado); 
                    } else {
                        JOptionPane.showMessageDialog(vActual, "Usuario o contraseña incorrectos");
                    }
                }
            }
        });

        botonRegistrate.addActionListener(e -> {
            dispose();
            RegistroSesion reg = new RegistroSesion();
            reg.setVisible(true);
        });

        panelprincipal.add(panelBotones, gbc);
        add(panelprincipal);

        setVisible(true);
    }
}