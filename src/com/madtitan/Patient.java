package com.madtitan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    /**initiate variables
     *
     */
    private final Connection connection;
    private final Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    /** ADD PATIENT
     *
     * @return boolean for patient added or not
     */
    public boolean addPatient(){
        scanner.nextLine();
        System.out.print("com.madtitan.Patient's name: ");
        String name = scanner.nextLine();
        System.out.print("com.madtitan.Patient's age: ");
        int age = scanner.nextInt();
        System.out.print("com.madtitan.Patient's gender(M/F): ");
        String gender = scanner.next();

        String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?);";

        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1,name);
            statement.setInt(2, age);
            statement.setString(3, gender);

            if(statement.executeUpdate() > 0){
                System.out.println("com.madtitan.Patient successfully added!!");
                return true;
            }else {
                System.out.println("Failed to add patient!!!");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /** Displays all patient
     *
     */
    public void viewPatient(){
        String query = "SELECT * FROM patients;";

        try( PreparedStatement statement = connection.prepareStatement(query)) {

            //resultSet stores all data fetched from the database
            ResultSet resultSet = statement.executeQuery();
            System.out.println("\nPatients: ");

            //table format
            System.out.println(
                    """
                            +----+-----------------------------+-----+---------+\

                            | id |            NAME             | AGE | GENDER  |\

                            +----+-----------------------------+-----+---------+"""
            );

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                System.out.printf("| %-3s| %-28s| %-4s| %-8s|\n", id, name, age, gender);
                System.out.println("+----+-----------------------------+-----+---------+");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /** Gets the details of a patient
     *
     */
    public boolean getPatientById(int id){
        String query = "SELECT * FROM patients WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("\ncom.madtitan.Patient's Name: " + resultSet.getString("name") + "\nAge: " + resultSet.getInt("age") + "\nGender: " + resultSet.getString("gender"));

                return true;
            }
            return false;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
