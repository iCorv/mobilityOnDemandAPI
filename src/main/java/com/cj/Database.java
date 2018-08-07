package com.cj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {
    /**
     * dbConnection holds the connection to the database
     */
    private static Connection dbConnection = null;


    /**
     * This function returns the database connection object, so one Connection object can be used in various classes.
     *
     * @return         database connection
     * @see Connection
     */
    public static Connection connectToDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver?");
            e.printStackTrace();
            return null;
        }

        try  {
            if(dbConnection != null) {
                return dbConnection;
            } else {
                dbConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mobilityOnDemand_db", "postgres", "CM9Ue6eEz6N32cXh");
            }
        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;

        }
        return dbConnection;
    }
}
