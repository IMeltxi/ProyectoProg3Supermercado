package domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

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
	
	//RECURSIVIDAD//
	public double calcularPuntos(List<Productos> listaInfoProductos) {
		// Iniciamos la recursión desde el índice 0 de la lista
		return procesarListaRecursiva(listaInfoProductos, 0);
	}


	//RECURSIVIDAD 1: Recorre la lista de productos disponibles uno a uno.
 
	private double procesarListaRecursiva(List<Productos> lista, int index) {
		// CASO BASE: Si llegamos al final de la lista, devolvemos 0
		if (index >= lista.size()) {
			return 0.0;
		}

		Productos prodActual = lista.get(index);
		String nombre = prodActual.getNombre();
		float precio = prodActual.getPrecio();

		double puntosEsteProducto = 0.0;

		// Verificamos si este producto está en nuestro HashMap de compra (y cuántos hay)
		if (this.productos.containsKey(nombre)) {
			int cantidadComprada = this.productos.get(nombre);

			// Filtro: Solo calculamos si el precio es > 10 (según tu regla anterior)
			if (precio > 5) {
				// Llamamos a la segunda recursividad para calcular el "bonus por cantidad"
				puntosEsteProducto = calcularPuntosPorCantidad(precio, cantidadComprada);
			}
		}

		// RECURSIÓN: Puntos de este producto + Puntos del resto de la lista
		return puntosEsteProducto + procesarListaRecursiva(lista, index + 1);
		}

	/**
	 * RECURSIVIDAD 2: Calcula los puntos progresivos según la cantidad.
	 * Ejemplo: 
	 * - Unidad 1: Precio * 1.0
	 * - Unidad 2: Precio * 1.3
	 * - Unidad 3: Precio * 1.6
	 */
	private double calcularPuntosPorCantidad(float precioBase, int nUnidad) {
		// CASO BASE: Si la unidad es 0, no suma nada.
		if (nUnidad <= 0) {
			return 0.0;
		}

		// LÓGICA DE NEGOCIO:
		// El multiplicador crece según el número de unidad.
		// (nUnidad - 1) * 0.3 significa:
		// Unidad 1 -> 1.0 + 0.0 = x1.0
		// Unidad 2 -> 1.0 + 0.3 = x1.3
		// Unidad 3 -> 1.0 + 0.6 = x1.6
		double multiplicador = 1.0 + ((nUnidad - 1) * 0.3);

		double valorEstaUnidad = precioBase * multiplicador;

		// RECURSIÓN: Valor de ESTA unidad (la número n) + Valor de la unidad anterior (n-1)
		return valorEstaUnidad + calcularPuntosPorCantidad(precioBase, nUnidad - 1);
	}


	
	
}
