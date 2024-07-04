package com.madtitan;

import java.sql.*;
import java.util.Scanner;

public class HospitalMgmtSys {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "password";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Scanner scanner = new Scanner(System.in);
        try(Connection connection = DriverManager.getConnection(url, username, password)){

            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            int choice = 0;

            do {
                System.out.println("\n\n\n    HOSPITAL MANAGEMENT SYSTEM    \n");
                System.out.println(
                        """
                                1. Add Patient
                                2. View Patient
                                3. View Doctors
                                4. Book Appointments
                                0. EXIT"""
                );
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        //Add com.madtitan.Patient
                        patient.addPatient();
                        break;
                    case 2:
                        //View com.madtitan.Patient
                        patient.viewPatient();
                        break;
                    case 3:
                        //View Doctors
                        doctor.viewDoctors();
                        break;
                    case 4:
                        //Book Appointments
                        bookAppointment(patient, doctor, connection, scanner);
                        break;
                    case 0:
                        //exit
                        System.out.println("\nTHANK YOU FOR USING OUR SERVICE!\n");
                        break;
                    default:
                        System.out.println("Enter valid choice!!");
                }

            } while (choice != 0);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.print("\nEnter patient id: ");
        int pId = scanner.nextInt();
        boolean pExists = patient.getPatientById(pId);
        System.out.print("\nEnter doctor id: ");
        int dId = scanner.nextInt();
        boolean docExists = doctor.getDoctorById(dId);
        System.out.print("\nAppointment date (YYYY-MM-DD): ");
        String aDate = scanner.next();

        if (pExists && docExists){
            if (doctorAvailable(dId, aDate, connection)){
                String appQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES (?, ?, ?);";

                try (PreparedStatement statement = connection.prepareStatement(appQuery)){
                    statement.setInt(1, pId);
                    statement.setInt(2, dId);
                    statement.setString(3, aDate);

                    if (statement.executeUpdate() > 0){
                        System.out.println("Appointment booked!!");
                    }
                    else {
                        System.out.println("Something went wrong!!!");
                    }

                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("com.madtitan.Doctor is busy on that date!!");
            }
        }
        else {
            System.out.println("No patient / doctor with the respective id found!!!");
        }

    }

    private static boolean doctorAvailable(int dId, String aDate, Connection connection) {
        String query = "SELECT * FROM appointments WHERE doctor_id = ? AND appointment_date = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, dId);
            statement.setString(2, aDate);

            ResultSet resultSet = statement.executeQuery();
            return !resultSet.next();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
