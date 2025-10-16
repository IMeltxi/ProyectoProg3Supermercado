package domain;

import java.time.LocalDate;
import java.util.HashMap;

public class Compra extends Productos {
	private int id;
	private int idUsuario;
	private HashMap<String, Integer> productos;// Mapa de productos y sus cantidades
	private LocalDate fechaCompra;
	private double precioTotal;
	
	public Compra(int id, int idUsuario, HashMap<String, Integer> productos, LocalDate fechaCompra,
			double precioTotal) {
		super();
		this.id = id;
		this.idUsuario = idUsuario;
		this.productos = productos;
		this.fechaCompra = fechaCompra;
		this.precioTotal = precioTotal;
	}
	public Compra() {
		this.id = 0;
		this.idUsuario = 0;
		this.productos = new HashMap<>();
		this.fechaCompra = LocalDate.now();
		this.precioTotal = 0.0;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public HashMap<String, Integer> getProductos() {
		return productos;
	}
	public void setProductos(HashMap<String, Integer> productos) {
		this.productos = productos;
	}
	public LocalDate getFechaCompra() {
		return fechaCompra;
	}
	public void setFechaCompra(LocalDate fechaCompra) {
		this.fechaCompra = fechaCompra;
	}
	public double getPrecioTotal() {
		return precioTotal;
	}
	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}
	
	public void agregarProducto(String nombre, int cantidad) {
		if (productos.containsKey(nombre)) {
			productos.put(nombre, productos.get(nombre) + cantidad);
		} else {
			productos.put(nombre, cantidad);
		}
	}
	
	public void eliminarProducto(String nombre, int cantidad) {
		if (productos.containsKey(nombre)) {
			int nuevaCantidad = productos.get(nombre) - cantidad;
			if (nuevaCantidad > 0) {
				productos.put(nombre, nuevaCantidad);
			} else {
				productos.remove(nombre);
			}
										}
		}
//Metodo que calcula el precio total de la compra usando el mapa y getprecio de la clase productos
public double calcularPrecioTotal(HashMap<String, Productos> carrito) {
     double total = 0.0;
     for(Productos producto : carrito.values()) {
    	total += producto.getPrecio() * productos.get(producto.getNombre());
     }
	 return total;
}
	
	

	
	
}
