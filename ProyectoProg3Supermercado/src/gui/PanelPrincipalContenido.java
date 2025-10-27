package gui;

import java.awt.*;
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
            add(crearPanelProducto("Producto " + i, (double) (Math.random() * 10 + 1)));
        }
    }

    private JPanel crearPanelProducto(String nombre, double precio) {
        JPanel panelProducto = new JPanel();
        panelProducto.setLayout(new BorderLayout());
        panelProducto.setBackground(Color.WHITE);
        panelProducto.setBorder(new LineBorder(new Color(0x013ADF), 2, true));

        
        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(JLabel.CENTER);
        lblImagen.setIcon(new ImageIcon(new ImageIcon("fotos_png/producto.png")
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

       
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
