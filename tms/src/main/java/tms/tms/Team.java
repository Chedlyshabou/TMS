package tms.tms;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Team {
    private int id;
    private String name;
    private String iconFilePath;
    private String description;

    public Team(int id, String name, String iconFilePath, String description) {
        this.id = id;
        this.name = name;
        this.iconFilePath = iconFilePath;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getIconFilePath() {
        return iconFilePath;
    }

    public String getDescription() {
        return description;
    }

    // Connect to the database
    static private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Globals.getDb_name(), Globals.getDb_username(), Globals.getDb_pass());
    }

    public static int getTeamIdByName(String name) throws SQLException {
        String query = "SELECT * FROM teams WHERE name = ?";
        try (PreparedStatement statement = connect().prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    return id;
                } else {
                    return 0;
                }
            }
        }
    }

    public static void createTeam(String name, String icon_file_path, String description) throws SQLException {
        if (getTeamIdByName(name) != 0) {
            throw new IllegalArgumentException("Team with name " + name + " already exists.");
        }
        String query = "INSERT INTO teams (name, icon_file_path, description) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connect().prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, icon_file_path);
            statement.setString(3, description);
            statement.executeUpdate();
        }
    }

    public static void updateTeam(String name, String icon_file_path, String description, int id) throws SQLException {
        String query = "UPDATE teams SET name = ?, icon_file_path = ?, description = ? WHERE id = ?";
        try (PreparedStatement statement = connect().prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, icon_file_path);
            statement.setString(3, description);
            statement.setInt(4, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("No team with ID " + id + " found.");
            }
        }
    }

    public static void deleteTeam(Team team) throws SQLException {
        String query = "DELETE FROM teams WHERE name = ?";
        try (PreparedStatement statement = connect().prepareStatement(query)) {
            statement.setString(1, team.getName());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("No team with name" + team.getName() + " found.");
            }
        }
    }
    public void setName(Team team, String newname) throws SQLException {
        if (getTeamIdByName(newname) != 0) {
            throw new IllegalArgumentException("Team with name " + newname + " already exists.");
        }
        String query = "UPDATE teams SET name = ? WHERE name = ?";
        PreparedStatement statement = connect().prepareStatement(query);
        statement.setString(1, newname);
        statement.setString(2, team.getName());
        statement.executeUpdate();
    }

    public static void addMember(String name, int id, int owner) throws SQLException {
        int teamId=getTeamIdByName(name);
        String query = "INSERT INTO team_members (team_id, member_id, owner) VALUES (?, ?, ?)";
        PreparedStatement statement = connect().prepareStatement(query);
        statement.setInt(1, teamId);
        statement.setInt(2, id);
        statement.setInt(3, owner);
        statement.executeUpdate();
    }

    public void removeMember(User member) throws SQLException {
        String query = "DELETE FROM team_members WHERE team_id = ? AND member_id = ?";
        PreparedStatement statement = connect().prepareStatement(query);
        statement.setInt(1, id);
        statement.setInt(2, member.getId());
        statement.executeUpdate();
    }

    public static ArrayList<Team> getAllTeams() {
        ArrayList<Team> teams = new ArrayList<>();

        try {
            Statement stmt = connect().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM teams");

            while (rs.next()) {
                int id = rs.getInt("id");
                String teamName = rs.getString("name");
                String iconFilePath = rs.getString("icon_file_path");
                String description = rs.getString("description");
                Team team = new Team(id, teamName, iconFilePath, description);
                teams.add(team);
            }

            rs.close();
            stmt.close();
            connect().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }

    public ArrayList<User> getMembers() {
        ArrayList<User> members = new ArrayList<>();

        try {
            PreparedStatement stmt = connect().prepareStatement("SELECT m.id, m.name, m.surname, m.birthday, m.email, m.pass FROM members m JOIN team_members tm ON m.id = tm.member_id WHERE tm.team_id = ?");
            stmt.setInt(1, this.id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                Date birthday = rs.getDate("birthday");
                String email = rs.getString("email");
                String pass = rs.getString("pass");
                User member = new User(name, surname, birthday, email, pass);
                members.add(member);
            }

            rs.close();
            stmt.close();
            connect().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    public List<Project> getProjects() throws SQLException {
        List<Project> projects = new ArrayList<>();

        String query = "SELECT p.* FROM projects p " +
                "JOIN project_teams pt ON p.id = pt.project_id " +
                "WHERE pt.team_id = ?";
        try (PreparedStatement stmt = connect().prepareStatement(query)) {
            stmt.setInt(1, this.id);
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Project project = new Project(id, name);
                    projects.add(project);
                }
            }
        }

        return projects;
    }

    public List<Task> getTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();

        String query = "SELECT t.* FROM tasks t " +
                "JOIN project_tasks pt ON t.id = pt.task_id " +
                "JOIN project_teams pte ON pt.project_id = pte.project_id " +
                "WHERE pte.team_id = ?";
        try (PreparedStatement stmt = connect().prepareStatement(query)) {
            stmt.setInt(1, this.id);
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Task task = new Task(id, name);
                    tasks.add(task);
                }
            }
        }

        return tasks;
    }
    public void addProject(Project project) throws SQLException {
        String query = "INSERT INTO project_teams (project_id, team_id) VALUES (?, ?)";
        PreparedStatement statement = connect().prepareStatement(query);
        statement.setInt(1, project.getId());
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    public void removeProject(Project project) throws SQLException {
        String query = "DELETE FROM project_teams WHERE project_id = ? AND team_id = ?";
        PreparedStatement statement = connect().prepareStatement(query);
        statement.setInt(1, project.getId());
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    public void addTask(Task task, User member) throws SQLException {
        String query = "INSERT INTO task_assignments (task_id, member_id, team_id) VALUES (?, ?, ?)";
        PreparedStatement statement = connect().prepareStatement(query);
        statement.setInt(1, task.getId());
        statement.setInt(2, member.getId());
        statement.setInt(3, id);
        statement.executeUpdate();
    }

    public void removeTask(Task task, User member) throws SQLException {
        String query = "DELETE FROM task_assignments WHERE task_id = ? AND member_id = ? AND team_id = ?";
        PreparedStatement statement = connect().prepareStatement(query);
        statement.setInt(1, task.getId());
        statement.setInt(2, member.getId());
        statement.setInt(3, id);
        statement.executeUpdate();
    }
}