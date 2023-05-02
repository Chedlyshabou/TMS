package tms.tms;

import java.sql.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Task {
    private int id;
    private String name;

    public Task(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ObservableList<Task> getAllTasks() {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "username", "password");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tasks");
            while (rs.next()) {
                Task task = new Task(rs.getInt("id"), rs.getString("name"));
                tasks.add(task);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        return tasks;
    }

    public static void addTask(String name) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "username", "password");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO tasks (name) VALUES (?)");
            stmt.setString(1, name);
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
    }

    public static void updateTask(int id, String name) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "username", "password");
            PreparedStatement stmt = conn.prepareStatement("UPDATE tasks SET name = ? WHERE id = ?");
            stmt.setString(1, name);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
    }

    public static void deleteTask(int id) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "username", "password");
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM tasks WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
    }

    public ObservableList<Project> getProjects() {
        ObservableList<Project> projects = FXCollections.observableArrayList();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "username", "password");
            PreparedStatement stmt = conn.prepareStatement("SELECT p.* FROM projects p JOIN project_tasks pt ON p.id = pt.project_id WHERE pt.task_id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Project project = new Project(rs.getInt("id"), rs.getString("name"));
                projects.add(project);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        return projects;
    }
}