package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.ConexionDB;

public class VentaDAO {

    // Método para obtener ventas según filtros
    public static List<Venta> obtenerVentas(String fechaInicio, String fechaFin, String cliente, String empleado) {
        List<Venta> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT v.id_venta, v.no_factura, v.serie, v.fecha_venta, v.id_cliente, v.id_empleado, v.total, ")
           .append("COALESCE(c.nombres, '') AS cliente, COALESCE(e.nombres, '') AS empleado ")
           .append("FROM ventas v ")
           .append("LEFT JOIN clientes c ON v.id_cliente = c.id_cliente ")
           .append("LEFT JOIN empleados e ON v.id_empleado = e.id_empleado ")
           .append("WHERE 1=1 ");

        // Agregar filtros dinámicos según parámetros
        if (fechaInicio != null && !fechaInicio.isEmpty() && fechaFin != null && !fechaFin.isEmpty()) {
            sql.append("AND DATE(v.fecha_venta) BETWEEN ? AND ? ");
        }
        if (cliente != null && !cliente.isEmpty()) {
            sql.append("AND c.nombres LIKE ? ");
        }
        if (empleado != null && !empleado.isEmpty()) {
            sql.append("AND e.nombres LIKE ? ");
        }

        sql.append("ORDER BY v.fecha_venta DESC");

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;

            if (fechaInicio != null && !fechaInicio.isEmpty() && fechaFin != null && !fechaFin.isEmpty()) {
                ps.setString(index++, fechaInicio);
                ps.setString(index++, fechaFin);
            }
            if (cliente != null && !cliente.isEmpty()) {
                ps.setString(index++, "%" + cliente + "%");
            }
            if (empleado != null && !empleado.isEmpty()) {
                ps.setString(index++, "%" + empleado + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Venta v = new Venta();
                    v.setId_venta(rs.getInt("id_venta"));
                    v.setNo_factura(rs.getString("no_factura"));
                    v.setSerie(rs.getString("serie"));
                    v.setFecha_venta(rs.getTimestamp("fecha_venta"));
                    v.setId_cliente(rs.getInt("id_cliente"));
                    v.setId_empleado(rs.getInt("id_empleado"));
                    v.setTotal(rs.getDouble("total"));
                    v.setCliente(rs.getString("cliente"));
                    v.setEmpleado(rs.getString("empleado"));
                    lista.add(v);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al obtener ventas: " + e.getMessage());
        }

        return lista;
    }
}
