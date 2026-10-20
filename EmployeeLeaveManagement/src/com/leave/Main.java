
package com.leave;

import java.util.Scanner;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        EmployeeDAO employeeDAO = new EmployeeDAO();
        LeaveRequestDAO requestDAO = new LeaveRequestDAO();
        LeaveApprovalDAO approvalDAO = new LeaveApprovalDAO();
        LeaveBalanceDAO balanceDAO = new LeaveBalanceDAO();

        int choice;

        do {
            System.out.println("\n===== EMPLOYEE LEAVE MANAGEMENT =====");
            System.out.println("1. Register Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Apply Leave");
            System.out.println("4. View Leave Requests");
            System.out.println("5. Approve Leave");
            System.out.println("6. Reject Leave");
            System.out.println("7. View Leave Balance");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid menu number.");
                continue;
            }

            switch (choice) {


case 1:
    System.out.print("Employee name: ");
    String name = sc.nextLine().trim();

    System.out.print("Email: ");
    String email = sc.nextLine().trim();

    System.out.print("Department: ");
    String department = sc.nextLine().trim();

    System.out.print("Joining date (YYYY-MM-DD): ");
    String joiningDate = sc.nextLine().trim();

    if (name.isEmpty() || email.isEmpty()
            || department.isEmpty()) {
        System.out.println("Fields cannot be empty.");
        break;
    }

    employeeDAO.registerEmployee(
        name, email, department, joiningDate);
    break;


                case 2:
                    employeeDAO.viewEmployees();
                    break;

                case 3:
                    try {
                        System.out.print("Employee ID: ");
                        int employeeId =
                            Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Leave type (CASUAL/SICK): ");
                        String leaveType = sc.nextLine().trim();

                        System.out.print("From date (YYYY-MM-DD): ");
                        String fromDate = sc.nextLine().trim();

                        System.out.print("To date (YYYY-MM-DD): ");
                        String toDate = sc.nextLine().trim();

                        System.out.print("Reason: ");
                        String reason = sc.nextLine().trim();

                        requestDAO.applyLeave(
                            employeeId, leaveType,
                            fromDate, toDate, reason);

                    } catch (NumberFormatException e) {
                        System.out.println("Employee ID must be a number.");
                    }
                    break;

                case 4:
                    requestDAO.viewLeaveRequests();
                    break;

                case 5:
                    try {
                        System.out.print("Leave ID to approve: ");
                        int leaveId =
                            Integer.parseInt(sc.nextLine().trim());
                        approvalDAO.approveLeave(leaveId);
                    } catch (NumberFormatException e) {
                        System.out.println("Leave ID must be a number.");
                    }
                    break;

                case 6:
                    try {
                        System.out.print("Leave ID to reject: ");
                        int leaveId =
                            Integer.parseInt(sc.nextLine().trim());
                        approvalDAO.rejectLeave(leaveId);
                    } catch (NumberFormatException e) {
                        System.out.println("Leave ID must be a number.");
                    }
                    break;

                case 7:
                    try {
                        System.out.print("Employee ID: ");
                        int employeeId =
                            Integer.parseInt(sc.nextLine().trim());
                        balanceDAO.viewLeaveBalance(employeeId);
                    } catch (NumberFormatException e) {
                        System.out.println("Employee ID must be a number.");
                    }
                    break;

                case 0:
                    System.out.println(
                        "Thank you for using the system!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (true);
    }
}
