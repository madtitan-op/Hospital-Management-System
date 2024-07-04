package com.madtitan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {
    /**initiate variables
     *
     */
    private final Connection connection;

    public Doctor(Connection connection){
        this.connection = connection;
    }

    /** Displays all doctors
     *
     */
    public void viewDoctors(){
        String query = "SELECT * FROM doctors;";

        try( PreparedStatement statement = connection.prepareStatement(query)) {

            //resultSet stores all data fetched from the database
            ResultSet resultSet = statement.executeQuery();
            System.out.println("\nDoctors: ");

            //table format
            System.out.println(
                    """
                            +----+-----------------------------+----------------+\

                            | id |            NAME             | SPECIALIZATION |\

                            +----+-----------------------------+----------------+"""
            );

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String spec = resultSet.getString("specialization");

                System.out.printf("| %-3s| %-28s| %-15s|\n", id, name, spec);
                System.out.println("+----+-----------------------------+----------------+");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /** Gets the details of a doctor
     *
     */
    public boolean getDoctorById(int id){
        String query = "SELECT * FROM doctors WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("\ncom.madtitan.Doctor's Name: " + resultSet.getString("name") +
                        "\nSpecialization: " + resultSet.getString("specialization")
                );

                return true;
            }
            return false;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
