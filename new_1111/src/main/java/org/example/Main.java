package org.example;

import java.sql.*;

public class Main {

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/botanical_garden";
    private static final String USER     = "root";
    private static final String PASSWORD = "Parolacontmysql1.";

    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("✔ Conectat la baza de date!\n");

            // ── Afișare Plante ─────────────────────────────────────────────────
            System.out.println("======= PLANTE =======");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM Planta")) {

                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println("ID: "       + rs.getInt("id")
                            + " | Denumire: "       + rs.getString("denumire")
                            + " | Tip: "            + rs.getString("tip")
                            + " | Specie: "         + rs.getString("specie")
                            + " | Carnivora: "      + (rs.getBoolean("planta_carnivora") ? "Da" : "Nu")
                            + " | Imagine: "        + rs.getString("imagini"));
                }
                if (!found) System.out.println("(nicio plantă în baza de date)");
            }

            // ── Afișare Exemplare ──────────────────────────────────────────────
            System.out.println("\n======= EXEMPLARE =======");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT e.id, e.zona, e.imagine, e.planta_id, p.denumire " +
                                 "FROM Exemplar e LEFT JOIN Planta p ON e.planta_id = p.id")) {

                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println("ID: "        + rs.getInt("id")
                            + " | Zona: "            + rs.getString("zona")
                            + " | Planta: "          + rs.getString("denumire")
                            + " | Imagine: "         + rs.getString("imagine"));
                }
                if (!found) System.out.println("(niciun exemplar în baza de date)");
            }

        } catch (SQLException e) {
            System.err.println("❌ Eroare: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
        }
    }
}