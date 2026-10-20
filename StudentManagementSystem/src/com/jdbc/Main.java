
package com.jdbc;

import java.sql.Date;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static StudentDAO studentDAO = new StudentDAO();
    static CourseDAO courseDAO = new CourseDAO();
    static EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    static AttendanceDAO attendanceDAO = new AttendanceDAO();
    static MarksDAO marksDAO = new MarksDAO();
    static ResultDAO resultDAO = new ResultDAO();
    static TransactionDAO transactionDAO = new TransactionDAO();

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Register Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Add Course");
            System.out.println("6. View Courses");
            System.out.println("7. Enroll Student");
            System.out.println("8. View Enrollments");
            System.out.println("9. Mark Attendance");
            System.out.println("10. View Attendance");
            System.out.println("11. Calculate Attendance");
            System.out.println("12. Enter Marks");
            System.out.println("13. View Marks");
            System.out.println("14. Generate Result");
            System.out.println("15. Register and Enroll (Transaction)");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {

                case 1:
                    registerStudent();
                    break;

                case 2:
                    studentDAO.viewStudents();
                    break;

                case 3:
                    System.out.print("Student ID: ");
                    int updateId = readInt();

                    System.out.print("New name: ");
                    String name = sc.nextLine();

                    System.out.print("New email: ");
                    String email = sc.nextLine();

                    System.out.print("New phone: ");
                    String phone = sc.nextLine();

                    studentDAO.updateStudent(
                        updateId, name, email, phone
                    );
                    break;

                case 4:
                    System.out.print("Student ID to delete: ");
                    studentDAO.deleteStudent(readInt());
                    break;

                case 5:
                    System.out.print("Course name: ");
                    String courseName = sc.nextLine();

                    System.out.print("Duration in months: ");
                    int duration = readInt();

                    courseDAO.addCourse(courseName, duration);
                    break;

                case 6:
                    courseDAO.viewCourses();
                    break;



case 7:
    System.out.println("--- Available Students ---");
    studentDAO.viewStudents();

    System.out.println("--- Available Courses ---");
    courseDAO.viewCourses();

    System.out.print("Student ID: ");
    int studentId = readInt();

    System.out.print("Course ID: ");
    int courseId = readInt();

    if (enrollmentDAO.enrollmentIdsExist(studentId, courseId)) {
        enrollmentDAO.enrollStudent(studentId, courseId);
    }
    break;



                case 8:
                    enrollmentDAO.viewEnrollments();
                    break;

                case 9:
                    System.out.print("Enrollment ID: ");
                    int attendanceEnrollment = readInt();

                    System.out.print("Status (Present/Absent): ");
                    String status = sc.nextLine();

                    attendanceDAO.markAttendance(
                        attendanceEnrollment, status
                    );
                    break;

                case 10:
                    attendanceDAO.viewAttendance();
                    break;

                case 11:
                    System.out.print("Enrollment ID: ");
                    attendanceDAO.calculateAttendance(readInt());
                    break;

                case 12:
                    System.out.print("Enrollment ID: ");
                    int marksEnrollment = readInt();

                    System.out.print("Subject name: ");
                    String subject = sc.nextLine();

                    System.out.print("Marks obtained: ");
                    double obtained = readDouble();

                    System.out.print("Maximum marks: ");
                    double maximum = readDouble();

                    marksDAO.addMarks(
                        marksEnrollment, subject, obtained, maximum
                    );
                    break;

                case 13:
                    System.out.print("Enrollment ID: ");
                    marksDAO.viewMarks(readInt());
                    break;

                case 14:
                    System.out.print("Enrollment ID: ");
                    resultDAO.generateResult(readInt());
                    break;

                case 15:
                    registerAndEnroll();
                    break;

                case 0:
                    System.out.println("Thank you for using the system!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);

        sc.close();
    }

    static void registerStudent() {

        System.out.print("Student name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Phone: ");
        String phone = sc.nextLine();

        System.out.print("Admission date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        studentDAO.addStudent(
            name, email, phone, Date.valueOf(date)
        );
    }

    static void registerAndEnroll() {

        System.out.print("Student name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Phone: ");
        String phone = sc.nextLine();

        System.out.print("Admission date (YYYY-MM-DD): ");
        Date admissionDate = Date.valueOf(sc.nextLine());

        System.out.print("Existing course ID: ");
        int courseId = readInt();

        transactionDAO.registerAndEnroll(
            name, email, phone, admissionDate, courseId
        );
    }


static int readInt() {
    while (true) {
        String input = sc.nextLine().trim();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(
                "Invalid input: '" + input + "'. " +
                "Please enter a whole number, such as 1, 2, or 7."
            );
            System.out.print("Try again: ");
        }
    }
}


    static double readDouble() {
        while (true) {
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                if (Double.isFinite(value)) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // Ask again below.
            }
            System.out.print("Enter a valid number: ");
        }
    }
}
