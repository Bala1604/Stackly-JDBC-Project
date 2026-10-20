
package com.leave;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LeaveApprovalDAO {

    public void approveLeave(int leaveId) {

        String requestSql =
            "SELECT employee_id, from_date, to_date, status " +
            "FROM leave_requests WHERE leave_id = ? FOR UPDATE";

        String balanceSql =
            "SELECT total_leaves, used_leaves " +
            "FROM leave_balance WHERE employee_id = ? FOR UPDATE";

        String updateRequestSql =
            "UPDATE leave_requests SET status = 'APPROVED' " +
            "WHERE leave_id = ? AND status = 'PENDING'";

        String updateBalanceSql =
            "UPDATE leave_balance SET used_leaves = used_leaves + ? " +
            "WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int employeeId;
                int days;

                try (PreparedStatement ps =
                         con.prepareStatement(requestSql)) {

                    ps.setInt(1, leaveId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                "Leave request not found.");
                        }

                        if (!"PENDING".equals(rs.getString("status"))) {
                            throw new SQLException(
                                "Only pending requests can be approved.");
                        }

                        employeeId = rs.getInt("employee_id");

                        LocalDate from = rs.getDate("from_date").toLocalDate();
                        LocalDate to = rs.getDate("to_date").toLocalDate();

                        days = Math.toIntExact(
                            ChronoUnit.DAYS.between(from, to) + 1);
                    }
                }

                int totalLeaves;
                int usedLeaves;

                try (PreparedStatement ps =
                         con.prepareStatement(balanceSql)) {

                    ps.setInt(1, employeeId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                "Leave balance record not found.");
                        }

                        totalLeaves = rs.getInt("total_leaves");
                        usedLeaves = rs.getInt("used_leaves");
                    }
                }

                if (days > totalLeaves - usedLeaves) {
                    throw new SQLException(
                        "Insufficient leave balance.");
                }

                try (PreparedStatement ps =
                         con.prepareStatement(updateRequestSql)) {

                    ps.setInt(1, leaveId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                            "Could not approve leave request.");
                    }
                }

                try (PreparedStatement ps =
                         con.prepareStatement(updateBalanceSql)) {

                    ps.setInt(1, days);
                    ps.setInt(2, employeeId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                            "Could not update leave balance.");
                    }
                }

                con.commit();

                System.out.println(
                    "Leave approved successfully!");
                System.out.println(
                    "Leave days deducted: " + days);

            } catch (SQLException | ArithmeticException e) {

                try {
                    con.rollback();
                    System.out.println(
                        "Transaction rolled back.");
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                System.out.println(
                    "Approval failed: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(
                "Database error: " + e.getMessage());
        }
    }

    public void rejectLeave(int leaveId) {

        String sql =
            "UPDATE leave_requests SET status = 'REJECTED' " +
            "WHERE leave_id = ? AND status = 'PENDING'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, leaveId);

            if (ps.executeUpdate() == 1) {
                System.out.println(
                    "Leave request rejected successfully!");
            } else {
                System.out.println(
                    "Request not found or no longer pending.");
            }

        } catch (SQLException e) {
            System.out.println(
                "Error rejecting leave: " + e.getMessage());
        }
    }
}
