package org.example.mmsd_al.DBClasses;
import java.io.File;
import java.sql.*;

public class ClassDB {

    private static String areaName="Технопром";
    private static Connection conn;
    public final int Version=7;
    private static String defaultPath="pkm.db";

    public static  boolean create(String pathDB){

        if(pathDB==null || pathDB.isEmpty()) pathDB = new String(defaultPath);
        File file = new File(pathDB);
        if(file.exists()) file.delete();
        try {
            Connection connection= DriverManager.getConnection("jdbc:sqlite:"+pathDB);
            Statement statement=connection.createStatement();
            statement.executeUpdate("create table teachers (id integer, name string)");
            statement.executeUpdate("create table students (id integer, name string)");
            statement.close();
            connection.close();
        } catch (SQLException e) {

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
