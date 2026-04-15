package com.nextword.backend.feature.user.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.nextword.backend.feature.user.dto.request.admin.FinancialReportResponse;
import com.nextword.backend.feature.user.dto.request.admin.TransactionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.Color; // 🌟 Necesario para los colores
import java.io.IOException;
import java.time.LocalDate;

@Service
public class PdfExportService {

    public void export(HttpServletResponse response, FinancialReportResponse data) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // 🎨 COLORES DE TU MARCA
        Color primaryBlue = new Color(24, 95, 165); // El azul de NextWord
        Color successGreen = new Color(59, 109, 17); // Verde de ganancias
        Color tableHeaderBg = new Color(243, 244, 246); // Gris clarito para cabeceras
        Color borderColor = new Color(229, 231, 235); // Gris muy suave para líneas

        // 1. LOGO MEJORADO
        try {
            Image logo = Image.getInstance(getClass().getResource("/NexWordLogo.jpg"));
            logo.scaleToFit(120, 120); // 🌟 Más pequeñito y proporcionado
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            System.out.println("⚠️ Sin logo.");
        }

        // 2. TÍTULOS CON ESTILO
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, primaryBlue);
        Paragraph title = new Paragraph("Reporte Financiero - NextWord", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(10);
        document.add(title);

        Font fontDate = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
        Paragraph date = new Paragraph("Generado el: " + LocalDate.now(), fontDate);
        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(20); // 🌟 Espacio antes de la tabla
        document.add(date);

        // 3. LA TABLA ELEGANTE
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3f, 3f, 2f, 2f}); // 🌟 Proporciones de las columnas

        // Fuentes para la tabla
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.DARK_GRAY);
        Font fontCell = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        Font fontMoney = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, successGreen);

        // Nombres de las columnas
        String[] headers = {"Concepto", "Alumno", "Fecha", "Monto"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, fontHeader));
            cell.setBackgroundColor(tableHeaderBg);
            cell.setPadding(10f); // 🌟 Espacio interior para que no se vea apretado
            cell.setBorderColor(borderColor);
            table.addCell(cell);
        }

        // Llenar datos con estilo
        for (TransactionResponse tx : data.recentTransactions()) {
            // Celda Concepto
            table.addCell(createCell(tx.topic(), fontCell, borderColor));

            // Celda Alumno
            table.addCell(createCell(tx.studentName(), fontCell, borderColor));

            // Celda Fecha
            table.addCell(createCell(tx.date(), fontCell, borderColor));

            // Celda Monto (Con color verde)
            table.addCell(createCell("+$" + tx.amount(), fontMoney, borderColor));
        }

        document.add(table);

        // Pie de página
        Font fontFooter = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.GRAY);
        Paragraph footer = new Paragraph("\nDocumento generado por el sistema de administración NextWord.", fontFooter);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
    }

    // 🌟 Función auxiliar para crear celdas bonitas sin repetir tanto código
    private PdfPCell createCell(String text, Font font, Color borderColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8f);
        cell.setBorderColor(borderColor);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
}
