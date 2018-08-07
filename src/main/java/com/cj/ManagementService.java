package com.cj;

import javax.ws.rs.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// The Java class will be hosted at the URI path "/management_service"
@Path("/management_service")
public class ManagementService {

    /**
     * GET request: Returns all user data from the user, specified by his ID.
     *
     * @param  userID  the ID of the requested user
     * @return         all user data belonging to the user ID
     */
    @GET
    @Path("/users/{user_id}")
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Produces("text/plain")
    public String getUsername(@PathParam("user_id") String userID) {
        // get the Connection object
        Connection conn = Database.connectToDatabase();
        if (conn == null) {
            return "Connection refused!";
        }
        // prepare db query TODO: use prepare statement to insert values
        String query = "SELECT * FROM Users WHERE User_ID = " + userID + ";";
        try  {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            // iterate to first entry in result set
            rs.next();
            return rs.getString(2) + " " + rs.getString(3);
        } catch (SQLException e) {
            e.printStackTrace();
            return "User not found!";
        }
    }

    /**
     * POST request: Adds a new user to the database. Checks if the user ID is already in use.
     *
     * @param  userID       the ID of the new user
     * @param  firstName    the first name of the new user
     * @param  lastName     the last name of the new user
     * @param  gender       the gender of the new user (0 - female, 1 - male)
     * @param  age          the age of the new user
     * @return              "Success!" on creating a new user in the database, a short information if it failed otherwise.
     */
    @POST
    @Path("/users/{user_id}")
    @Produces("text/plain")
    public String addUser(@PathParam("user_id") String userID, @FormParam("firstName") String firstName,
                          @FormParam("lastName") String lastName, @FormParam("gender") int gender, @FormParam("age") int age) {

        if(userExists(userID)) {
            return "A user with this ID already exists!";
        } else {
            Connection conn = Database.connectToDatabase();
            if (conn == null) {
                return "Connection refused!";
            }
            String query = "INSERT INTO Users(User_ID, First_Name, Last_Name, Gender, Age) VALUES(?, ?, ?, ?, ?)";
            try  {
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, Integer.parseInt(userID));
                pst.setString(2, firstName);
                pst.setString(3, lastName);
                pst.setInt(4, gender);
                pst.setInt(5, age);
                pst.executeUpdate();
                return "Success!";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Failure!";
            }
        }
    }

    /**
     * PUT request: Changes the user data in the database. Checks if the user exists.
     *
     * @param  userID       the ID of the user
     * @param  firstName    the first name of the user
     * @param  lastName     the last name of the user
     * @param  gender       the gender of the user (0 - female, 1 - male)
     * @param  age          the age of the user
     * @return              "Success!" on changing the user data in the database, a short information if it failed otherwise.
     */
    @PUT
    @Path("/users/{user_id}")
    @Produces("text/plain")
    public String changeUserData(@PathParam("user_id") String userID, @FormParam("firstName") String firstName,
                          @FormParam("lastName") String lastName, @FormParam("gender") int gender, @FormParam("age") int age) {

        if(userExists(userID)) {
            Connection conn = Database.connectToDatabase();
            if (conn == null) {
                return "Connection refused!";
            }
            String update = "UPDATE Users "
                    + "SET First_Name = ?,"
                    + "Last_Name = ?,"
                    + "Gender = ?,"
                    + "Age = ?"
                    + "WHERE User_ID = ?";
            try  {
                PreparedStatement pst = conn.prepareStatement(update);
                pst.setString(1, firstName);
                pst.setString(2, lastName);
                pst.setInt(3, gender);
                pst.setInt(4, age);
                pst.setInt(5, Integer.parseInt(userID));
                int affectedRow = pst.executeUpdate();
                return "Success!";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Failure!";
            }
        } else {
            return "A user with this ID doesn't exist!";
        }
    }

    /**
     * DELETE request: Deletes the user from the database. Checks if the user exists and has no open demands.
     *
     * @param  userID       the ID of the user
     * @return              "Success!" on deleting the user data in the database, a short information if it failed otherwise.
     */
    @DELETE
    @Path("/users/{user_id}")
    @Produces("text/plain")
    public String deleteUser(@PathParam("user_id") String userID) {
        if(userExists(userID) && !openDemands(userID)) {
            Connection conn = Database.connectToDatabase();
            if (conn == null) {
                return "Connection refused!";
            }
            String delete = "DELETE FROM Users WHERE User_ID = ?";
            try  {
                PreparedStatement pst = conn.prepareStatement(delete);
                pst.setInt(1, Integer.parseInt(userID));
                int affectedRow = pst.executeUpdate();
                return "Success!";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Failure!";
            }
        } else {
            return "A user with this ID doesn't exist or has open demands!";
        }
    }


    /**
     * POST request: Adds a new demand to the database. Checks if the user ID exists and demand number is not in use.
     *
     * @param  demandNum            the demand identifier number
     * @param  userID               the ID of the user
     * @param  fromPickUp           the location to pick up the user
     * @param  toDropOff            the location to drop off the user
     * @param  earliestPickUp       the earliest possible time to pick up the user
     * @param  latestDropOff        the latest possible drop off time
     * @param  model                the model chosen by the user
     * @param  engine               the engine chosen by the user
     * @param  infotainmentSystem   the infotainmentSystem chosen by the user
     * @param  interiorDesign       the interiorDesign chosen by the user
     * @return                      "Success!" on creating a new demand in the database, a short information if it failed otherwise.
     */
    @POST
    @Path("/demand/{demand_number}")
    @Produces("text/plain")
    public String addDemand(@PathParam("demand_number") String demandNum, @FormParam("userID") int userID,
                            @FormParam("fromPickUp") int fromPickUp, @FormParam("toDropOff") int toDropOff,
                            @FormParam("earliestPickUp") TimeStamp earliestPickUp,
                            @FormParam("latestDropOff") TimeStamp latestDropOff,
                            @FormParam("model") String model,
                            @FormParam("engine") String engine,
                            @FormParam("infotainmentSystem") String infotainmentSystem,
                            @FormParam("interiorDesign") String interiorDesign) {

        if(!userExists(Integer.toString(userID))) {
            return "No user found under this ID!";
        } else if (demandExists(demandNum)) {
            return "A demand with this number already exists!";
        } else {
            Connection conn = Database.connectToDatabase();
            if (conn == null) {
                return "Connection refused!";
            }
            String query = "INSERT INTO Demand(Demand_Number, User_ID, From_Pick_Up, To_Drop_Off, Earliest_Pick_Up, " +
                           "Latest_Drop_Off, Model, Engine, Infotainment_System, Interior_Design) " +
                           "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try  {
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, Integer.parseInt(demandNum));
                pst.setInt(2, userID);
                pst.setInt(3, fromPickUp);
                pst.setInt(4, toDropOff);
                pst.setTimestamp(5, earliestPickUp.getTimestamp());
                pst.setTimestamp(6, latestDropOff.getTimestamp());
                pst.setString(7, model);
                pst.setString(8, engine);
                pst.setString(9, infotainmentSystem);
                pst.setString(10, interiorDesign);
                pst.executeUpdate();
                return "Success!";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Failure!";
            }
        }
    }


    /**
     * Checks if the user with the specified ID exists in the database.
     *
     * @param  userID       the ID of the user
     * @return              true if exists, false otherwise.
     */
    private boolean userExists(String userID) {
        Connection conn = Database.connectToDatabase();
        if (conn == null) {
            return false;
        }
        String query = "SELECT * FROM Users WHERE User_ID = " + userID + ";";
        try  {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            rs.next();
            if(rs.getInt(1) == Integer.parseInt(userID))
                return true;
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if the user with the specified ID has open demands.
     *
     * @param  userID       the ID of the user
     * @return              true if exists, false otherwise.
     */
    private boolean openDemands(String userID) {
        Connection conn = Database.connectToDatabase();
        if (conn == null) {
            return false;
        }
        String query = "SELECT * FROM Demand WHERE User_ID = " + userID + ";";
        try  {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            rs.next();
            if(rs.getInt(2) == Integer.parseInt(userID))
                return true;
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Checks if a demand with the specified demand number exists in the database.
     *
     * @param  demandNum    the demand number to be checked for existence
     * @return              true if exists, false otherwise.
     */
    private boolean demandExists(String demandNum) {
        Connection conn = Database.connectToDatabase();
        if (conn == null) {
            return false;
        }
        String query = "SELECT * FROM Demand WHERE Demand_Number = " + demandNum + ";";
        try  {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            rs.next();
            if(rs.getInt(1) == Integer.parseInt(demandNum))
                return true;
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the highest user ID in the database.
     * @return              user ID
     */
    public int getLastUserID() {
        Connection conn = Database.connectToDatabase();
        if (conn == null) {
            return -1;
        }
        String query = "SELECT * FROM Users WHERE User_ID = (SELECT MAX(User_ID) FROM Users);";
        try  {

            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {

            System.out.println("Couldn't get last user ID!");
            e.printStackTrace();
            return -1;

        }
    }

    /**
     * Returns the highest demand number in the database.
     * @return              demand number
     */
    public int getLastDemandNum() {
        Connection conn = Database.connectToDatabase();
        if (conn == null) {
            return -1;
        }
        String query = "SELECT * FROM Demand WHERE Demand_Number = (SELECT MAX(Demand_Number) FROM Demand);";
        try  {

            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {

            System.out.println("Couldn't get last user ID!");
            e.printStackTrace();
            return -1;

        }
    }
}