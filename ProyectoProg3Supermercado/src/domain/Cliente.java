package domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
	public enum tipoCliente {
        INVITADO, PREMIUM, NORMAL
    }
    private tipoCliente tipoCliente;
    private int idCliente;
    private String nombre;
    private String apellido;
    private LocalDate fechNac;
    private String email;
    private String contrasena;
    private int puntos;
    private List<Productos> carrito;
    
    // Constructor completo para inicializar todos los atributos del Cliente
	public Cliente(tipoCliente tipoCliente, int idCliente, String nombre, String apellido,
			String fechNacStr, String email, String contrasena, int puntos, List<Productos> carrito) {
		super();
		this.tipoCliente = tipoCliente;
		this.idCliente = idCliente;
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechNac = LocalDate.parse(fechNacStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.email = email;
		this.contrasena = contrasena;
		this.puntos = puntos;
		this.carrito = carrito;
	}
	
    // Constructor sin carrito, inicializa el carrito como una lista vacía
	public Cliente(tipoCliente tipoCliente, int idCliente, String nombre, String apellido,
			String fechNacStr, String email, String contrasena, int puntos) {
		super();
		this.tipoCliente = tipoCliente;
		this.idCliente = idCliente;
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechNac = LocalDate.parse(fechNacStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.email = email;
		this.contrasena = contrasena;
		this.puntos = puntos;
		this.carrito = new ArrayList<>();
	}

	public tipoCliente getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(tipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public int getidCliente() {
		return idCliente;
	}

	public void setidCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public LocalDate getFechNac() {
		return fechNac;
	}

	public void setFechNac(LocalDate fechNac) {
		this.fechNac = fechNac;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public List<Productos> getCarrito() {
		return carrito;
	}

	public void setCarrito(List<Productos> carrito) {
		this.carrito = carrito;
	}
        
}
