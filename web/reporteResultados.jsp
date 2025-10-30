<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Venta" %>
<!DOCTYPE html>
<html>
<head>
    <title>Reporte de Ventas</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="p-4">

<!-- 🔹 Barra superior con botones -->
<div class="d-flex justify-content-between mb-4">
    <!-- Volver al menú principal -->
    <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-secondary">
        Volver al Menú Principal
    </a>

    <!-- Exportar a PDF -->
    <a href="<%= request.getContextPath() %>/ReporteVentasPDF?fechaInicio=<%= request.getParameter("fechaInicio") != null ? request.getParameter("fechaInicio") : "" %>&fechaFin=<%= request.getParameter("fechaFin") != null ? request.getParameter("fechaFin") : "" %>"
       target="_blank" class="btn btn-danger">
        📄 Exportar a PDF
    </a>
</div>

<h2 class="mb-4 text-center">Reporte de Ventas</h2>

<!-- 🔹 Tabla de ventas -->
<table class="table table-bordered table-striped shadow-sm">
    <thead class="table-dark text-center">
        <tr>
            <th>No. Factura</th>
            <th>Serie</th>
            <th>Fecha Venta</th>
            <th>Total (Q)</th>
        </tr>
    </thead>
    <tbody>
    <%
        List<Venta> ventas = (List<Venta>) request.getAttribute("ventas");
        if (ventas != null && !ventas.isEmpty()) {
            for (Venta v : ventas) {
    %>
        <tr>
            <td><%= v.getNo_factura() %></td>
            <td><%= v.getSerie() %></td>
            <td><%= v.getFecha_venta() %></td>
            <td class="text-end"><%= String.format("%.2f", v.getTotal()) %></td>
        </tr>
    <%
            }
        } else {
    %>
        <tr>
            <td colspan="4" class="text-center text-muted">No hay registros para mostrar</td>
        </tr>
    <%
        }
    %>
    </tbody>
</table>

<!-- 🔹 Alerta de ventas -->
<%
    String alerta = (String) request.getAttribute("alerta");
    if (alerta != null) {
%>
    <div class="alert alert-info text-center mt-4" style="font-size: 18px;">
        <%= alerta %>
    </div>
<%
    }
%>

<!-- 🔹 Gráfico de ventas (por cliente) -->
<div class="mt-5">
    <canvas id="graficoVentas" width="900" height="350"></canvas>
</div>

<script>
    const ctx = document.getElementById('graficoVentas').getContext('2d');

    const datos = [
        <% if (ventas != null) {
               for (Venta v : ventas) { %>
                   { cliente: "<%= v.getCliente() %>", total: <%= v.getTotal() %> },
        <%     }
           } %>
    ];

    const clientes = datos.map(d => d.cliente);
    const totales = datos.map(d => d.total);

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: clientes,
            datasets: [{
                label: 'Total de Ventas (Q)',
                data: totales,
                backgroundColor: totales.map(t =>
                    t > 250 ? 'rgba(75, 192, 75, 0.8)' :
                    t < 200 ? 'rgba(255, 99, 132, 0.8)' :
                              'rgba(54, 162, 235, 0.8)'
                ),
                barThickness: 20,
                borderRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                title: { 
                    display: true, 
                    text: 'Ventas por Cliente',
                    font: { size: 18 }
                },
                tooltip: { 
                    callbacks: { 
                        label: ctx => `Q ${ctx.parsed.y.toFixed(2)}`
                    }
                }
            },
            scales: {
                x: {
                    ticks: { 
                        autoSkip: false,
                        maxRotation: 45,
                        minRotation: 45,
                        font: { size: 10 }
                    }
                },
                y: { beginAtZero: true }
            }
        }
    });
</script>

</body>
</html>
