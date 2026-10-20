
package com.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionDAO {

    public void registerAndEnroll(
            String name, String email, String phone,
            Date admissionDate, int courseId) {

        String studentSQL =
            "INSERT INTO students " +
            "(student_name, email, phone, admission_date) " +
            "VALUES (?, ?, ?, ?)";

        String enrollmentSQL =
            "INSERT INTO enrollments " +
            "(student_id, course_id, enrollment_date) " +
            "VALUES (?, ?, ?)";

        Connection con = null;

        try {
            con = DBConnection.getConnection();

            // Disable automatic saving
            con.setAutoCommit(false);

            int studentId;

            // Step 1: Register student
            try (PreparedStatement ps = con.prepareStatement(
                    studentSQL,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, phone);
                ps.setDate(4, admissionDate);

                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException(
                            "Could not generate student ID."
                        );
                    }
                    studentId = keys.getInt(1);
                }
            }

            // Step 2: Enroll the newly registered student
            try (PreparedStatement ps =
                    con.prepareStatement(enrollmentSQL)) {

                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.setDate(3, new Date(System.currentTimeMillis()));

                ps.executeUpdate();
            }

            // Save both operations together
            con.commit();

            System.out.println(
                "Student registered and enrolled successfully!"
            );
            System.out.println("New Student ID: " + studentId);

        } catch (SQLException e) {

            if (con != null) {
                try {
                    con.rollback();
                    System.out.println(
                        "Transaction rolled back. Changes were cancelled."
                    );
                } catch (SQLException rollbackError) {
                    System.out.println(
                        "Rollback failed: " + rollbackError.getMessage()
                    );
                }
            }

            System.out.println("Transaction failed: " + e.getMessage());

        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.out.println(
                        "Connection cleanup error: " + e.getMessage()
                    );
                }
            }
        }
    }
}
