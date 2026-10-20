package com.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class StudentDAO {

    // 1. Register a new student
    public void addStudent(String name, String email,
                           String phone, Date admissionDate) {

        String sql = "INSERT INTO students " +
                     "(student_name, email, phone, admission_date) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setDate(4, admissionDate);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student registered successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error registering student: "
                               + e.getMessage());
        }
    }

    // 2. Display all students

public void viewStudents() {

    String sql = "SELECT student_id, student_name, email, " +
                 "phone, admission_date FROM students";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        System.out.println("\n--- Student Details ---");

        boolean found = false;

        while (rs.next()) {
            found = true;

            System.out.println(
                "ID: " + rs.getInt("student_id") +
                ", Name: " + rs.getString("student_name") +
                ", Email: " + rs.getString("email") +
                ", Phone: " + rs.getString("phone") +
                ", Admission Date: " +
                rs.getDate("admission_date")
            );
        }

        if (!found) {
            System.out.println("No students registered yet.");
        }

    } catch (SQLException e) {
        System.out.println(
            "Error viewing students: " + e.getMessage()
        );
    }
}

    // 3. Update student details
    public void updateStudent(int id, String name,
                              String email, String phone) {

        String sql = "UPDATE students SET student_name = ?, " +
                     "email = ?, phone = ? WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating student: "
                               + e.getMessage());
        }
    }

    // 4. Delete a student
    public void deleteStudent(int id) {

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting student: "
                               + e.getMessage());
        }
    }
}
