package controlador;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import modelo.Venta;
import modelo.VentaDAO;

@WebServlet("/ReporteServlet")
public class ReporteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String cliente = request.getParameter("cliente");
        String empleado = request.getParameter("empleado");

        // Llamada al modelo
        List<Venta> ventas = VentaDAO.obtenerVentas(fechaInicio, fechaFin, cliente, empleado);

        // Calcular total y alertas
        double totalVentas = ventas.stream().mapToDouble(Venta::getTotal).sum();
        double promedioHistorico = 250;
        double umbral = 0.2;
        String alerta;

        if (totalVentas > promedioHistorico * (1 + umbral)) {
            alerta = "🔔 Alerta: Venta alta";
        } else if (totalVentas < promedioHistorico * (1 - umbral)) {
            alerta = "⚠️ Alerta: Venta baja";
        } else {
            alerta = "Ventas dentro del promedio";
        }

        // Enviar datos al JSP
        request.setAttribute("ventas", ventas);
        request.setAttribute("alerta", alerta);
        request.getRequestDispatcher("reporteResultados.jsp").forward(request, response);
    }
}
