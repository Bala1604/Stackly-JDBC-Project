
package com.leave;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LeaveRequestDAO {

    public void applyLeave(
            int employeeId,
            String leaveType,
            String fromDate,
            String toDate,
            String reason) {

        String sql = "INSERT INTO leave_requests " +
                "(employee_id, leave_type, from_date, to_date, " +
                "reason, status, applied_date) " +
                "VALUES (?, ?, ?, ?, ?, 'PENDING', ?)";

        try {
            LocalDate start = LocalDate.parse(fromDate);
            LocalDate end = LocalDate.parse(toDate);

            if (end.isBefore(start)) {
                System.out.println(
                        "Error: To date cannot be before from date.");
                return;
            }

            if (start.isBefore(LocalDate.now())) {
                System.out.println(
                        "Error: Leave cannot start in the past.");
                return;
            }

        } catch (java.time.format.DateTimeParseException e) {
            System.out.println(
                    "Invalid date. Use YYYY-MM-DD.");
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ps.setString(2, leaveType);
            ps.setDate(3, Date.valueOf(fromDate));
            ps.setDate(4, Date.valueOf(toDate));
            ps.setString(5, reason);
            ps.setDate(6, Date.valueOf(LocalDate.now()));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println(
                        "Leave request submitted successfully!");
                System.out.println(
                        "Status: PENDING");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error applying leave: " + e.getMessage());
        }
    }

public void viewLeaveRequests() {

    String sql =
        "SELECT lr.leave_id, e.employee_name, " +
        "lr.leave_type, lr.from_date, lr.to_date, " +
        "lr.reason, lr.status, lr.applied_date " +
        "FROM leave_requests lr " +
        "JOIN employees e ON lr.employee_id = e.employee_id " +
        "ORDER BY lr.leave_id DESC";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        System.out.println("\n--- Leave Requests ---");

        boolean found = false;

        while (rs.next()) {
            found = true;

            System.out.println(
                "Leave ID: " + rs.getInt("leave_id") +
                ", Employee: " + rs.getString("employee_name") +
                ", Type: " + rs.getString("leave_type") +
                ", From: " + rs.getDate("from_date") +
                ", To: " + rs.getDate("to_date") +
                ", Reason: " + rs.getString("reason") +
                ", Status: " + rs.getString("status") +
                ", Applied: " + rs.getDate("applied_date")
            );
        }

        if (!found) {
            System.out.println("No leave requests found.");
        }

    } catch (SQLException e) {
        System.out.println(
            "Error viewing leave requests: " + e.getMessage());
    }
}

}
