package db;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import domain.Cliente;
import domain.Productos;

public class BD {
	
	private static Connection con;


	//CONECTAR CON BASE DE DATOS
    public static void conectar() {
    	con = null;
    	
        try {
        	Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BaseDeDatos/supermecado.db");
            System.out.println("Conexión establecida.");
            crearTablas();
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        } 
    }

    //DESCONECTAR BASE DE DATOS
    public static void desconectar() {
    	if (con != null) {
    		try {
                con.close();
                con = null;

                System.out.println("Conexión cerrada.");
            }catch (SQLException e) {
            	System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
    	}
    }
    
    
    public static ArrayList<Cliente> obtenerCliente() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTE";

        if (con == null) conectar(); 

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ID_CLIENTE"); 
                
                String tipoStr = rs.getString("TIPO");
                Cliente.tipoCliente tipo = Cliente.tipoCliente.NORMAL; 
                if(tipoStr != null) {
                    try {
                        tipo = Cliente.tipoCliente.valueOf(tipoStr);
                    } catch (IllegalArgumentException e) {
                        tipo = Cliente.tipoCliente.NORMAL;
                    }
                }

                String nombre = rs.getString("NOMBRE");
                String apellido = rs.getString("APELLIDO");
                String email = rs.getString("EMAIL");
                String fechaStr = rs.getString("FECHANA"); 
                
                // Validación: Si viene nulo o vacío (Me he ayudado de ChatGPT para estas 8 líneas)
                if (fechaStr == null || fechaStr.trim().isEmpty()) {
                    fechaStr = "2000-01-01"; 
                } 
                else if (fechaStr.matches("\\d+")) { 
                    try {
                        long millis = Long.parseLong(fechaStr);
                        fechaStr = new java.sql.Date(millis).toLocalDate().toString();
                    } catch (Exception e) {
                        fechaStr = "2000-01-01"; // Si falla, fecha por defecto
                    }
                }

                String contrasena = rs.getString("CONTRASEYA");
                int puntos = rs.getInt("PUNTOS");

                Cliente c = new Cliente(tipo, id, nombre, apellido, fechaStr, email, contrasena, puntos);
                clientes.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
    
    public static List<Object[]> obtenerCarritoDelCliente(int idCliente) {
        List<Object[]> carrito = new ArrayList<>();

        String sql = "SELECT p.ID_PRODUCTO, p.NOMBRE, p.DESCRIPCION, p.PRECIO, p.STOCK, c.CANTIDAD " +
                     "FROM CARRITO c JOIN PRODUCTO p ON c.ID_PRODUCTO = p.ID_PRODUCTO " +
                     "WHERE c.ID_CLIENTE = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idProd = rs.getInt("ID_PRODUCTO");
                String nombre = rs.getString("NOMBRE");
                double precio = rs.getDouble("PRECIO");
                int cantidad = rs.getInt("CANTIDAD");
                double total = precio * cantidad;
                
                carrito.add(new Object[]{idProd, nombre, (double)cantidad, precio, total, "Eliminar"});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carrito;
    }
    
    
    public static void EliminarUsuario(int idCliente) {
        String sql = "DELETE FROM CLIENTE WHERE ID_CLIENTE = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static ArrayList<Productos> obtenerProductos() {
    	
    	ArrayList<Productos> productos = new ArrayList<Productos>();
    	
    	String sql ="SELECT * FROM PRODUCTO";
    	
    	try (PreparedStatement stmt = con.prepareStatement(sql);
    			ResultSet rs = stmt.executeQuery()){
    		while (rs.next()) {
    			Integer id = rs.getInt("ID_PRODUCTO");
    			String nombre = rs.getString("NOMBRE");
    			Float precio = rs.getFloat("PRECIO");
    			Integer stock = rs.getInt("STOCK");
    			String descripcion = rs.getString("DESCRIPCION");
    			
    			Productos nuevop = new Productos(nombre,descripcion,precio,stock,id,"");
    			
    			productos.add(nuevop);
    			
    		}
    		
    	}   catch (SQLException e) {
			e.printStackTrace();
    	}
    	return productos;
    }
    
    public static void EliminarProducto(int idproducto) {
        String sql = "DELETE FROM PRODUCTO WHERE ID_PRODUCTO = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idproducto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void insertarCliente(Cliente c) {
        String sql = "INSERT INTO CLIENTE (TIPO, NOMBRE, APELLIDO, EMAIL, FECHANA, CONTRASEYA, PUNTOS) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, c.getTipoCliente().toString());
            stmt.setString(2, c.getNombre());
            stmt.setString(3, c.getApellido());
            stmt.setString(4, c.getEmail());
            stmt.setDate(5, Date.valueOf(c.getFechNac()));
            stmt.setString(6, c.getContrasena());
            stmt.setInt(7, c.getPuntos());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarCliente(Cliente c) {
        String sql = "UPDATE CLIENTE SET NOMBRE=?, APELLIDO=?, EMAIL=?, FECHANA=?, CONTRASEYA=?, PUNTOS=?, TIPO=? WHERE ID_CLIENTE=?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getApellido());
            stmt.setString(3, c.getEmail());
            stmt.setDate(4, Date.valueOf(c.getFechNac()));
            stmt.setString(5, c.getContrasena());
            stmt.setInt(6, c.getPuntos());
            stmt.setString(7, c.getTipoCliente().toString());
            stmt.setInt(8, c.getidCliente());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //MÉTODOS PARA PRODUCTOS

    public static void insertarProducto(Productos p) {
        String sql = "INSERT INTO PRODUCTO (NOMBRE, DESCRIPCION, PRECIO, STOCK) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getDescripcion());
            stmt.setFloat(3, p.getPrecio());
            stmt.setInt(4, p.getStock());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarProducto(Productos p) {
        String sql = "UPDATE PRODUCTO SET NOMBRE=?, DESCRIPCION=?, PRECIO=?, STOCK=? WHERE ID_PRODUCTO=?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getDescripcion());
            stmt.setFloat(3, p.getPrecio());
            stmt.setInt(4, p.getStock());
            stmt.setInt(5, p.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // MÉTODOS PARA HISTORIAL DE COMPRAS 
    
    // Inserta una nueva compra en la tabla COMPRA y devuelve el ID generado.

    public static int insertarCompra(int idCliente, double total, String estado) {
        int idCompra = -1;
        String sql = "INSERT INTO COMPRA (ID_CLIENTE, TOTAL, ESTADO) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idCliente);
            stmt.setDouble(2, total);
            stmt.setString(3, estado);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idCompra = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idCompra;
    }

    // Inserta un producto en la tabla DETALLE_COMPRA asociado a una compra.
     
    public static void insertarDetalleCompra(int idCompra, Productos producto, int cantidad) {
        String sql = "INSERT INTO DETALLE_COMPRA (ID_COMPRA, ID_PRODUCTO, CANTIDAD, PRECIO_UNITARIO) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCompra);
            stmt.setInt(2, producto.getId());
            stmt.setInt(3, cantidad);
            stmt.setFloat(4, producto.getPrecio());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Devuelve el historial de compras de un cliente con los detalles de productos.
    public static List<String> historialComprasCliente(int idCliente) {
        List<String> historial = new ArrayList<>();
        String sql = "SELECT c.ID_COMPRA, c.FECHA_COMPRA, c.TOTAL, c.ESTADO, "
                   + "p.NOMBRE, d.CANTIDAD, d.PRECIO_UNITARIO "
                   + "FROM COMPRA c "
                   + "JOIN DETALLE_COMPRA d ON c.ID_COMPRA = d.ID_COMPRA "
                   + "JOIN PRODUCTO p ON d.ID_PRODUCTO = p.ID_PRODUCTO "
                   + "WHERE c.ID_CLIENTE = ? "
                   + "ORDER BY c.FECHA_COMPRA DESC";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idCompra = rs.getInt("ID_COMPRA");
                String fecha = rs.getString("FECHA_COMPRA");
                double total = rs.getDouble("TOTAL");
                String estado = rs.getString("ESTADO");
                String nombreProducto = rs.getString("NOMBRE");
                int cantidad = rs.getInt("CANTIDAD");
                float precioUnitario = rs.getFloat("PRECIO_UNITARIO");

                String linea = String.format("Compra #%d (%s) - %s - Total: %.2f | Producto: %s x%d a %.2f",
                        idCompra, fecha, estado, total, nombreProducto, cantidad, precioUnitario);
                historial.add(linea);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historial;
    }
    

    public static void crearTablas() {
    	if (con == null) return;
        try (Statement stmt = con.createStatement()) {
            
            // 1. TABLA CLIENTE
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CLIENTE (" +
                               "ID_CLIENTE INTEGER PRIMARY KEY AUTOINCREMENT, " +
                               "TIPO TEXT, " +
                               "NOMBRE TEXT, " +
                               "APELLIDO TEXT, " +
                               "EMAIL TEXT, " +
                               "FECHANA TEXT, " +
                               "CONTRASEYA TEXT, " +
                               "PUNTOS INTEGER)");

            // 2. TABLA PRODUCTO
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS PRODUCTO (" +
                               "ID_PRODUCTO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                               "NOMBRE TEXT, DESCRIPCION TEXT, PRECIO REAL, STOCK INTEGER)");

            // 3. TABLA CARRITO
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CARRITO (" +
                               "ID_CLIENTE INTEGER, ID_PRODUCTO INTEGER, CANTIDAD INTEGER DEFAULT 1, " +
                               "PRIMARY KEY (ID_CLIENTE, ID_PRODUCTO), " +
                               "FOREIGN KEY(ID_CLIENTE) REFERENCES CLIENTE(ID_CLIENTE), " +
                               "FOREIGN KEY(ID_PRODUCTO) REFERENCES PRODUCTO(ID_PRODUCTO))");
            
            // 4. TABLAS COMPRA Y DETALLE
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS COMPRA (" +
                               "ID_COMPRA INTEGER PRIMARY KEY AUTOINCREMENT, " +
                               "ID_CLIENTE INTEGER, TOTAL REAL, ESTADO TEXT, " +
                               "FECHA_COMPRA TEXT DEFAULT CURRENT_TIMESTAMP)");
            
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS DETALLE_COMPRA (" +
                               "ID_COMPRA INTEGER, ID_PRODUCTO INTEGER, CANTIDAD INTEGER, PRECIO_UNITARIO REAL)");
            
            try {
                stmt.executeUpdate("ALTER TABLE CARRITO ADD COLUMN CANTIDAD INTEGER DEFAULT 1");
            } catch (SQLException e) {}
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCTO");
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Base de datos de productos vacía. Insertando productos por defecto...");
                String sqlInsert = "INSERT INTO PRODUCTO (NOMBRE, DESCRIPCION, PRECIO, STOCK) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = con.prepareStatement(sqlInsert)) {
                    for (int i = 1; i <= 12; i++) {
                        pstmt.setString(1, "Producto " + i);
                        pstmt.setString(2, "Descripción del producto " + i);
                        double precio = Math.round((Math.random() * 10 + 1) * 100.0) / 100.0; 
                        pstmt.setDouble(3, precio);
                        pstmt.setInt(4, 100);
                        pstmt.executeUpdate();
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void agregarProductoCarrito(int idCliente, int idProducto) {
        // Verificar si ya existe para sumar cantidad o insertar nuevo
        String sqlCheck = "SELECT CANTIDAD FROM CARRITO WHERE ID_CLIENTE = ? AND ID_PRODUCTO = ?";
        try {
            if (con == null) conectar();
            PreparedStatement pst = con.prepareStatement(sqlCheck);
            pst.setInt(1, idCliente);
            pst.setInt(2, idProducto);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Existe: Actualizamos cantidad (+1)
                String sqlUpdate = "UPDATE CARRITO SET CANTIDAD = CANTIDAD + 1 WHERE ID_CLIENTE = ? AND ID_PRODUCTO = ?";
                PreparedStatement pstUp = con.prepareStatement(sqlUpdate);
                pstUp.setInt(1, idCliente);
                pstUp.setInt(2, idProducto);
                pstUp.executeUpdate();
            } else {
                // No existe: Insertamos
                String sqlInsert = "INSERT INTO CARRITO (ID_CLIENTE, ID_PRODUCTO, CANTIDAD) VALUES (?, ?, 1)";
                PreparedStatement pstIn = con.prepareStatement(sqlInsert);
                pstIn.setInt(1, idCliente);
                pstIn.setInt(2, idProducto);
                pstIn.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void eliminarProductoCarrito(int idCliente, int idProducto) {
        String sql = "DELETE FROM CARRITO WHERE ID_CLIENTE = ? AND ID_PRODUCTO = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            pstmt.setInt(2, idProducto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarCantidadCarrito(int idCliente, int idProducto, int nuevaCantidad) {
        String sql = "UPDATE CARRITO SET CANTIDAD = ? WHERE ID_CLIENTE = ? AND ID_PRODUCTO = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, nuevaCantidad);
            pstmt.setInt(2, idCliente);
            pstmt.setInt(3, idProducto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    
}
