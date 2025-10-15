package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class IniciarSesion extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField correoField;
    private JPasswordField contraseñaField;

    public IniciarSesion() {
        // Configuración de la ventana
        setTitle("Iniciar Sesión");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con GridBagLayout para centrar
        JPanel panelprincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel tituloLabel = new JLabel("MERCADEUSTO", JLabel.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 48)); 
        tituloLabel.setBorder(new EmptyBorder(200, 0, 40, 0)); 
        add(tituloLabel, BorderLayout.NORTH);

        // Panel interior con BoxLayout
        JPanel panelBotones = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Inicio de Sesión");
        panelBotones.setBorder(BorderFactory.createCompoundBorder(titledBorder, new EmptyBorder(20, 20, 20, 20)));
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        // Etiqueta y campo de correo
        JLabel correoLabel = new JLabel("Correo:");
        correoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(correoLabel);

        correoField = new JTextField(20);
        correoField.setMaximumSize(new Dimension(400, 30));
        correoField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(correoField);

        panelBotones.add(Box.createRigidArea(new Dimension(0, 10)));

        // Etiqueta y campo de contraseña
        JLabel contraseñaLabel = new JLabel("Contraseña:");
        contraseñaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(contraseñaLabel);

        contraseñaField = new JPasswordField(20);
        contraseñaField.setMaximumSize(new Dimension(400, 30));
        contraseñaField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(contraseñaField);

        panelBotones.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón Iniciar Sesión
        JButton botonInicioSesion = new JButton("Iniciar Sesión");
        botonInicioSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(botonInicioSesion);

        panelBotones.add(Box.createRigidArea(new Dimension(0, 20)));

        // Etiqueta y botón de registro
        JLabel registroEtiqueta = new JLabel("Regístrate si no tienes cuenta");
        registroEtiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(registroEtiqueta);

        JButton botonRegistrate = new JButton("Registrarse");
        botonRegistrate.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(botonRegistrate);

        // Agregar panel interior al principal
        panelprincipal.add(panelBotones, gbc);
        add(panelprincipal);

        setVisible(true);
    }

    // Método main para ejecutar la ventana
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new IniciarSesion();
        });
    }
}
