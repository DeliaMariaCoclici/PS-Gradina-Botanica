package org.example.Model.Repository;

import java.sql.*;

public class Repo {

    protected static final String DB_URL = "jdbc:mysql://localhost:3306/botanical_garden";
    protected static final String USER = "root";
    protected static final String PASSWORD = "Parolacontmysql1.";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    protected boolean executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DB ERROR] " + e.getMessage() + " | SQLState: " + e.getSQLState());
            return false;
        }
    }

    protected void executeInitScript(String sql) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("[INIT ERROR] " + e.getMessage() + " | SQLState: " + e.getSQLState());
        }
    }
}