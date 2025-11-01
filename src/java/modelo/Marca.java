package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import utils.ConexionDB;

public class Marca {
    private int id_marca;
    private String marca;
    private ConexionDB cn;
    
    public Marca() {
        cn = new ConexionDB();
    }
    
    public Marca(int id_marca, String marca) {
        this.id_marca = id_marca;
        this.marca = marca;
    }
    
    // Getters y Setters
    public int getId_marca() { return id_marca; }
    public void setId_marca(int id_marca) { this.id_marca = id_marca; }
    
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    
    public HashMap<String, String> insertar() {
        HashMap<String, String> resultado = new HashMap<>();
        try {
            String sql = "INSERT INTO marcas(marca) VALUES(?)";
            PreparedStatement stmt = cn.getConexion().prepareStatement(sql);
            stmt.setString(1, getMarca());
            stmt.executeUpdate();
            resultado.put("resultado", "1");
            resultado.put("mensaje", "Marca agregada correctamente");
        } catch (SQLException e) {
            resultado.put("resultado", "0");
            resultado.put("mensaje", "Error: " + e.getMessage());
        }
        return resultado;
    }
    
    public HashMap<String, String> actualizar() {
        HashMap<String, String> resultado = new HashMap<>();
        try {
            String sql = "UPDATE marcas SET marca=? WHERE id_marca=?";
            PreparedStatement stmt = cn.getConexion().prepareStatement(sql);
            stmt.setString(1, getMarca());
            stmt.setInt(2, getId_marca());
            stmt.executeUpdate();
            resultado.put("resultado", "1");
            resultado.put("mensaje", "Marca actualizada correctamente");
        } catch (SQLException e) {
            resultado.put("resultado", "0");
            resultado.put("mensaje", "Error: " + e.getMessage());
        }
        return resultado;
    }
    
    public HashMap<String, String> eliminar() {
        HashMap<String, String> resultado = new HashMap<>();
        try {
            String sql = "DELETE FROM marcas WHERE id_marca = ?";
            PreparedStatement stmt = cn.getConexion().prepareStatement(sql);
            stmt.setInt(1, getId_marca());
            stmt.executeUpdate();
            resultado.put("resultado", "1");
            resultado.put("mensaje", "Marca eliminada correctamente");
        } catch (SQLException e) {
            resultado.put("resultado", "0");
            resultado.put("mensaje", "Error: " + e.getMessage());
        }
        return resultado;
    }
    
    public List<Marca> leer() {
        List<Marca> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM marcas ORDER BY marca";
            ResultSet rs = cn.getConexion().createStatement().executeQuery(sql);
            while(rs.next()) {
                Marca marca = new Marca();
                marca.setId_marca(rs.getInt("id_marca"));
                marca.setMarca(rs.getString("marca"));
                lista.add(marca);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return lista;
    }
}