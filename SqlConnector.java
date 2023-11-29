package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class is used to connect to the database and retrieve the information
 * needed to create the schedule.
 *
 * @author Sergiy Redko
 * @version 1.0
 * @since 1.0
 */
public class SqlConnector {
    private String url;
    private String user;
    private String password;
    private Connection connection;

    /**
     * This constructor creates a new SqlConnector object.
     *
     * @param url      The url of the database.
     * @param user     The user-name for the database.
     * @param password The password for the database.
     */
    public SqlConnector(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * This method creates a connection to the database.
     *
     * @throws CouldNotConnectToDatabaseException when connection to DB could not be established.
     */
    public void createConnection() throws CouldNotConnectToDatabaseException {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new CouldNotConnectToDatabaseException(e.getMessage());
        }
    }

    /**
     * This method retrieves the information about the treatments from the database.
     *
     * @return ArrayList of Treatment objects.
     * @throws SQLException when there is an error with the SQL query.
     */
    public ArrayList<Treatment> getTreatments() throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select animals.animalid, animals.animalnickname, animals.animalspecies, treatments.taskid,  treatments.treatmentid, treatments.starthour, tasks.duration, tasks.description, tasks.maxwindow\n" +
                "from Animals\n" +
                "join Treatments on animals.animalid = treatments.animalid\n" +
                "join tasks on treatments.taskid = tasks.taskid;");

        ArrayList<Treatment> treatments = new ArrayList<>();

        while (resultSet.next()) {
            Treatment treatment = new Treatment(resultSet.getInt("animalid"),
                    resultSet.getString("animalnickname"),
                    resultSet.getInt("taskid"),
                    resultSet.getInt("treatmentid"),
                    resultSet.getInt("starthour"),
                    resultSet.getInt("duration"),
                    resultSet.getString("description"),
                    resultSet.getInt("maxwindow"));
            treatments.add(treatment);
        }
        return treatments;
    }

    /**
     * This method retrieves the information about the cleanings from the database.
     *
     * @return String array of Task objects.
     * @throws SQLException when there is an error with the SQL query.
     */
    public String[] getCleanings(AnimalSpecies species) throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select animals.animalnickname\n" +
                "from animals\n" +
                "where AnimalSpecies = '" + species.toString().toLowerCase() + "';");

        ArrayList<String> cleanings = new ArrayList<>();

        while (resultSet.next()) {
            String cleaning = resultSet.getString("animalnickname");
            cleanings.add(cleaning);
        }

        String[] stringCleanings = cleanings.toArray(new String[cleanings.size()]);
        return stringCleanings;
    }

    /**
     * This method retrieves the information about the feedings from the database.
     *
     * @return String array of Task objects.
     * @throws SQLException when there is an error with the SQL query.
     */
    public String[] getFeedings(AnimalSpecies species) throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select animals.animalnickname\n" +
                "from animals\n" +
                "where animals.animalid in\n" +
                "\t(select animals.animalid\n" +
                "\tfrom Animals\n" +
                "\tjoin Treatments on animals.animalid = treatments.animalid\n" +
                "\tjoin tasks on treatments.taskid = tasks.taskid\n" +
                "\tWHERE tasks.Description != 'Kit feeding' and animals.AnimalSpecies = '" + species.toString().toLowerCase() + "');");

        ArrayList<String> feedings = new ArrayList<>();

        while (resultSet.next()) {
            String feeding = resultSet.getString("animalnickname");
            feedings.add(feeding);
        }
        String[] stringFeedings = feedings.toArray(new String[feedings.size()]);

        return stringFeedings;
    }

    /**
     * This method closes the connection to the database.
     *
     * @throws CouldNotCloseDatabaseConnectionException when connection to DB could not be closed.
     */
    public void closeConnection() throws CouldNotCloseDatabaseConnectionException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new CouldNotCloseDatabaseConnectionException(e.getMessage());
        }
    }

    /**
     * This method updates the treatments in the database.
     *
     * @param treatmentList ArrayList of Treatment objects.
     * @throws SQLException when there is an error with the SQL query.
     */
    public void updateTreatments(ArrayList<Treatment> treatmentList) throws SQLException{
        Statement statement = connection.createStatement();

        for(Treatment treatment : treatmentList){

            statement.executeUpdate("update treatments set StartHour = " + treatment.getStartHour() + " where TreatmentID = " + treatment.getTreatmentID() + ";");
        }

    }

}
