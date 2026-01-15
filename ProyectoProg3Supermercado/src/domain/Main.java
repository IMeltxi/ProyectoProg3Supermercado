package domain;
import db.*;
import java.util.ArrayList;
import java.util.List;


import gui.VentanaCargando;

public class Main {
    public static void main(String[] args) {
    	
    	BD.conectar();
    	
    	BD.crearTablas();
    	
    	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Cerrando aplicación y base de datos...");
            BD.desconectar();
        }));
    	
        // --- Productos ---
    	/*
        List<Productos> productos = new ArrayList<>();
        productos.add(new Productos("Laptop", "Portátil Gaming", 1200.50f, 10));
        productos.add(new Productos("Smartphone", "Teléfono Android", 350.99f, 25));
        productos.add(new Productos("Auriculares", "Bluetooth Noise Cancelling", 89.99f, 50));
        productos.add(new Productos("Teclado", "Mechanical RGB", 69.50f, 30));
        
        int idProd = 1;
        for (Productos p : productos) {
            p.setId(idProd++);
        }
		*/
    	
        // --- Clientes ---
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente(Cliente.tipoCliente.PREMIUM, 1, "Juan", "Pérez", "1990-05-12", "juan@mail.com", "1234", 500));
        clientes.add(new Cliente(Cliente.tipoCliente.NORMAL, 2, "Ana", "García", "1985-11-02", "ana@mail.com", "abcd", 200));
        clientes.add(new Cliente(Cliente.tipoCliente.INVITADO, 3, "Luis", "Martínez", "2000-01-22", "luis@mail.com", "pass", 0));
        clientes.add(new Cliente(Cliente.tipoCliente.PREMIUM, 4, "María", "Rodríguez", "1995-07-30", "maria@mail.com", "qwer", 350));

        // --- Abrir ventana de login y pasar los datos ---
        javax.swing.SwingUtilities.invokeLater(() -> {
            //new IniciarSesion(clientes, productos);
        	new VentanaCargando();
        });
        
        ////////////

     // Insertar nueva compra
//     int idCompra = BD.insertarCompra(1, 150.75, "COMPLETADA");
//
//     // Insertar productos en la compra
//     Productos prod1 = new Productos(); // Asegúrate de inicializar con ID y precio
//     prod1.setId(2);
//     prod1.setPrecio(50.25f);
//     BD.insertarDetalleCompra(idCompra, prod1, 2);
//
//     Productos prod2 = new Productos();
//     prod2.setId(5);
//     prod2.setPrecio(25.25f);
//     BD.insertarDetalleCompra(idCompra, prod2, 2);
//
//     // Consultar historial
//     List<String> historial = BD.historialComprasCliente(1);
//     historial.forEach(System.out::println);

     
     // =========================================================================
     		// --- INICIO: TEST DE PUNTOS RECURSIVOS (CANTIDAD EN COMPRA ACTUAL) ---
     		// =========================================================================

     		System.out.println("\n--- PRUEBA DE LÓGICA RECURSIVA (Dominio de Producto) ---");

     		// 1. Catálogo de productos disponibles (necesario para saber los precios)
     		// Constructor: nombre, descripcion, precio, stock, id, rutaImagen
     		List<Productos> catalogoProductos = new ArrayList<>();
     		Productos pLaptop = new Productos("Laptop Gamer", "Alta gama", 1000.0f, 10, 1, "n/a");
     		Productos pRaton = new Productos("Raton Basico", "Oficina", 5.0f, 100, 2, "n/a"); // < 10 euros
     		
     		catalogoProductos.add(pLaptop);
     		catalogoProductos.add(pRaton);

     		// 2. Crear una Compra nueva (Carrito)
     		Compra compraActual = new Compra();
     		
     		// 3. Llenamos el carrito
     		// CASO A: Compramos 3 Laptops. 
     		// La recursividad debería hacer: 
     		//  - 1ª Laptop: 1000 * 1.0 = 1000
     		//  - 2ª Laptop: 1000 * 1.3 = 1300
     		//  - 3ª Laptop: 1000 * 1.6 = 1600
     		//  Total esperado: 3900 puntos.
     		compraActual.agregarProducto("Laptop Gamer", 3);
     		
     		// CASO B: Compramos 10 Ratones.
     		// Precio 5.0 (< 10). La recursividad debería ignorarlos.
     		compraActual.agregarProducto("Raton Basico", 10);

     		// 4. Ejecutar el cálculo
     		System.out.println("Calculando puntos para:");
     		System.out.println(" - 3x Laptop Gamer (1000€ c/u)");
     		System.out.println(" - 10x Raton Basico (5€ c/u)");
     		
     		double puntosObtenidos = compraActual.calcularPuntos(catalogoProductos);

     		System.out.println("\nRESULTADO:");
     		System.out.println("Puntos Totales: " + puntosObtenidos);
     		
     		// Verificación visual
     		if (puntosObtenidos == 3900.0) {
     			System.out.println("✅ PRUEBA EXITOSA: El cálculo progresivo es correcto (1000 + 1300 + 1600).");
     		} else {
     			System.out.println("❌ ALGO FALLÓ: El resultado no coincide con el esperado.");
     		}
     		System.out.println("--------------------------------------------------------\n");

     		// =========================================================================
     		// --- FIN TEST ---
     		// =========================================================================       
     		
     
    }
}
