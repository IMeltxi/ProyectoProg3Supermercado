package gui;

import javax.swing.*;
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
        panelCentro.add(new JLabel("Usuario:"), gbc);

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
        gbc.gridwidth = 2;
        JButton btnRegistrar = new JButton("Registrarse");
        panelCentro.add(btnRegistrar, gbc);

        gbc.gridy++;
        JButton btnEmpleado = new JButton("Empleado");
        panelCentro.add(btnEmpleado, gbc);

        // Agregar el panel al principal
        panel.add(panelCentro, BorderLayout.CENTER);

        // Añadir panel principal al frame
        add(panel);
    }

    // Método main para ejecutar la ventana
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistroSesion ventana = new RegistroSesion();
            ventana.setVisible(true);
        });
    }
}
