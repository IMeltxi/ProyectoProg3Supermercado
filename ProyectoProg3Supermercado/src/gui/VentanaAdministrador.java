package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import domain.Cliente;
import domain.Productos;

public class VentanaAdministrador extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel pCentro, pNorte, pSur;
    private JButton vistausuario;
    private JButton vistaadministrador;
    private File icono;

    public VentanaAdministrador(JFrame vAnterior) {
        this.setBounds(0, 0, 600, 600);
        this.setLocationRelativeTo(null);
        this.setTitle("MercaDeusto - ADMINISTRADOR");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFrame vActual = this;

        pCentro = new JPanel(new GridBagLayout()); 
        pNorte = new JPanel();
        pSur = new JPanel();

        this.getContentPane().add(pNorte, BorderLayout.NORTH);
        this.getContentPane().add(pCentro, BorderLayout.CENTER);
        this.getContentPane().add(pSur, BorderLayout.SOUTH);

        pNorte.setBackground(Color.gray.brighter());
        pCentro.setBackground(Color.gray.brighter());
        pSur.setBackground(Color.gray.brighter());

        vistausuario = new JButton("VISTA USUARIO");
        vistausuario.setBackground(Color.BLUE.darker());
        vistausuario.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        vistausuario.setForeground(Color.white);

        vistaadministrador = new JButton("VISTA ADMINISTRADOR");
        vistaadministrador.setBackground(Color.BLUE.darker());
        vistaadministrador.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        vistaadministrador.setForeground(Color.white);

        // Imagen
        icono = new File("fotos_png/1106557.png");
        ImageIcon iconopng = new ImageIcon(icono.getPath());
        Image image = iconopng.getImage();
        Image imagenscalada = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon iconoescalado = new ImageIcon(imagenscalada);
        JLabel imagen = new JLabel(iconoescalado);

        pNorte.add(imagen);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10); // Espacios entre componentes

        gbc.gridx = 0;
        pCentro.add(vistausuario, gbc);

        gbc.gridx = 1;
        pCentro.add(vistaadministrador, gbc);

        setVisible(true);

        // MouseAdapter para vista administrador
        vistaadministrador.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Crear listas de ejemplo vacías para clientes y productos
                List<Cliente> clientes = new ArrayList<>();
                List<Productos> productos = new ArrayList<>();

                // Ocultar ventana actual
                setVisible(false);

                // Abrir ventana de gestión
                new Ventanagestion(vActual, clientes, productos);
            }
        });

        // MouseAdapter para vista usuario
        vistausuario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(vActual, "Vista Usuario seleccionada");
            }
        });
    }
}
