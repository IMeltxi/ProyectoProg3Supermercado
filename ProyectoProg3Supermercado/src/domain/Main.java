package domain;
import db.*;
import java.util.ArrayList;
import java.util.List;

import gui.IniciarSesion;

public class Main {
    public static void main(String[] args) {
    	
    	BD.conectar();
    	
        // --- Productos ---
        List<Productos> productos = new ArrayList<>();
        productos.add(new Productos("Laptop", "Portátil Gaming", 1200.50f, 10));
        productos.add(new Productos("Smartphone", "Teléfono Android", 350.99f, 25));
        productos.add(new Productos("Auriculares", "Bluetooth Noise Cancelling", 89.99f, 50));
        productos.add(new Productos("Teclado", "Mechanical RGB", 69.50f, 30));
        int idProd = 1;
        for (Productos p : productos) {
            p.setId(idProd++);
        }

        // --- Clientes ---
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente(Cliente.tipoCliente.PREMIUM, 1, "Juan", "Pérez", "1990-05-12", "juan@mail.com", "1234", 500));
        clientes.add(new Cliente(Cliente.tipoCliente.NORMAL, 2, "Ana", "García", "1985-11-02", "ana@mail.com", "abcd", 200));
        clientes.add(new Cliente(Cliente.tipoCliente.INVITADO, 3, "Luis", "Martínez", "2000-01-22", "luis@mail.com", "pass", 0));
        clientes.add(new Cliente(Cliente.tipoCliente.PREMIUM, 4, "María", "Rodríguez", "1995-07-30", "maria@mail.com", "qwer", 350));

        // --- Abrir ventana de login y pasar los datos ---
        javax.swing.SwingUtilities.invokeLater(() -> {
            new IniciarSesion(clientes, productos);
        });
    }
}
