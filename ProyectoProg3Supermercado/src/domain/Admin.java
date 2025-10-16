package domain;

import java.util.List;

public class Admin {
	private int id;
	private String nombre;
	private String contrasena;
	private String correo;
	private List<Productos> productos;
	
	public Admin(int id, String nombre, String contrasena, String correo, List<Productos> productos) {
		this.id = id;
		this.nombre = nombre;
		this.contrasena = contrasena;
		this.correo = correo;
		this.productos = productos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public List<Productos> getProductos() {
		return productos;
	}

	public void setProductos(List<Productos> productos) {
		this.productos = productos;
	}
	
	
	
	
	
	
	
}
