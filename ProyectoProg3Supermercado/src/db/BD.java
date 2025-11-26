package db;
import java.sql.Connection;
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

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Cliente.tipoCliente tipo = Cliente.tipoCliente.valueOf(rs.getString("TIPO"));
                int id = rs.getInt("ID_CLIENTE");
                String nombre = rs.getString("NOMBRE");
                String apellido = rs.getString("APELLIDO");
                String fechaNac = rs.getString("FECHANA");
                String correo = rs.getString("EMAIL");
                String contrasenia = rs.getString("CONTRASEYA");
                int puntos = rs.getInt("PUNTOS");

                Cliente cliente = new Cliente(tipo, id, nombre, apellido, fechaNac, correo, contrasenia, puntos);

                cliente.setCarrito(obtenerCarritoDelCliente(id));

                clientes.add(cliente);
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



    

}
