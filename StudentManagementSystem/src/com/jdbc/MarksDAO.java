
package com.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarksDAO {

    // 1. Enter marks for a subject
    public void addMarks(int enrollmentId, String subject,
                         double marksObtained, double maxMarks) {

        if (marksObtained < 0 || maxMarks <= 0 ||
            marksObtained > maxMarks) {
            System.out.println("Invalid marks. Check the values.");
            return;
        }

        String sql = "INSERT INTO marks " +
                     "(enrollment_id, subject_name, marks_obtained, max_marks) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);
            ps.setString(2, subject);
            ps.setDouble(3, marksObtained);
            ps.setDouble(4, maxMarks);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Marks entered successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error entering marks: " + e.getMessage());
        }
    }

    // 2. View marks
    public void viewMarks(int enrollmentId) {

        String sql = "SELECT subject_name, marks_obtained, max_marks " +
                     "FROM marks WHERE enrollment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\n--- Marks Details ---");

                while (rs.next()) {
                    System.out.println(
                        "Subject: " + rs.getString("subject_name") +
                        ", Marks: " + rs.getDouble("marks_obtained") +
                        "/" + rs.getDouble("max_marks")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error viewing marks: " + e.getMessage());
        }
    }
    
}
