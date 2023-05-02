package tms.tms;
import java.sql.*;
import java.util.ArrayList;

public class ProjectTeams {

        private Connection conn;

        // Constructor
        public ProjectTeams() {
            try {
                // Connect to the database
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Globals.getDb_name(), Globals.getDb_username(), Globals.getDb_pass());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Get projects for a given team
        public void getProjectsForTeam(Team team) {
            try {
                // Prepare the SQL statement
                ArrayList<Project> projects = new ArrayList<>();
                String sql = "SELECT * FROM projects " +
                        "INNER JOIN project_teams ON projects.id = project_teams.project_id " +
                        "WHERE project_teams.team_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, team.getId());

                // Execute the SQL statement
                ResultSet rs = stmt.executeQuery();

                // Process the results
                while (rs.next()) {
                    String name = rs.getString("name");
                    int id = rs.getInt("id");
                    projects.add(new Project(id, name));
                }

                // Close the statement and result set
                stmt.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Get teams for a given project
        public void getTeamsForProject(Project project) {
            try {
                // Prepare the SQL statement
                ArrayList<Team> teams = new ArrayList<>();
                String sql = "SELECT * FROM teams " +
                        "INNER JOIN project_teams ON teams.id = project_teams.team_id " +
                        "WHERE project_teams.project_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, project.getId());
                // Execute the SQL statement
                ResultSet rs = stmt.executeQuery();
                // Process the results
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String teamName = rs.getString("name");
                    String iconFilePath = rs.getString("icon_file_path");
                    String description = rs.getString("description");
                    Team team = new Team(id, teamName, iconFilePath, description);
                    teams.add(team);
                }

                // Close the statement and result set
                stmt.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


