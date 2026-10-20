
package com.leave;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaveBalanceDAO {

    public void viewLeaveBalance(int employeeId) {

        String sql =
            "SELECT e.employee_name, " +
            "lb.total_leaves, lb.used_leaves " +
            "FROM employees e " +
            "JOIN leave_balance lb " +
            "ON e.employee_id = lb.employee_id " +
            "WHERE e.employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    int total = rs.getInt("total_leaves");
                    int used = rs.getInt("used_leaves");
                    int remaining = total - used;

                    System.out.println("\n--- Leave Balance ---");
                    System.out.println(
                        "Employee: " +
                        rs.getString("employee_name"));
                    System.out.println("Total Leaves: " + total);
                    System.out.println("Used Leaves: " + used);
                    System.out.println(
                        "Remaining Leaves: " + remaining);

                } else {
                    System.out.println(
                        "Employee or leave balance not found.");
                }
            }

        } catch (SQLException e) {
            System.out.println(
                "Error viewing leave balance: " + e.getMessage());
        }
    }
}
