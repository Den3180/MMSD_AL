package org.example.mmsd_al.DBClasses;
import javafx.beans.property.SimpleStringProperty;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.Settings.ClassSettings;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class ClassDB {

    //<editor-fold desc="Переменные">
    private static String areaName="Технопром";
    private static Connection conn;
    public static final int Version=7;
    private static String defaultPath="pkm.db";
    private static Statement statement;
    //</editor-fold>

    //<editor-fold desc="Создание базы">

    /**
     * Создать базу данных.
     * @param pathDB путь к базе данных
     * @return boolean
     */
    public static  boolean create(String pathDB){

        if(pathDB==null || pathDB.isEmpty()) pathDB = new String(defaultPath);
        File file = new File(pathDB);
        if(file.exists()) file.delete();
        try {
            conn= DriverManager.getConnection("jdbc:sqlite:"+pathDB);
            statement=conn.createStatement();
            statement.executeUpdate("create table info (version INTEGER, lpu TEXT)");
            statement.executeUpdate("INSERT INTO info (version) VALUES(" + Version + ")");
            statement.executeUpdate("CREATE TABLE dev(name TEXT, prot INTEGER, ip_adr TEXT, ip_port INTEGER,"
                    + " address INTEGER, period INTEGER, port TEXT, sim TEXT, model INTEGER, lat REAL, longt REAL, elev REAL, picket TEXT)");
            statement.executeUpdate("CREATE TABLE reg(name TEXT, dev INTEGER, type INTEGER, adr INTEGER,"
                    + " format INTEGER, k NUMERIC, vmax NUMERIC, vmin NUMERIC, rec INTEGER, val NUMERIC,"
                    + " dt TEXT, ext INTEGER, accuracy INTEGER, nval NUMERIC)");
            statement.executeUpdate("CREATE TABLE map(name TEXT, json TEXT)");
            statement.executeUpdate("CREATE TABLE obj(type INTEGER, map INTEGER, x INTEGER, y INTEGER,"
                    + " prop TEXT)");
            statement.executeUpdate("CREATE TABLE cmd(name TEXT, type INTEGER, dev INTEGER, adr INTEGER,"
                    + " val INTEGER, period INTEGER)");
            statement.executeUpdate("CREATE TABLE link(event INTEGER, src INTEGER, cmd INTEGER)");
            statement.executeUpdate("CREATE TABLE user(name TEXT, login TEXT, pass TEXT, grant INT)");
            statement.executeUpdate("CREATE TABLE log(type INTEGER, dt TEXT, param TEXT, val TEXT,"
                    + " ack INTEGER, namdev TEXT)");
            statement.executeUpdate("CREATE TABLE mes(type INTEGER, buf BLOB)");
            statement.executeUpdate("CREATE TABLE sub (var TEXT, val INTEGER, txt TEXT, fcolor INTEGER,"
                    + " bcolor INTEGER)");
            statement.executeUpdate("CREATE TABLE az (Identity TEXT,Location TEXT, TypeOfConstruction TEXT,"
                    + "ElectrodeMaterial TEXT, CurrentLoad REAL, DissolutionRate REAL, SurfaceArea REAL, WeightAZ REAL, LenghtAZ INTEGER,"
                    + "Diagonal INTEGER, MassAssembly REAL, MountingMethod TEXT, ServiceLife INTEGER, KMA TEXT)");
            statement.executeUpdate("CREATE TABLE ehz1 (daterec TEXT, namelpu TEXT, nameobj TEXT, km TEXT, ukz INTEGER, skz INTEGER, typeskz TEXT," +
                    " dateinskz TEXT, methodground TEXT, typeground TEXT, elmeter TEXT, typecontrol TEXT, bsz TEXT)");
            statement.executeUpdate("CREATE TABLE ehz4(daterec TEXT, namelpu TEXT, nameobj TEXT, upz TEXT, lastrepair TEXT, location TEXT," +
                    " typeprotect TEXT, numprotect INTEGER, startupz TEXT)");
            statement.executeUpdate("CREATE TABLE ehz6(daterec TEXT, namelpu TEXT, nameobj TEXT, actpit TEXT, datepit TEXT, km TEXT, gps TEXT, " +
                    "lenghtpit INTEGER, dnar INTEGER, thickness INTEGER, reasonpit TEXT, deep REAL, typeground TEXT, soilresi INTEGER, " +
                    "insulatiomat TEXT, adhesiomat TEXT, damagearea INTEGER, nutcorrdamage TEXT, maxdeepdamage INTEGER, damage1mm INTEGER," +
                    "damage13 INTEGER, damage3 INTEGER, ute TEXT)");
            statement.executeUpdate("CREATE TABLE ehz8q(daterec TEXT, namelpu TEXT, nametube TEXT, km TEXT, nameroad TEXT, typeroad TEXT," +
                    " datemeasure TEXT, ustart NUMERIC, upatr NUMERIC, usvech NUMERIC, uend NUMERIC, upatrend NUMERIC, usvechend NUMERIC, " +
                    "resis INTEGER, note TEXT)");
            statement.executeUpdate("CREATE TABLE f02year(daterec TEXT,namelpu TEXT,namejbj TEXT, km REAL, typeehz TEXT, numukz TEXT, " +
                    "stateukz TEXT,tokukz REAL,numskz TEXT,tokmedskz REAL,uskz REAL,azresist REAL,numupz TEXT,stateupz TEXT,tokupz REAL," +
                    "numudz TEXT,stateudz TEXT,tokudz REAL)");
            statement.executeUpdate("CREATE TABLE f141gas (datarec TEXT,namelpu TEXT,nameobj TEXT,number INTEGER, typework TEXT, " +
                    "smrpot REAL,smrplan REAL, smrfact REAL, mtrpot REAL, mtrplan REAL, mtrfact REAL, pirpot REAL, pirplan REAL," +
                    " pirfact REAL, ukzpot INTEGER, ukzplan INTEGER, ukzfact INTEGER, vlpot INTEGER, vlplan INTEGER, vlfact INTEGER," +
                    " udzpot INTEGER, udzplan INTEGER, udzfact INTEGER, upzpot INTEGER, upzplan INTEGER, upzfact INTEGER," +
                    " vstavkapot INTEGER, vstavkaplan INTEGER, vstavkafact INTEGER, kippot INTEGER, kipplan INTEGER, kipfact INTEGER," +
                    " istochtokpot INTEGER, istochtokplan INTEGER, istochtokfact INTEGER, prochpot INTEGER, prochplan INTEGER, " +
                    " prochfact INTEGER, note TEXT)");
            statement.executeUpdate("CREATE TABLE f40year(daterec TEXT,namelpu TEXT,nameobj TEXT,singlepipe INTEGER,countKIP INTEGER," +
                    "countUKZ INTEGER,countUDZ INTEGER,countUPZ INTEGER,countsource  INTEGER,countins INTEGER,lengthLEP INTEGER," +
                    "countSKZtime INTEGER,countSKZtele INTEGER,electricity INTEGER, electricityload INTEGER,rejectionLEP INTEGER," +
                    "rejectionEHZ INTEGER,rejectionSUM INTEGER,securitysinglepipe INTEGER,securityspKPZ INTEGER," +
                    " securitylenght TEXT,securitytime TEXT, securityKPZ TEXT,securityUvom TEXT,lenghtVKO INTEGER," +
                    "lenghtPKO INTEGER,transresisMax INTEGER,transresisMin INTEGER)");
            statement.executeUpdate("CREATE TABLE f41year(Field2 TEXT,Field3 TEXT,Field4 TEXT,Field5 REAL,Field6 REAL,Field7 REAL," +
                    "Field8 INTEGER,Field9 TEXT,Field10 TEXT,Field11 TEXT,Field12 REAL,Field13 REAL,Field14 REAL,Field15 REAL," +
                    "Field16 REAL,Field17 REAL,Field18 INTEGER,Field19 INTEGER,Field20 INTEGER,Field21 INTEGER,Field22 INTEGER," +
                    "Field23 INTEGER,Field24 INTEGER,Field25 INTEGER,Field26 INTEGER,Field27 INTEGER,Field28 INTEGER,Field29 INTEGER," +
                    "Field30 REAL,Field31 REAL,Field32 TEXT,Field33 REAL,Field34 REAL,Field35 TEXT,Field36 REAL,Field37 INTEGER," +
                    "Field38 INTEGER,Field39 TEXT)");
            statement.executeUpdate("CREATE TABLE f52gas(Field1 TEXT,Field2 TEXT,Field3 TEXT,Field4 TEXT,Field5 TEXT,Field6 TEXT," +
                    "Field7 REAL,Field8 INTEGER,Field9 INTEGER,Field10 INTEGER,Field11 INTEGER,Field12 INTEGER,Field13 INTEGER," +
                    "Field14 INTEGER,Field15 INTEGER,Field16 INTEGER,Field17 INTEGER,Field18 INTEGER,Field19 INTEGER,Field20 INTEGER," +
                    "Field21 INTEGER,Field22 INTEGER,Field23 INTEGER,Field24 INTEGER,Field25 INTEGER,Field26 INTEGER,Field27 INTEGER," +
                    "Field28 INTEGER,Field29 INTEGER,Field30 INTEGER,Field31  INTEGER,Field32 INTEGER,Field33 INTEGER,Field34 INTEGER," +
                    "Field35 INTEGER,Field36 INTEGER,Field37 INTEGER)");
            statement.executeUpdate("CREATE TABLE skz(id INTEGER NOT NULL,unomin REAL,nactivin REAL,nfullin REAL,unomout REAL," +
                    "inomout REAL,nnomout REAL,fcode TEXT,fnumber INTEGER,modulescount INTEGER,fyear TEXT, yearstart TEXT," +
                    "resource INTEGER, PRIMARY KEY(id))");
            statement.executeUpdate("CREATE TABLE vei(daterec TEXT, namelpu TEXT, typeobj TEXT, location TEXT, km TEXT, typeflange TEXT, " +
                    "placement TEXT, datebegin TEXT, manufactory TEXT, datemade TEXT, serial TEXT, dnar INTEGER, pmax INTEGER, " +
                    "status TEXT, kip TEXT, spark TEXT, bsz TEXT)");
            statement.close();
            conn.close();
        } catch (SQLException e) {

        }
        return true;
    }

    /**
     * Подключение к базе данных.
     * @param settings настройки подключения
     * @return boolean
     */
    public boolean open(ClassSettings settings){
        try {
            conn= DriverManager.getConnection("jdbc:sqlite:"+settings.getdB());
            statement=conn.createStatement();
            return true;
        }
        catch (Exception ex){
            return false;
        }
    }
    //</editor-fold>

    /**
     * Получить текущую версию базы данных.
     * @return int
     */
    public int infoLoad(){
        int id=0;
        try{
                ResultSet resultSet= statement.executeQuery("SELECT version FROM info");
                while (resultSet.next()){
                   id=resultSet.getInt("version");
                   return id;
                }
            }
        catch (Exception ex){
        }
        return id;
    }

    /**
     * Получить идентификатор(обозначение) ЛПУ
     * @return
     */
    public String infoArea(){
        try {
            SimpleStringProperty lpu=new SimpleStringProperty();
            ResultSet resultSet=statement.executeQuery("SELECT lpu FROM info");
            while (resultSet.next()){
                lpu.set(resultSet.getString("lpu"));
            }
            return lpu.getValue()==null? "нет данных": lpu.get() ;
        }
        catch(Exception ex){
        }
        return "нет данных";
    }

    //<editor-fold desc="Devices">

    /**
     * Загрузиьт устройства из базы данных.
     * @return
     */
    public ArrayList<ClassDevice> devicesLoad(){
        ArrayList<ClassDevice> lst=new ArrayList<>();
        try{
            ResultSet resultSet= statement.executeQuery("SELECT rowid, * FROM dev ORDER BY name");
            while (resultSet.next()){
                ClassDevice dev=new ClassDevice();
                dev.setCountNumber(lst.size()+1);
                dev.setId(resultSet.getInt("rowid"));
                dev.set_Name(resultSet.getString("name"));
                dev.set_Protocol(ClassDevice.EnumProtocol.values()[resultSet.getInt("prot")]);
                dev.set_Period(resultSet.getInt("period"));
                dev.set_IPAddress(resultSet.getString("ip_adr"));
                dev.set_IPPort(resultSet.getInt("ip_port"));
                dev.set_Address(resultSet.getInt("address"));
                dev.set_ComPort(resultSet.getString("port"));
                dev.set_SIM(resultSet.getString("sim"));
                dev.set_Model(ClassDevice.EnumModel.values()[resultSet.getInt("model")]);
                dev.set_Latitude(resultSet.getDouble("lat"));
                dev.set_Longitude(resultSet.getDouble("longt"));
                dev.set_Elevation(resultSet.getDouble("elev"));
                dev.set_Picket(resultSet.getString("picket"));
                lst.add(dev);
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return lst;
    }
    //</editor-fold>

    //<editor-fold desc="Setters and Getters">
    public static String getAreaName() {
        return areaName;
    }

    public static void setAreaName(String areaName) {
        ClassDB.areaName = areaName;
    }
    //</editor-fold>

}