package controlador;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import modelo.Venta;
import modelo.VentaDAO;

@WebServlet("/ReporteVentasPDF")
public class ReporteVentasPDF extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");

        // ✅ Obtener lista de ventas
        List<Venta> ventas = VentaDAO.obtenerVentas(fechaInicio, fechaFin, null, null);

        // ✅ Configurar PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=reporte_ventas.pdf");

        try (OutputStream out = response.getOutputStream()) {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, out);
            doc.open();

            // 🧾 Encabezado
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 11);
            Font totalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLDITALIC);

            Paragraph titulo = new Paragraph("Reporte de Ventas", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            doc.add(titulo);

            String fechaActual = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
            Paragraph fecha = new Paragraph("Generado el: " + fechaActual, new Font(Font.FontFamily.HELVETICA, 10));
            fecha.setAlignment(Element.ALIGN_RIGHT);
            fecha.setSpacingAfter(15);
            doc.add(fecha);

            // 📋 Tabla
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{3, 2, 3, 2});

            tabla.addCell(new PdfPCell(new Phrase("No. Factura", headerFont)));
            tabla.addCell(new PdfPCell(new Phrase("Serie", headerFont)));
            tabla.addCell(new PdfPCell(new Phrase("Fecha Venta", headerFont)));
            tabla.addCell(new PdfPCell(new Phrase("Total (Q)", headerFont)));

            double totalGeneral = 0;
            for (Venta v : ventas) {
                tabla.addCell(new PdfPCell(new Phrase(v.getNo_factura(), bodyFont)));
                tabla.addCell(new PdfPCell(new Phrase(v.getSerie(), bodyFont)));
                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(v.getFecha_venta()), bodyFont)));
                tabla.addCell(new PdfPCell(new Phrase(String.format("%.2f", v.getTotal()), bodyFont)));
                totalGeneral += v.getTotal();
            }

            doc.add(tabla);

            // 💰 Total general
            Paragraph total = new Paragraph("\nTotal General: Q" + String.format("%.2f", totalGeneral), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            total.setSpacingBefore(15);
            doc.add(total);

            doc.close();
        } catch (DocumentException e) {
            throw new IOException("Error al generar el PDF: " + e.getMessage());
        }
    }
}
