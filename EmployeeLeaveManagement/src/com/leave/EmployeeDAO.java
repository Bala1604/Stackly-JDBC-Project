
package com.leave;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAO {

    public void registerEmployee(
            String name,
            String email,
            String department,
            String joiningDate) {

        String employeeSql =
            "INSERT INTO employees " +
            "(employee_name, email, department, joining_date) " +
            "VALUES (?, ?, ?, ?)";

        String balanceSql =
            "INSERT INTO leave_balance " +
            "(employee_id, total_leaves, used_leaves) " +
            "VALUES (?, 12, 0)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int employeeId;

                try (PreparedStatement ps =
                         con.prepareStatement(
                             employeeSql,
                             PreparedStatement.RETURN_GENERATED_KEYS)) {

                    ps.setString(1, name.trim());
                    ps.setString(2, email.trim());
                    ps.setString(3, department.trim());
                    ps.setDate(4, Date.valueOf(joiningDate));

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                "Employee ID was not generated.");
                        }

                        employeeId = rs.getInt(1);
                    }
                }

                try (PreparedStatement ps =
                         con.prepareStatement(balanceSql)) {

                    ps.setInt(1, employeeId);
                    ps.executeUpdate();
                }

                con.commit();

                System.out.println(
                    "Employee registered successfully!");

                System.out.println(
                    "Employee ID: " + employeeId);

                System.out.println(
                    "Initial leave balance: 12 days");

            } catch (SQLException | IllegalArgumentException e) {

                try {
                    con.rollback();
                    System.out.println(
                        "Transaction rolled back.");
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                System.out.println(
                    "Registration failed: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(
                "Database error: " + e.getMessage());
        }
    }

public void viewEmployees() {

    String sql = "SELECT * FROM employees";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        System.out.println("\n--- Employee Details ---");

        boolean found = false;

        while (rs.next()) {
            found = true;

            System.out.println(
                "ID: " + rs.getInt("employee_id")
                + ", Name: " + rs.getString("employee_name")
                + ", Email: " + rs.getString("email")
                + ", Department: " + rs.getString("department")
                + ", Joining Date: " + rs.getDate("joining_date")
            );
        }

        if (!found) {
            System.out.println("No employees registered yet.");
        }

    } catch (SQLException e) {
        System.out.println(
            "Error viewing employees: " + e.getMessage()
        );
    }
}

}
