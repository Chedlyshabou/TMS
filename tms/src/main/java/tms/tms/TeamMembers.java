package tms.tms;
import java.sql.*;
import java.util.ArrayList;

public class TeamMembers {
    // Database connection details
    static private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Globals.getDb_name(), Globals.getDb_username(), Globals.getDb_pass());
    }

    // Get all team members for a given team ID
    public static ArrayList<User> getMembersForTeam(Team team) throws SQLException {
        ArrayList<User> members = new ArrayList<>();
        String sql = "SELECT * FROM team_members JOIN members ON team_members.member_id = members.id WHERE team_id = ?";
        PreparedStatement stmt = connect().prepareStatement(sql);
        stmt.setInt(1, team.getId());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int memberId = rs.getInt("id");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            Date birthday = rs.getDate("birthday");
            String email = rs.getString("email");
            String pass = rs.getString("pass");
            members.add(new User(name, surname, birthday, email, pass));
        }
        rs.close();
        stmt.close();
        connect().close();
        return members;
    }

    // Get all teams for a given member ID
    public static ArrayList<Team> getTeamsForMember(int memberId) throws SQLException {
        ArrayList<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM team_members JOIN teams ON team_members.team_id = teams.id WHERE member_id = ?";
        try (PreparedStatement statement = connect().prepareStatement(query)) {
            statement.setInt(1, memberId);
            ResultSet resultSet = statement.executeQuery();
            // add each team to the list
            while (resultSet.next()) {
                int teamId = resultSet.getInt("id");
                String teamName = resultSet.getString("name");
                String iconFilePath = resultSet.getString("icon_file_path");
                String description = resultSet.getString("description");
                teams.add(new Team(teamId, teamName, iconFilePath, description));
            }
            resultSet.close();
            statement.close();
            connect().close();
            return teams;
        }
    }
    public static int getOwnershipForMember(int memberId) throws SQLException {
        String query = "SELECT owner FROM team_members JOIN teams ON team_members.team_id = teams.id WHERE member_id = ?";
        try (PreparedStatement statement = connect().prepareStatement(query)) {
            statement.setInt(1, memberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int ownerId = resultSet.getInt("owner");
                    return ownerId;
                } else {
                    return 0;
                }
            }
        }
    }
}