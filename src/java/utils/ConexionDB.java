package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private Connection conexion;
    private final String url = "jdbc:mysql://localhost:3306/db_empresa?useSSL=false&serverTimezone=UTC";
    private final String usuario = "root";
    private final String contrasena = "Minato";

    public ConexionDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, usuario, contrasena);
            System.out.println("✅ Conectado a MySQL (localhost) correctamente.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("❌ Error al conectar: " + e.getMessage());
        }
    }

    public Connection getConexion() {
        return conexion;
    }
}
