
package com.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class AttendanceDAO {

    // 1. Mark attendance
    public void markAttendance(int enrollmentId, String status) {


if (status == null ||
    (!status.trim().equalsIgnoreCase("Present") &&
     !status.trim().equalsIgnoreCase("Absent"))) {

    System.out.println("Status must be Present or Absent.");
    return;
}

status = status.trim().substring(0, 1).toUpperCase()
         + status.trim().substring(1).toLowerCase();


String sql =
"INSERT INTO attendance (enrollment_id, attendance_date, status) " +
"VALUES (?, ?, ?) " +
"ON DUPLICATE KEY UPDATE status = VALUES(status)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);
            ps.setDate(2, new Date(System.currentTimeMillis()));
            ps.setString(3, status);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Attendance marked successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error marking attendance: "
                               + e.getMessage());
        }
    }

    // 2. View attendance records
    public void viewAttendance() {

        String sql =
            "SELECT s.student_name, c.course_name, " +
            "a.attendance_date, a.status " +
            "FROM attendance a " +
            "JOIN enrollments e ON a.enrollment_id = e.enrollment_id " +
            "JOIN students s ON e.student_id = s.student_id " +
            "JOIN courses c ON e.course_id = c.course_id " +
            "ORDER BY a.attendance_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Attendance Records ---");

            while (rs.next()) {
                System.out.println(
                    "Student: " + rs.getString("student_name") +
                    ", Course: " + rs.getString("course_name") +
                    ", Date: " + rs.getDate("attendance_date") +
                    ", Status: " + rs.getString("status")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error viewing attendance: "
                               + e.getMessage());
        }
    }

    // 3. Calculate attendance percentage
    public void calculateAttendance(int enrollmentId) {

        String sql =
            "SELECT COUNT(*) AS total_days, " +
            "COALESCE(SUM(CASE WHEN status = 'Present' " +
            "THEN 1 ELSE 0 END), 0) AS present_days " +
            "FROM attendance WHERE enrollment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int totalDays = rs.getInt("total_days");
                    int presentDays = rs.getInt("present_days");

                    double percentage = totalDays == 0 ? 0.0 :
                        (presentDays * 100.0 / totalDays);

                    System.out.println("\n--- Attendance Summary ---");
                    System.out.println("Total days: " + totalDays);
                    System.out.println("Present days: " + presentDays);
                    System.out.printf("Attendance: %.2f%%%n",
                                      percentage);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error calculating attendance: "
                               + e.getMessage());
        }
    }
    public boolean enrollmentExists(int enrollmentId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE enrollment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error checking enrollment: " + e.getMessage());
        }

        System.out.println("Enrollment ID " + enrollmentId + " does not exist.");
        return false;
    }
}
