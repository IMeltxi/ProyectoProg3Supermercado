package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import domain.Cliente;
import domain.Productos;

public class Ventanacomprarecursiva extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtPresupuesto;
    private JPanel panelResultados;

    private ArrayList<Productos> listaProductos;   
    private float presupuesto;
    private String rutaTxtProductos = "Resources/productosInfo.txt";

    public Ventanacomprarecursiva(Cliente cliente) {
        setTitle("Compra recursiva por presupuesto");
        setSize(1000, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        listaProductos = new ArrayList<>();

       //carga
        cargarProductosDesdeTxt(rutaTxtProductos);

        
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBorder(new TitledBorder("Buscar combinaciones de productos"));

        JLabel lblPresupuesto = new JLabel("Introduce tu PRESUPUESTO (€):");
        txtPresupuesto = new JTextField(10);

        JButton btnObtener = new JButton("Obtener compra");

        JPanel panelL1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelL1.add(lblPresupuesto);
        panelL1.add(txtPresupuesto);
        panelL1.add(btnObtener);

        panelFormulario.add(panelL1);
        add(panelFormulario, BorderLayout.NORTH);

       
        panelResultados = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JScrollPane scrollResultados = new JScrollPane(panelResultados);
        add(scrollResultados, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
            this,
            "Se mostrarán combinaciones de productos que se ajusten al presupuesto y stock.",
            "Información",
            JOptionPane.INFORMATION_MESSAGE
        );

        // ---------- VALIDACIÓN TEXTO ----------
        txtPresupuesto.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume();
                }
            }
        });

        // boton
        btnObtener.addActionListener(e -> {
            panelResultados.removeAll();

            if (txtPresupuesto.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Introduce un presupuesto.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                presupuesto = Float.parseFloat(txtPresupuesto.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Presupuesto no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<ArrayList<Productos>> combinaciones = calcularCombinaciones(presupuesto);

            if (combinaciones.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron combinaciones.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Elegir la combinación que más se acerque al presupuesto
            ArrayList<Productos> mejor = null;
            float mejorSuma = 0;

            for (ArrayList<Productos> combo : combinaciones) {
                float suma = 0;
                for (Productos p : combo) suma += p.getPrecio();

                if (suma > mejorSuma && suma <= presupuesto) {
                    mejorSuma = suma;
                    mejor = combo;
                }
            }

            if (mejor != null) {
                float total = 0;
                for (Productos p : mejor) {
                    panelResultados.add(crearPanelProducto(p));
                    total += p.getPrecio();
                }

                JLabel lblTotal = new JLabel("TOTAL: " + total + " €");
                lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
                panelResultados.add(lblTotal);
            }

            panelResultados.revalidate();
            panelResultados.repaint();
        });

        setVisible(true);
    }

    // crear panel
    private JPanel crearPanelProducto(Productos p) {
        JPanel panelProducto = new JPanel(new BorderLayout());
        panelProducto.setPreferredSize(new Dimension(120, 150));
        panelProducto.setBackground(Color.WHITE);
        panelProducto.setBorder(new LineBorder(new Color(0x013ADF), 2, true));

        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(JLabel.CENTER);

        
        String path = p.getRutaImagen();

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
        JLabel lblPrecio = new JLabel(p.getPrecio() + " €", JLabel.CENTER);

        panelProducto.add(lblImagen, BorderLayout.CENTER);
        panelProducto.add(lblNombre, BorderLayout.NORTH);
        panelProducto.add(lblPrecio, BorderLayout.SOUTH);

        return panelProducto;
    }

    // cargar txt
    private void cargarProductosDesdeTxt(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) { primeraLinea = false; continue; } 
                String[] partes = linea.split(";");
                if (partes.length < 6) continue;

                String nombre = partes[0].replace("\"","").trim();
                String descripcion = partes[1].replace("\"","").trim();
                float precio = Float.parseFloat(partes[2].trim());
                int stock = Integer.parseInt(partes[3].trim());
                int id = Integer.parseInt(partes[4].trim());
                String rutaImagen = partes[5].replace("\"","").trim();

                Productos p = new Productos(nombre, descripcion, precio, stock, id, rutaImagen);
                listaProductos.add(p);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // fun recursiva
    private ArrayList<ArrayList<Productos>> calcularCombinaciones(float presupuesto) {
        ArrayList<ArrayList<Productos>> resultado = new ArrayList<>();
        if (listaProductos.isEmpty()) return resultado;

        calcularCombinacionesProductos(resultado, new ArrayList<>(), presupuesto, 0, 0, listaProductos);

        return resultado;
    }

    private void calcularCombinacionesProductos(ArrayList<ArrayList<Productos>> resultado,  ArrayList<Productos> temp, float presupuesto, float suma, int inicio, ArrayList<Productos> productos) {

        if (suma > presupuesto) return;

        if (!temp.isEmpty()) {
            resultado.add(new ArrayList<>(temp));
        }

        for (int i = inicio; i < productos.size(); i++) {
            Productos p = productos.get(i);

            // Si stock > 0, se puede añadir
            if (p.getStock() > 0) {
                temp.add(p);
                calcularCombinacionesProductos(resultado, temp, presupuesto, suma + p.getPrecio(), i + 1, productos);
                temp.remove(temp.size() - 1);
            }
        }
    }
}
