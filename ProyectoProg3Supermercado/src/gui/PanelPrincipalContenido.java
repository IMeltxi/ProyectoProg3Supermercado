package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;

import io.LectorCSV; // <-- IMPORTANTE
import domain.Productos;

public class PanelPrincipalContenido extends JPanel {

    private static final long serialVersionUID = 1L;
    private VentanaCarrito carritoRef; 
    private List<Productos> listaProductos;

    public PanelPrincipalContenido(VentanaCarrito carrito, List<Productos> listaProductos) {
        this.carritoRef = carrito; 
        this.listaProductos = listaProductos;
        
        setBackground(Color.WHITE);

        int filas = (int) Math.ceil(listaProductos.size() / 4.0);
        if (filas < 3) filas = 3;
       
        setLayout(new GridLayout(filas, 4, 20, 20)); 
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        for (Productos p : listaProductos) {
            add(crearPanelProducto(p));
        }
    }

    private JPanel crearPanelProducto(Productos p) {
        JPanel panelProducto = new JPanel(new BorderLayout());
        panelProducto.setBackground(Color.WHITE);
        panelProducto.setBorder(new LineBorder(new Color(0x013ADF), 2, true));

        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(JLabel.CENTER);
        String path = "fotos_png/Producto" + p.getId() + ".png";
        p.setRutaImagen(path); //
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

        JLabel lblNombre = new JLabel(p.getNombre(), JLabel.CENTER);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        JLabel lblPrecio = new JLabel(p.getPrecio() + " €", JLabel.CENTER);

        JButton btnAdd = new JButton("Añadir");
        btnAdd.setBackground(new Color(0x013ADF));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnAdd.addActionListener(e -> {
            if (carritoRef != null) {
            	carritoRef.agregarProducto(p);
                //JOptionPane.showMessageDialog(this, p.getNombre() + " añadido al carrito.");
            }
        });

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(Color.WHITE);
        centro.add(lblImagen, BorderLayout.CENTER);
        centro.add(lblPrecio, BorderLayout.SOUTH);

        panelProducto.add(lblNombre, BorderLayout.NORTH);
        panelProducto.add(centro, BorderLayout.CENTER); 
        panelProducto.add(btnAdd, BorderLayout.PAGE_END);

        return panelProducto;
    }
}