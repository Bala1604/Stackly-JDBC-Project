
package com.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultDAO {

    public void generateResult(int enrollmentId) {

        String sql =
            "SELECT COUNT(*) AS total_subjects, " +
            "COALESCE(SUM(marks_obtained), 0) AS total_marks, " +
            "COALESCE(SUM(max_marks), 0) AS max_total, " +
            "COALESCE(SUM(CASE " +
            "WHEN marks_obtained < max_marks * 0.40 " +
            "THEN 1 ELSE 0 END), 0) AS failed_subjects " +
            "FROM marks WHERE enrollment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    int subjects = rs.getInt("total_subjects");
                    double total = rs.getDouble("total_marks");
                    double maxTotal = rs.getDouble("max_total");
                    int failed = rs.getInt("failed_subjects");

                    if (subjects == 0 || maxTotal == 0) {
                        System.out.println(
                            "No marks available for this enrollment."
                        );
                        return;
                    }

                    double percentage = total * 100.0 / maxTotal;
                    String result = failed == 0 ? "PASS" : "FAIL";

                    System.out.println("\n===== STUDENT RESULT =====");
                    System.out.println("Enrollment ID: " + enrollmentId);
                    System.out.println("Subjects: " + subjects);
                    System.out.printf("Total Marks: %.2f / %.2f%n",
                                      total, maxTotal);
                    System.out.printf("Percentage: %.2f%%%n",
                                      percentage);
                    System.out.println("Result: " + result);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error generating result: "
                               + e.getMessage());
        }
    }
    
}
