package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class PanelPrincipalContenido extends JPanel {

    private static final long serialVersionUID = 1L;

    public PanelPrincipalContenido() {
        
        setBackground(Color.WHITE);
        setLayout(new GridLayout(3, 4, 20, 20)); // 3 filas x 4 columnas, con separación

        // margen 
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // prototipo
        for (int i = 1; i <= 12; i++) {
            add(crearPanelProducto("Producto " + i, (double) (Math.random() * 10 + 1),i));
        }
    }

        private JPanel crearPanelProducto(String nombre, double precio, int j) {
            JPanel panelProducto = new JPanel(new BorderLayout());
            panelProducto.setBackground(Color.WHITE);
            panelProducto.setBorder(new LineBorder(new Color(0x013ADF), 2, true));

            JLabel lblImagen = new JLabel();
            lblImagen.setHorizontalAlignment(JLabel.CENTER);

            String path = "fotos_png/Producto" + j + ".png";
            ImageIcon icon;
            File f = new File(path);
            if (f.exists()) {
                icon = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            } else {
                BufferedImage placeholder = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                icon = new ImageIcon(placeholder);
            }
            lblImagen.setIcon(icon);

       
        JLabel lblNombre = new JLabel(nombre, JLabel.CENTER);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 16));

       

        JButton btnAdd = new JButton("Añadir");
        btnAdd.setBackground(new Color(0x013ADF));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));

       
        panelProducto.add(lblImagen, BorderLayout.CENTER);
        panelProducto.add(lblNombre, BorderLayout.NORTH);
        panelProducto.add(btnAdd, BorderLayout.PAGE_END);

        return panelProducto;
    }
}
