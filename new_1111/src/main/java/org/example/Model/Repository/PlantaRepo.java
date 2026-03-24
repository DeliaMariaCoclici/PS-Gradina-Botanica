package org.example.Model.Repository;

import org.example.Model.Planta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlantaRepo extends Repo {

    public PlantaRepo() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS Planta (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "denumire VARCHAR(255) NOT NULL, " +
                "tip VARCHAR(255) NOT NULL, " +
                "specie VARCHAR(255) NOT NULL, " +
                "planta_carnivora BOOLEAN NOT NULL, " +
                "imagini VARCHAR(255))";
        executeInitScript(sql);
    }

    // ── Mapare ResultSet → Planta ──────────────────────────────────────────────
    private Planta mapRow(ResultSet rs) throws SQLException {
        return new Planta(
                rs.getInt("id"),
                rs.getString("denumire"),
                rs.getString("tip"),
                rs.getString("specie"),
                rs.getBoolean("planta_carnivora"),
                rs.getString("imagini")
        );
    }

    // ── CRUD ───────────────────────────────────────────────────────────────────
    public List<Planta> getAllPlants() {
        List<Planta> plante = new ArrayList<>();
        String sql = "SELECT * FROM Planta";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) plante.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[getAllPlants] " + e.getMessage());
        }
        return plante;
    }

    public boolean addPlant(Planta p) {
        String sql = "INSERT INTO Planta (denumire, tip, specie, planta_carnivora, imagini) VALUES (?, ?, ?, ?, ?)";
        return executeUpdate(sql, p.getDenumire(), p.getTip(), p.getSpecie(), p.isPlantaCarnivora(), p.getImagePath());
    }

    public boolean updatePlant(Planta p) {
        String sql = "UPDATE Planta SET denumire=?, tip=?, specie=?, planta_carnivora=?, imagini=? WHERE id=?";
        return executeUpdate(sql, p.getDenumire(), p.getTip(), p.getSpecie(), p.isPlantaCarnivora(), p.getImagePath(), p.getId());
    }

    public boolean deletePlant(int id) {
        return executeUpdate("DELETE FROM Planta WHERE id=?", id);
    }

    // ── Sortare ────────────────────────────────────────────────────────────────
    public List<Planta> getPlantsSortedBy(String criteria) {
        List<Planta> plante = new ArrayList<>();
        String orderBy = switch (criteria.toLowerCase()) {
            case "tip"     -> "tip ASC, specie ASC";
            case "specie"  -> "specie ASC, tip ASC";
            case "denumire"-> "denumire ASC";
            default        -> "id ASC";
        };
        String sql = "SELECT * FROM Planta ORDER BY " + orderBy;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) plante.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[getPlantsSortedBy] " + e.getMessage());
        }
        return plante;
    }

    // ── Filtrare ───────────────────────────────────────────────────────────────
    public List<Planta> filterPlants(String tip, String specie, Boolean carnivora) {
        List<Planta> plante = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Planta WHERE 1=1");

        if (tip != null && !tip.isEmpty())        sql.append(" AND tip = ?");
        if (specie != null && !specie.isEmpty())  sql.append(" AND specie = ?");
        if (carnivora != null)                    sql.append(" AND planta_carnivora = ?");

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (tip != null && !tip.isEmpty())       stmt.setString(idx++, tip);
            if (specie != null && !specie.isEmpty()) stmt.setString(idx++, specie);
            if (carnivora != null)                   stmt.setBoolean(idx++, carnivora);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) plante.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[filterPlants] " + e.getMessage());
        }
        return plante;
    }
}