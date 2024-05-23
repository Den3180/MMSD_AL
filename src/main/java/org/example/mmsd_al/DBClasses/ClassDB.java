package org.example.mmsd_al.DBClasses;
import java.sql.*;

public class ClassDB {

    public final int Version=7;
    private static String areaName="Технопром";
    private static Connection conn;

    public static  boolean create(String pathDB){

        try {
            Connection connection= DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement=connection.createStatement();
            statement.executeUpdate("create table person (id integer, name string)");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    //region Setters and Getters
    public static String getAreaName() {
        return areaName;
    }

    public static void setAreaName(String areaName) {
        ClassDB.areaName = areaName;
    }
    //endregion
}
