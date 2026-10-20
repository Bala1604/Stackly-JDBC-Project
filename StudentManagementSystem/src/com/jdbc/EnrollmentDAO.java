
package com.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class EnrollmentDAO {

    // Enroll a student in a course

	public boolean isAlreadyEnrolled(int studentId, int courseId) {
		String sql =
			    "SELECT e.enrollment_id, s.student_id, s.student_name, " +
			    "c.course_name, e.enrollment_date " +
			    "FROM enrollments e " +
			    "JOIN students s ON e.student_id = s.student_id " +
			    "JOIN courses c ON e.course_id = c.course_id";
	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, studentId);
	        ps.setInt(2, courseId);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next() && rs.getInt(1) > 0) {
	                System.out.println(
	                    "This student is already enrolled in this course."
	                );
	                return true;
	            }
	        }

	    } catch (SQLException e) {
	        System.out.println("Enrollment check failed: " + e.getMessage());
	        return true;
	    }

	    return false;
	}


    // Display student names and their course names using JOIN
    public void viewEnrollments() {

        String sql =
            "SELECT s.student_id, s.student_name, c.course_name, " +
            "e.enrollment_date " +
            "FROM enrollments e " +
            "JOIN students s ON e.student_id = s.student_id " +
            "JOIN courses c ON e.course_id = c.course_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Student Enrollments ---");

            while (rs.next()) {
                System.out.println(
                    "Student ID: " + rs.getInt("student_id") +
                    ", Student: " + rs.getString("student_name") +
                    ", Course: " + rs.getString("course_name") +
                    ", Date: " + rs.getDate("enrollment_date")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error viewing enrollments: "
                               + e.getMessage());
        }
    }
    

public void enrollStudent(int studentId, int courseId) {

    String sql = "INSERT INTO enrollments " +
                 "(student_id, course_id, enrollment_date) " +
                 "VALUES (?, ?, ?)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, studentId);
        ps.setInt(2, courseId);
        ps.setDate(3, new java.sql.Date(
            System.currentTimeMillis()
        ));

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Student enrolled successfully!");
        }

    } catch (SQLException e) {
        System.out.println("Enrollment failed: " + e.getMessage());
    }
}

public boolean enrollmentIdsExist(int studentId, int courseId) {

    String sql =
        "SELECT " +
        "(SELECT COUNT(*) FROM students WHERE student_id = ?) AS student_count, " +
        "(SELECT COUNT(*) FROM courses WHERE course_id = ?) AS course_count";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, studentId);
        ps.setInt(2, courseId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                if (rs.getInt("student_count") == 0) {
                    System.out.println("Student ID does not exist.");
                    return false;
                }

                if (rs.getInt("course_count") == 0) {
                    System.out.println("Course ID does not exist.");
                    return false;
                }

                return true;
            }
        }

    } catch (SQLException e) {
        System.out.println("ID validation failed: " + e.getMessage());
    }

    return false;
}

}
