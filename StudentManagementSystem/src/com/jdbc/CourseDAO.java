
package com.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseDAO {

    // 1. Add a new course
    public void addCourse(String courseName, int duration) {

        if (courseName == null || courseName.trim().isEmpty()) {
            System.out.println("Course name cannot be empty.");
            return;
        }

        if (duration <= 0) {
            System.out.println("Duration must be greater than zero.");
            return;
        }

        String sql = "INSERT INTO courses " +
                     "(course_name, duration_months) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseName.trim());
            ps.setInt(2, duration);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Course added successfully!");
            }

        } catch (SQLException e) {
            System.out.println(
                "Error adding course: " + e.getMessage()
            );
        }
    }

    // 2. View all courses
    public void viewCourses() {

        String sql = "SELECT course_id, course_name, duration_months " +
                     "FROM courses ORDER BY course_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Available Courses ---");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println(
                    "Course ID: " + rs.getInt("course_id") +
                    ", Name: " + rs.getString("course_name") +
                    ", Duration: " +
                    rs.getInt("duration_months") + " months"
                );
            }

            if (!found) {
                System.out.println("No courses available yet.");
            }

        } catch (SQLException e) {
            System.out.println(
                "Error viewing courses: " + e.getMessage()
            );
        }
    }
}
