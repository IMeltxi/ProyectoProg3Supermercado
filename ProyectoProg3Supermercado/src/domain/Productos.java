package domain;

public class Productos {
	private String nombre;
	private String descripcion;
	private float precio;
	private int stock;
	private int id;
	private String rutaImagen;
	
	public Productos() {	
	}
	
	public Productos(String nombre, String descripcion, float precio, int stock, int id, String rutaImagen) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.id = id;
		this.rutaImagen = rutaImagen;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getRutaImagen() {
	    return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
	    this.rutaImagen = rutaImagen;
	}
	
	

}
