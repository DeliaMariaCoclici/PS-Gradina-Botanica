package org.example.Model.Repository;

import org.example.Model.Exemplar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExemplarRepo extends Repo {

    public ExemplarRepo() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS Exemplar (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "zona VARCHAR(255) NOT NULL, " +
                "imagine VARCHAR(255), " +
                "planta_id INT, " +
                "FOREIGN KEY (planta_id) REFERENCES Planta(id) ON DELETE CASCADE)";
        executeInitScript(sql);
    }

    // ── Mapare ResultSet → Exemplar ────────────────────────────────────────────
    private Exemplar mapRow(ResultSet rs) throws SQLException {
        return new Exemplar(
                rs.getInt("id"),
                rs.getString("zona"),
                rs.getString("imagine"),
                rs.getInt("planta_id"),
                rs.getString("denumire")
        );
    }

    // ── CRUD ───────────────────────────────────────────────────────────────────
    public List<Exemplar> getAllExemplare() {
        List<Exemplar> list = new ArrayList<>();
        String sql = "SELECT e.id, e.zona, e.imagine, e.planta_id, p.denumire " +
                "FROM Exemplar e LEFT JOIN Planta p ON e.planta_id = p.id";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[getAllExemplare] " + e.getMessage());
        }
        return list;
    }

    public boolean addExemplar(Exemplar e) {
        String sql = "INSERT INTO Exemplar (zona, imagine, planta_id) VALUES (?, ?, ?)";
        return executeUpdate(sql, e.getZona(), e.getImagine(), e.getPlantaId());
    }

    public boolean updateExemplar(Exemplar e) {
        String sql = "UPDATE Exemplar SET zona=?, imagine=?, planta_id=? WHERE id=?";
        return executeUpdate(sql, e.getZona(), e.getImagine(), e.getPlantaId(), e.getId());
    }

    public boolean deleteExemplar(int id) {
        return executeUpdate("DELETE FROM Exemplar WHERE id=?", id);
    }

    // ── Căutare după specie ────────────────────────────────────────────────────
    public List<Exemplar> findBySpecie(String specie) {
        List<Exemplar> list = new ArrayList<>();
        String sql = "SELECT e.id, e.zona, e.imagine, e.planta_id, p.denumire " +
                "FROM Exemplar e JOIN Planta p ON e.planta_id = p.id " +
                "WHERE p.specie = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, specie);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[findBySpecie] " + e.getMessage());
        }
        return list;
    }
}