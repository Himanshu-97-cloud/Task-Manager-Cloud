package com.example.taskapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private final String url = System.getenv("DB_URL");
    private final String user = System.getenv("DB_USER");
    private final String pass = System.getenv("DB_PASS");

    public TaskDAO() {
        try (Connection con = getConnection();
             Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS tasks (" +
                       "id SERIAL PRIMARY KEY, " +
                       "title VARCHAR(255) NOT NULL, " +
                       "status VARCHAR(50) DEFAULT 'Pending')");
        } catch (SQLException e) {
            System.out.println("DB Init Note: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    public void addTask(String title) throws SQLException {
        String sql = "INSERT INTO tasks (title, status) VALUES (?, 'Pending')";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.executeUpdate();
        }
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY id DESC";
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("status")
                ));
            }
        }
        return list;
    }

    public void markDone(int id) throws SQLException {
        String sql = "UPDATE tasks SET status = 'Completed' WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void deleteTask(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}