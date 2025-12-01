package db;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        } 
    }

    //DESCONECTAR BASE DE DATOS
    public static void desconectar() {
    	if (con != null) {
    		try {
                con.close();

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
    
    public static List<Productos> obtenerCarritoDelCliente(int idCliente) {
        List<Productos> carrito = new ArrayList<>();

        String sql = "SELECT p.ID_PRODUCTO, p.NOMBRE, p.DESCRIPCION, p.PRECIO, p.STOCK " +
                     "FROM CARRITO c JOIN PRODUCTO p ON c.ID_PRODUCTO = p.ID_PRODUCTO " +
                     "WHERE c.ID_CLIENTE = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Productos p = new Productos();
                p.setId(rs.getInt("ID_PRODUCTO"));
                p.setNombre(rs.getString("NOMBRE"));
                p.setDescripcion(rs.getString("DESCRIPCION"));
                p.setPrecio(rs.getFloat("PRECIO"));
                p.setStock(rs.getInt("STOCK"));
                carrito.add(p);
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
    			
    			Productos nuevop = new Productos(nombre,descripcion,precio,stock,id);
    			
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

    // --- MÉTODOS PARA PRODUCTOS ---

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

    

}
