package io;

import domain.Cliente;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.awt.Desktop;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class FacturaPDF {

    public static void generarFactura(Cliente cliente, JTable tabla, File destino,double descuento) throws IOException {

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);

        float y = 770;
        float margin = 50;

        PDPageContentStream cs = new PDPageContentStream(doc, page);

        // ====== TÍTULO ======
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
        cs.newLineAtOffset(margin, y);
        cs.showText("FACTURA DE COMPRA");
        cs.endText();

        y -= 30;

        // ====== DATOS ======
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 11);
        cs.newLineAtOffset(margin, y);
        cs.showText("Fecha: " + LocalDate.now());
        cs.endText();

        y -= 15;

        cs.beginText();
        cs.newLineAtOffset(margin, y);
        cs.showText("Cliente: " + (cliente != null ? cliente.toString() : "No identificado"));
        cs.endText();

        y -= 30;

        // ====== CABECERA TABLA ======
        cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
        writeRow(cs, margin, y, "Producto", "Cantidad", "P.Unit", "Total");
        y -= 15;

        cs.setFont(PDType1Font.HELVETICA, 11);

        TableModel m = tabla.getModel();
        double totalCompra = 0.0;

        for (int i = 0; i < m.getRowCount(); i++) {
        	
        	if (m.getValueAt(i, 1) == null) continue;
        	
            String nombre = m.getValueAt(i, 1).toString();
            double cantidad = Double.parseDouble(m.getValueAt(i, 2).toString());
            double precio = Double.parseDouble(m.getValueAt(i, 3).toString());
            double total = Double.parseDouble(m.getValueAt(i, 4).toString());

            totalCompra += total;

            writeRow(cs, margin, y,
                    nombre,
                    String.valueOf((int)cantidad),
                    String.format("%.2f €", precio),
                    String.format("%.2f €", total));

            y -= 14;
        }

        y -= 20;

     // ====== TOTALES y DESCUENTOS ======
        // Subtotal (si hay descuento)
        if (descuento > 0) {
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLineAtOffset(margin, y);
            cs.showText("Subtotal: " + String.format("%.2f €", totalCompra));
            cs.endText();
            y -= 15;

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLineAtOffset(margin, y);
            cs.showText("Descuento (Puntos): -" + String.format("%.2f €", descuento));
            cs.endText();
            y -= 20;
        }
        //Total final
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 13);
        cs.newLineAtOffset(margin, y);
        cs.showText("TOTAL: " + String.format("%.2f €", totalCompra));
        cs.endText();

        cs.close();
        doc.save(destino);
        doc.close();

        // Abrir el PDF automáticamente
        try {
            Desktop.getDesktop().open(destino);
        } catch (Exception e) {}
    }

    private static void writeRow(PDPageContentStream cs, float x, float y,
                                 String c1, String c2, String c3, String c4) throws IOException {

        float[] colX = {0, 300, 380, 460};

        String[] data = {c1, c2, c3, c4};

        for (int i = 0; i < data.length; i++) {
            cs.beginText();
            cs.newLineAtOffset(x + colX[i], y);
            cs.showText(data[i]);
            cs.endText();
        }
    }
}
