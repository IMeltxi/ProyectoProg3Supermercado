package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class PanelPrincipalContenido extends JPanel {

    private static final long serialVersionUID = 1L;
    private VentanaCarrito carritoRef; // Referencia al carrito

    public PanelPrincipalContenido(VentanaCarrito carrito) {
        this.carritoRef = carrito; 
        
        setBackground(Color.WHITE);
        setLayout(new GridLayout(3, 4, 20, 20)); 
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        for (int i = 1; i <= 12; i++) {
            double precio = Math.round((Math.random() * 10 + 1) * 100.0) / 100.0;
            add(crearPanelProducto("Producto " + i, precio, i));
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
            Graphics2D g2d = placeholder.createGraphics();
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0,0,100,100); 
            g2d.dispose();
            icon = new ImageIcon(placeholder);
        }
        lblImagen.setIcon(icon);
        // ...

        JLabel lblNombre = new JLabel(nombre, JLabel.CENTER);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        // Mostrar precio en la etiqueta
        JLabel lblPrecio = new JLabel(precio + " €", JLabel.CENTER);

        JButton btnAdd = new JButton("Añadir");
        btnAdd.setBackground(new Color(0x013ADF));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));

        // LÓGICA DEL BOTÓN AÑADIR 
        btnAdd.addActionListener(e -> {
            if (carritoRef != null) {
                carritoRef.agregarProducto(nombre, precio);
                JOptionPane.showMessageDialog(this, nombre + " añadido al carrito.");
            }
        });

        // Estructura visual
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(Color.WHITE);
        centro.add(lblImagen, BorderLayout.CENTER);
        centro.add(lblPrecio, BorderLayout.SOUTH);

        panelProducto.add(lblNombre, BorderLayout.NORTH);
        panelProducto.add(centro, BorderLayout.CENTER); // Imagen + Precio
        panelProducto.add(btnAdd, BorderLayout.PAGE_END);

        return panelProducto;
    }
}