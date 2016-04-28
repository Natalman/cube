package com.company;

import java.sql.*;

public class CubeData {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_Connection_URL = "jdbc:mysql://localhost:3306/cube";
    static final String USER = "root";
    static final String PASSWORD = "itecitec";

    static Statement cubeState = null;
    static Connection cubCon = null;
    static ResultSet Rs = null;

    public final static String CUBES_TABLE_NAME = "Cubes";
    public final static String PK_COLUMN = "id";      // Primary key

    public final static String NAME_COLUMN = "SolvedRubrikCube";
    public final static String TIME_COLUMN = "TimeTakenInSec";

    private static CubeModel cubeModel;



    public static void main(String[] args) {

        //setup creates database (if it doesn't exist), opens connection, and adds sample data

        if (!setup()) {
            System.exit(-1);
        }

        if (!loadCubes()) {
            System.exit(-1);
        }

        //If no errors, then start GUI
        CubeForm tableGUI = new CubeForm(cubeModel);

    }
    public static boolean loadCubes(){

        try{

            if (Rs!=null) {
                Rs.close();
            }

            String getAllData = "SELECT * FROM " + CUBES_TABLE_NAME;
            Rs = cubeState.executeQuery(getAllData);

            if (cubeModel == null) {
                //If no current movieDataModel, then make one
                cubeModel = new CubeModel(Rs);
            } else {
                //Or, if one already exists, update its ResultSet
                cubeModel.updateResultSet(Rs);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading cubes table");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

    }

    public static boolean setup(){
        try {

            //Load driver class
            try {
                String Driver = "com.mysql.jdbc.Driver";
                Class.forName(Driver);
            } catch (ClassNotFoundException cnfe) {
                System.out.println("No database drivers found. Quitting");
                return false;
            }

            cubCon =DriverManager.getConnection(DB_Connection_URL, USER, PASSWORD);

            cubeState = cubCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Does the table exist? If not, create it.
            if (!CubeExists()) {

                //Create a table in the database with 3 columns: ID, Name, and time
                String createTableSQL = "CREATE TABLE " + CUBES_TABLE_NAME + " (" + PK_COLUMN + " int NOT NULL AUTO_INCREMENT, " + NAME_COLUMN + " varchar(50), " + TIME_COLUMN + " double, PRIMARY KEY(" + PK_COLUMN + "))";
                System.out.println(createTableSQL);
                cubeState.executeUpdate(createTableSQL);

                System.out.println("Created cube table");

                //creating cube data
                String addDataSQL = "INSERT INTO " + CUBES_TABLE_NAME + "(" + NAME_COLUMN + ", " + TIME_COLUMN + ")" + " VALUES ('Cubestomer II robot', 5.270)";
                cubeState.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + CUBES_TABLE_NAME +  "(" + NAME_COLUMN + ", " + TIME_COLUMN + ")" + " VALUES ('Fakhri Raihaan (using his feet)', 27.93)";
                cubeState.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + CUBES_TABLE_NAME +  "(" + NAME_COLUMN + ", " + TIME_COLUMN + ")" + " VALUES ('Ruxin Liu (age 3)', 99.33)";
                cubeState.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + CUBES_TABLE_NAME +  "(" + NAME_COLUMN + ", " + TIME_COLUMN + ")" + " VALUES ('Mats Valk (human record holder)', 6.27)";
                cubeState.executeUpdate(addDataSQL);
            }
            return true;

        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }

    private static boolean CubeExists() throws SQLException {

        String checkTablePresentQuery = "SHOW TABLES LIKE '" + CUBES_TABLE_NAME + "'";
        ResultSet tablesRS = cubeState.executeQuery(checkTablePresentQuery);
        if (tablesRS.next()) {    //If ResultSet has a next row, it has at least one row... that must be our table
            return true;
        }
        return false;

    }

    //CLosing the statement, the result set and the connection
    public static void shutdown(){
        try {
            if (Rs != null) {
                Rs.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (cubeState != null) {
                cubeState.close();
                System.out.println("Statement closed");
            }
        } catch (SQLException se){
            //Closing the connection could throw an exception too
            se.printStackTrace();
        }

        try {
            if (cubCon != null) {
                cubCon.close();
                System.out.println("Database connection closed");
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
