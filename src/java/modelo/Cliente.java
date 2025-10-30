package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.ConexionDB;

public class Cliente {
    private int id_cliente;
    private String nit, nombres, apellidos, direccion, telefono, fecha_nacimiento;

    ConexionDB cn = new ConexionDB();

    // Constructores
    public Cliente() {}
    public Cliente(int id, String n, String nom, String ape, String dir, String tel, String fn) {
        id_cliente = id;
        nit = n;
        nombres = nom;
        apellidos = ape;
        direccion = dir;
        telefono = tel;
        fecha_nacimiento = fn;
    }

    // Getters y setters
    public int getId_cliente() { return id_cliente; }
    public void setId_cliente(int id_cliente) { this.id_cliente = id_cliente; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getFecha_nacimiento() { return fecha_nacimiento; }
    public void setFecha_nacimiento(String fecha_nacimiento) { this.fecha_nacimiento = fecha_nacimiento; }

    // ======================================================
    // CRUD
    // ======================================================

    public List<Cliente> leer() {
        List<Cliente> lista = new ArrayList<>();
        try {
            Connection conn = cn.getConexion();
            String query = "SELECT * FROM clientes;";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nit"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("fecha_nacimiento")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al leer clientes: " + e.getMessage());
        }
        return lista;
    }

    public boolean agregar() {
        try {
            Connection conn = cn.getConexion();
            String query = "INSERT INTO clientes (nit, nombres, apellidos, direccion, telefono, fecha_nacimiento) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, nit);
            ps.setString(2, nombres);
            ps.setString(3, apellidos);
            ps.setString(4, direccion);
            ps.setString(5, telefono);
            ps.setString(6, fecha_nacimiento);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al agregar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar() {
        try {
            Connection conn = cn.getConexion();
            String query = "UPDATE clientes SET nit=?, nombres=?, apellidos=?, direccion=?, telefono=?, fecha_nacimiento=? WHERE id_cliente=?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, nit);
            ps.setString(2, nombres);
            ps.setString(3, apellidos);
            ps.setString(4, direccion);
            ps.setString(5, telefono);
            ps.setString(6, fecha_nacimiento);
            ps.setInt(7, id_cliente);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar() {
        try {
            Connection conn = cn.getConexion();
            String query = "DELETE FROM clientes WHERE id_cliente=?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id_cliente);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
}
