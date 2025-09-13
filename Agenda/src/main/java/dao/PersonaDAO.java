package dao;

import data.Conexion;
import modelo.Persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    public List<Persona> listar() {
        List<Persona> lista = new ArrayList<>();
        String sqlPersonas = "SELECT id, nombre, direccion FROM Personas ORDER BY id";
        try (Connection conn = Conexion.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sqlPersonas)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String direccion = rs.getString("direccion");

                List<String> telefonos = new ArrayList<>();
                try (PreparedStatement pst = conn.prepareStatement("SELECT telefono FROM Telefonos WHERE personaId=? ORDER BY id")) {
                    pst.setInt(1, id);
                    try (ResultSet rt = pst.executeQuery()) {
                        while (rt.next()) {
                            telefonos.add(rt.getString("telefono"));
                        }
                    }
                }
                lista.add(new Persona(id, nombre, direccion, telefonos));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void agregar(String nombre, String direccion, List<String> telefonos) {
        String sqlInsert = "INSERT INTO Personas(nombre, direccion) VALUES(?, ?)";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int personaId = keys.getInt(1);
                    insertarTelefonos(conn, personaId, telefonos);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarTelefonos(Connection conn, int personaId, List<String> telefonos) throws SQLException {
        if (telefonos == null) {
            return;
        }
        String sqlTel = "INSERT INTO Telefonos(personaId, telefono) VALUES(?, ?)";
        for (String t : telefonos) {
            String tel = t.trim();
            if (tel.isEmpty()) {
                continue;
            }
            try (PreparedStatement pst = conn.prepareStatement(sqlTel)) {
                pst.setInt(1, personaId);
                pst.setString(2, tel);
                pst.executeUpdate();
            }
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Personas WHERE id=?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0; // true si eliminó algo
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modificar(int id, String nombre, String direccion, List<String> telefonos) {
        String sqlUpdate = "UPDATE Personas SET nombre=?, direccion=? WHERE id=?";
        String sqlDeleteTel = "DELETE FROM Telefonos WHERE personaId=?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlUpdate); PreparedStatement psDel = conn.prepareStatement(sqlDeleteTel)) {

            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.setInt(3, id);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return false; // no existe
            }

            psDel.setInt(1, id);
            psDel.executeUpdate();

            insertarTelefonos(conn, id, telefonos);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
