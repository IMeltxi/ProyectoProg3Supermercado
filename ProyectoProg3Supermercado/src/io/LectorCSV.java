package io;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import domain.Productos;

public class LectorCSV {

    public static List<Productos> cargarProductos(String rutaCsv) {
        List<Productos> productos = new ArrayList<>();

        try {
            List<String> lineas = Files.readAllLines(Paths.get(rutaCsv));

            // Saltar la cabecera
            for (int i = 1; i < lineas.size(); i++) {
                String[] datos = lineas.get(i).split(";");
                if (datos.length < 5) continue;

                String nombre = datos[0].replace("\"", "");
                String descripcion = datos[1].replace("\"", "");
                float precio = Float.parseFloat(datos[2]);
                int stock = Integer.parseInt(datos[3]);
                int id = Integer.parseInt(datos[4]);
                String ruta = datos[5];

                productos.add(new Productos(nombre, descripcion, precio, stock, id, ruta));
            }
        } catch (Exception e) {
            System.out.println("❌ Error leyendo CSV: " + e.getMessage());
        }

        return productos;
    }
}


