package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/proyectofinal";
    private static final String USER = "root";
    private static final String PASSWORD = "alexmo_cY2001"; // tu contraseña real

    // ✅ Método correcto para obtener conexión
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("❌ Error: No se encontró el Driver MySQL JDBC.", e);
        }
    }

    // Constructor opcional (ya no lo necesitas realmente)
    public ConexionDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: No se encontró el driver MySQL JDBC.");
        }
    }

    // Si tu código anterior usa getConexion(), dejamos un alias compatible:
    public Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Cerrar conexiones de forma segura
    public static void cerrar(Connection c) {
        if (c != null) {
            try {
                if (!c.isClosed()) c.close();
            } catch (SQLException e) {
                System.err.println("⚠️ Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
