package org.example.Model.Repository;

import org.example.Model.Planta;
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
                "denumire VARCHAR(255), " +
                "tip VARCHAR(255), " +
                "specie VARCHAR(255), " +
                "planta_carnivora BOOLEAN, " +
                "imagini VARCHAR(255))";
        executeInitScript(sql);
    }

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

    public List<Planta> getAllPlants() {
        List<Planta> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement s = c.prepareStatement("SELECT * FROM Planta");
             ResultSet rs = s.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[getAllPlants] " + e.getMessage());
        }
        return list;
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

    public List<Planta> filterPlants(String tip, String specie, Boolean carnivora) {
        List<Planta> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Planta WHERE 1=1 ");
        if (tip != null && !tip.isEmpty()) sql.append("AND tip=? ");
        if (specie != null && !specie.isEmpty()) sql.append("AND specie=? ");
        if (carnivora != null) sql.append("AND planta_carnivora=? ");
        try (Connection c = getConnection();
             PreparedStatement s = c.prepareStatement(sql.toString())) {
            int i = 1;
            if (tip != null && !tip.isEmpty()) s.setString(i++, tip);
            if (specie != null && !specie.isEmpty()) s.setString(i++, specie);
            if (carnivora != null) s.setBoolean(i++, carnivora);
            ResultSet rs = s.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[filterPlants] " + e.getMessage());
        }
        return list;
    }

    public List<Planta> getPlantsSortedBy(String c) {
        String field = switch (c.toLowerCase()) {
            case "tip" -> "tip";
            case "specie" -> "specie";
            default -> "denumire";
        };
        List<Planta> list = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement s = conn.createStatement();
             ResultSet rs = s.executeQuery("SELECT * FROM Planta ORDER BY " + field)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[sort] " + e.getMessage());
        }
        return list;
    }
}
