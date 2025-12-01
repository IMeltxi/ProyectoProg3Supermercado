package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;

import domain.Cliente;
import domain.Productos;

import java.awt.*;
import java.util.List;

public class Ventanagestion extends JFrame {

    private JTable tableUsuarios;
    private JTable tableProductos;

    private List<Cliente> clientes;
    private List<Productos> productos;
    private Clientediplay cleintedisplay;
    private Productodisplay pDisplay;

    private static final long serialVersionUID = 1L;

    public Ventanagestion(JFrame vAnterior, List<Cliente> clientes, List<Productos> productos) {
        super("Administrador");

        this.clientes = clientes;
        this.productos = productos;

        setTitle("Administrador");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // --- Pestañas ---
        JTabbedPane tabbed = new JTabbedPane();

        // Panel de usuarios
        JPanel cliente = new JPanel(new BorderLayout());
        tableUsuarios = new JTable();
        JScrollPane scrollUsuarios = new JScrollPane(tableUsuarios);
        cliente.add(scrollUsuarios, BorderLayout.CENTER);

        // Panel de productos
        JPanel p_s = new JPanel(new BorderLayout());
        tableProductos = new JTable();
        JScrollPane scrollProductos = new JScrollPane(tableProductos);
        p_s.add(scrollProductos, BorderLayout.CENTER);

        tabbed.add(cliente, "Clientes");
        tabbed.add(p_s, "Productos");

        add(tabbed, BorderLayout.CENTER);
        
        //añadir clientes
        cleintedisplay = new Clientediplay();
        cliente.add(cleintedisplay, BorderLayout.CENTER);
        
        //añadir productos
        pDisplay = new Productodisplay();
        p_s.add(pDisplay, BorderLayout.CENTER);

        // --- Menú ---
        JMenuBar menu = new JMenuBar();
        JMenu fichero = new JMenu("Fichero");
        menu.add(fichero);

        fichero.addSeparator();

        JMenuItem salir = new JMenuItem("Salir");
        fichero.add(salir);
        fichero.addSeparator();

        JMenuItem cerrar_sesion = new JMenuItem("Cerrar sesión");
        fichero.add(cerrar_sesion);

        add(menu, BorderLayout.NORTH);

        salir.addActionListener(e -> {
            if (vAnterior != null) {
                vAnterior.setVisible(true);
            }
            dispose();
        });

        cerrar_sesion.addActionListener(e -> {
            new IniciarSesion();
            dispose();
        });

        // --- Cargar tablas --- sin base de datos
        //cargarTablaUsuarios();
        //cargarTablaProductos();

        setVisible(true);
    }

    // --- Tabla de usuarios con columna eliminar ---
    private void cargarTablaUsuarios() {
        String[] columnas = {"ID", "Nombre", "Apellido", "Fecha Nac.", "Email", "Tipo", "Puntos", "Eliminar"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Solo la columna eliminar editable
            }
        };

        for (Cliente c : clientes) {
            Object[] fila = {
                    c.getidCliente(),
                    c.getNombre(),
                    c.getApellido(),
                    c.getFechNac(),
                    c.getEmail(),
                    c.getTipoCliente(),
                    c.getPuntos(),
                    "Eliminar"
            };
            model.addRow(fila);
        }

        tableUsuarios.setModel(model);
        tableUsuarios.getColumn("Eliminar").setCellRenderer(new ButtonRenderer());
        tableUsuarios.getColumn("Eliminar").setCellEditor(new ButtonEditorUsuarios(model, clientes));
    }

    // --- Tabla de productos con columna eliminar ---
    private void cargarTablaProductos() {
        String[] columnas = {"ID", "Nombre", "Descripción", "Precio", "Stock", "Eliminar"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo eliminar editable
            }
        };

        for (Productos p : productos) {
            Object[] fila = {
                    p.getId(),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getPrecio(),
                    p.getStock(),
                    "Eliminar"
            };
            model.addRow(fila);
        }

        tableProductos.setModel(model);
        tableProductos.getColumn("Eliminar").setCellRenderer(new ButtonRenderer());
        tableProductos.getColumn("Eliminar").setCellEditor(new ButtonEditorProductos(model, productos));
    }

    // --- Renderer de botón ---
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // --- Editor de botón para usuarios ---
    private static class ButtonEditorUsuarios extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private DefaultTableModel model;
        private List<Cliente> lista;

        public ButtonEditorUsuarios(DefaultTableModel model, List<Cliente> lista) {
            this.model = model;
            this.lista = lista;
            button = new JButton("Eliminar");
            button.addActionListener(e -> {
                int row = model.getRowCount() - 1;
                row = button.getParent() instanceof JTable ? ((JTable) button.getParent()).getSelectedRow() : row;
                if (row >= 0 && row < lista.size()) {
                    lista.remove(row);
                    model.removeRow(row);
                }
            });
        }

        @Override
        public Object getCellEditorValue() { return ""; }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }
    }

    // --- Editor de botón para productos ---
    private static class ButtonEditorProductos extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private DefaultTableModel model;
        private List<Productos> lista;

        public ButtonEditorProductos(DefaultTableModel model, List<Productos> lista) {
            this.model = model;
            this.lista = lista;
            button = new JButton("Eliminar");
            button.addActionListener(e -> {
                int row = model.getRowCount() - 1;
                row = button.getParent() instanceof JTable ? ((JTable) button.getParent()).getSelectedRow() : row;
                if (row >= 0 && row < lista.size()) {
                    lista.remove(row);
                    model.removeRow(row);
                }
            });
        }

        @Override
        public Object getCellEditorValue() { return ""; }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            return button;
        }
    }
}


























