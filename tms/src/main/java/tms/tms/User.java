package tms.tms;

import java.sql.*;

public class User {
        private int id;
        private String name;
        private String surname;
        private Date birthday;
        private String email;
        private String password;



        // Getters and setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    private Connection connection;

    public User(String name, String surname, Date birthday, String email, String password) {
        try {
            // Load the JDBC driver and establish a connection to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Globals.getDb_name(), Globals.getDb_username(), Globals.getDb_pass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
    }

    public void addMember(String name, String surname, Date birthday, String email, String password) {
        try {
            // Check if member with the given email already exists in the database
            PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM members WHERE email = ?");
            checkStatement.setString(1, email);
            ResultSet checkResult = checkStatement.executeQuery();
            checkResult.next();
            int count = checkResult.getInt(1);
            if (count > 0) {

                // Member with given email already exists, do not add to database
                System.out.println("Error: Member with email " + email + " already exists in the database");
                return;
            }

            // If member does not already exist, add to database
            PreparedStatement addStatement = connection.prepareStatement("INSERT INTO members (name, surname, birthday, email, pass) VALUES (?, ?, ?, ?, ?)");
            System.out.println("reached here 0");
            addStatement.setString(1, name);
            addStatement.setString(2, surname);
            addStatement.setDate(3, birthday);
            addStatement.setString(4, email);
            addStatement.setString(5, password);
            System.out.println(name+surname+birthday+email+password);
            System.out.println("reached here first");
            addStatement.executeUpdate();
            System.out.println("reached here second");
            System.out.println("Member added to database: " + name + " " + surname);
            System.out.println("reached here third");
        } catch (SQLException e) {
            System.out.println("Error adding member to database: " + e.getMessage());
        }
    }

    // Function to update an existing member in the database
    public void updateMember(String name, String surname, Date birthday, String email, String password) {
        try {
            // Check if member with the given email exists in the database
            PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM members WHERE email = ?");
            checkStatement.setString(1, email);
            ResultSet checkResult = checkStatement.executeQuery();
            checkResult.next();
            int count = checkResult.getInt(1);
            if (count == 0) {
                // Member with given ID does not exist, cannot update
                System.out.println("Error: Member with email " + email + " does not exist in the database");
                return;
            }

            // If member exists, update database with new values
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE members SET name = ?, surname = ?, birthday = ?, email = ?, pass = ? WHERE email = ?");
            updateStatement.setString(1, name);
            updateStatement.setString(2, surname);
            updateStatement.setDate(3, birthday);
            updateStatement.setString(4, email);
            updateStatement.setString(5, password);
            updateStatement.setString(6, email);
            updateStatement.executeUpdate();
            System.out.println("Member updated in database: " + name + " " + surname);
        } catch (SQLException e) {
            System.out.println("Error updating member in database: " + e.getMessage());
        }
    }

    public void deleteMember(String email) {
        try {
            // Check if member with the given email exists in the database
            PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM members WHERE email = ?");
            checkStatement.setString(1, email);
            ResultSet checkResult = checkStatement.executeQuery();
            checkResult.next();
            int count = checkResult.getInt(1);
            if (count == 0) {
                // Member with given ID does not exist, cannot update
                System.out.println("Error: Member with email " + email + " does not exist in the database");
                return;
            }
            // Create a PreparedStatement to delete a member from the database
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM members WHERE email = ?");
            stmt.setString(1, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getMembers() {
        try {
            // Create a Statement to select all members from the database
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM members");
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMember(String email) {
        try {
            // Create a Statement to select all members from the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Globals.getDb_name(), Globals.getDb_username(), Globals.getDb_pass());
            PreparedStatement stmt = connection.prepareStatement("SELECT CONCAT(name, ' ', surname) AS fullname FROM members WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("fullname");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getMemberPassword(String email) {
        try {
            // Create a Statement to select all members from the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Globals.getDb_name(), Globals.getDb_username(), Globals.getDb_pass());
            PreparedStatement stmt = connection.prepareStatement("SELECT pass FROM members WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("pass");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static int getMemberId(String email) {
        try {
            // Create a Statement to select all members from the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Globals.getDb_name(), Globals.getDb_username(), Globals.getDb_pass());
            PreparedStatement stmt = connection.prepareStatement("SELECT id FROM members WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public ResultSet getTeamMembers(int teamId) {
        try {
            // Create a PreparedStatement to select all members in a team from the database
            PreparedStatement stmt = connection.prepareStatement("SELECT members.* FROM members INNER JOIN team_members ON members.id = team_members.member_id WHERE team_members.team_id = ?");
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void assignMemberToTeam(int memberId, int teamId) {
        try {
            // Create a PreparedStatement to assign a member to a team in the database
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO team_members (team_id, member_id) VALUES (?, ?)");
            stmt.setInt(1, teamId);
            stmt.setInt(2, memberId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getProjectMembers(int projectId) {
        try {
            // Create a PreparedStatement to select all members working on a project from the database
            PreparedStatement stmt = connection.prepareStatement("SELECT members.* FROM members INNER JOIN team_members ON members.id = team_members.member_id INNER JOIN project_teams ON team_members.team_id = project_teams.team_id WHERE project_teams.project_id = ?");
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void assignMemberToTask(int memberId, int taskId, int teamId) {
        try {
            // Create a PreparedStatement to assign a member to a task in the database
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO task_assignments (task_id, member_id, team_id) VALUES (?, ?, ?)");
            stmt.setInt(1, taskId);
            stmt.setInt(2, memberId);
            stmt.setInt(3, teamId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}