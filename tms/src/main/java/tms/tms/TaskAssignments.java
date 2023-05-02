package tms.tms;

import java.sql.*;
import java.util.ArrayList;

public class TaskAssignments {

    static private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Globals.getDb_name(), Globals.getDb_username(), Globals.getDb_pass());
    }

    // function to create a new task assignment
    public void createTaskAssignment(int taskId, int memberId, int teamId) throws SQLException {
        // check if the task assignment already exists
        String checkQuery = "SELECT * FROM task_assignments WHERE task_id = ? AND member_id = ? AND team_id = ?";
        PreparedStatement checkStmt = connect().prepareStatement(checkQuery);
        checkStmt.setInt(1, taskId);
        checkStmt.setInt(2, memberId);
        checkStmt.setInt(3, teamId);
        ResultSet checkResult = checkStmt.executeQuery();
        if (checkResult.next()) {
            throw new SQLException("Task assignment already exists.");
        }

        // insert new task assignment
        String insertQuery = "INSERT INTO task_assignments (task_id, member_id, team_id) VALUES (?, ?, ?)";
        PreparedStatement insertStmt = connect().prepareStatement(insertQuery);
        insertStmt.setInt(1, taskId);
        insertStmt.setInt(2, memberId);
        insertStmt.setInt(3, teamId);
        insertStmt.executeUpdate();
    }

    // function to update an existing task assignment
    public void updateTaskAssignment(int taskId, int memberId, int teamId, int newTaskId, int newMemberId, int newTeamId) throws SQLException {
        // check if the task assignment exists
        String checkQuery = "SELECT * FROM task_assignments WHERE task_id = ? AND member_id = ? AND team_id = ?";
        PreparedStatement checkStmt = connect().prepareStatement(checkQuery);
        checkStmt.setInt(1, taskId);
        checkStmt.setInt(2, memberId);
        checkStmt.setInt(3, teamId);
        ResultSet checkResult = checkStmt.executeQuery();
        if (!checkResult.next()) {
            throw new SQLException("Task assignment does not exist.");
        }

        // check if the new task assignment is unique and non-existent
        String uniqueQuery = "SELECT * FROM task_assignments WHERE task_id = ? AND member_id = ? AND team_id = ?";
        PreparedStatement uniqueStmt = connect().prepareStatement(uniqueQuery);
        uniqueStmt.setInt(1, newTaskId);
        uniqueStmt.setInt(2, newMemberId);
        uniqueStmt.setInt(3, newTeamId);
        ResultSet uniqueResult = uniqueStmt.executeQuery();
        if (uniqueResult.next() && !(taskId == newTaskId && memberId == newMemberId && teamId == newTeamId)) {
            throw new SQLException("New task assignment already exists.");
        }

        // update the task assignment
        String updateQuery = "UPDATE task_assignments SET task_id = ?, member_id = ?, team_id = ? WHERE task_id = ? AND member_id = ? AND team_id = ?";
        PreparedStatement updateStmt = connect().prepareStatement(updateQuery);
        updateStmt.setInt(1, newTaskId);
        updateStmt.setInt(2, newMemberId);
        updateStmt.setInt(3, newTeamId);
        updateStmt.setInt(4, taskId);
        updateStmt.setInt(5, memberId);
        updateStmt.setInt(6, teamId);
        updateStmt.executeUpdate();
    }
    public void deleteTaskAssignment(int taskId, int memberId, int teamId) throws SQLException {
            String query = "DELETE FROM task_assignments WHERE task_id = ? AND member_id = ? AND team_id = ?";
            PreparedStatement pstmt = connect().prepareStatement(query);
            pstmt.setInt(1, taskId);
            pstmt.setInt(2, memberId);
            pstmt.setInt(3, teamId);
            pstmt.executeUpdate();
    }
}