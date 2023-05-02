package tms.tms;
import java.sql.*;
import java.util.ArrayList;

public class Project {
    private int id;
    private String name;

    // Constructor
    public Project(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    static private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Globals.getDb_name(), Globals.getDb_username(), Globals.getDb_pass());
    }

    // SQL functions

    // Get all projects
    public static ArrayList<Project> getAllProjects() throws SQLException {
        ArrayList<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM projects";
        Statement stmt = connect().createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");

            Project project = new Project(id, name);
            projects.add(project);
        }

        return projects;
    }

    // Get teams assigned to this project
    public ArrayList<Team> getTeams() throws SQLException {
        ArrayList<Team> teams = new ArrayList<>();
        String query = "SELECT teams.* FROM teams " +
                "JOIN project_teams ON project_teams.team_id = teams.id " +
                "WHERE project_teams.project_id = ?";

        PreparedStatement pstmt = connect().prepareStatement(query);
        pstmt.setInt(1, this.id);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String teamName = rs.getString("name");
            String iconFilePath = rs.getString("icon_file_path");
            String description = rs.getString("description");
            Team team = new Team(id, teamName, iconFilePath, description);
            teams.add(team);
        }
        return teams;
    }

    // Get tasks assigned to this project
    public ArrayList<Task> getTasks() throws SQLException {
        ArrayList<Task> tasks = new ArrayList<>();
        String query = "SELECT tasks.* FROM tasks " +
                "JOIN project_tasks ON project_tasks.task_id = tasks.id " +
                "WHERE project_tasks.project_id = ?";

        PreparedStatement pstmt = connect().prepareStatement(query);

        pstmt.setInt(1, this.id);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");

            Task task = new Task(id, name);
            tasks.add(task);
        }
        return tasks;
    }

    // Create a new project
    public static void createProject(String name) throws SQLException {
        String query = "INSERT INTO projects (name) VALUES (?)";
        PreparedStatement pstmt = connect().prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.executeUpdate();
    }

    // Update project name
    public void updateProjectName(String newName) throws SQLException {
        String query = "UPDATE projects SET name = ? WHERE id = ?";
        PreparedStatement pstmt = connect().prepareStatement(query);
        pstmt.setString(1, newName);
        pstmt.setInt(2, this.id);
        pstmt.executeUpdate();
        this.name = newName;
    }

    public void deleteProject() throws SQLException {
        String query = "DELETE FROM projects WHERE id = ?";
        PreparedStatement pstmt = connect().prepareStatement(query);
        pstmt.setInt(1, this.id);
        pstmt.executeUpdate();
    }
}
