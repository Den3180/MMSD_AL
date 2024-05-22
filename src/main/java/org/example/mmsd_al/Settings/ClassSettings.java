package org.example.mmsd_al.Settings;

import com.fasterxml.jackson.dataformat.xml.*;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс настроек.
 */
public class ClassSettings implements Serializable {

    private final String FILE_NAME = "pkm.xml";
    private final String PATH_DB = "./pkm.db";
    private EnumTypeDB typeDB;
    private String dB;
    private String server;
    private String login;
    private String password;
    private int portModbus;
    private int portModem;
    private int timeout;
    private boolean requestLogin;
    private boolean recordLog;
    private boolean interpol;
    private List<String> devicesColumns;
    private List<String> channelsColumns;
    private int baudRate;
    private int dataBits;
    private int parity;
    private int stopBits;
    private boolean _interface;
    private int startWindow;
    private boolean modbusSlave;
    private int periodUSIKP;

    public ClassSettings(){
        typeDB = EnumTypeDB.SQLite;
        dB = PATH_DB;
        server = "localhost";
        portModbus = 1;
        portModem = 0;
        timeout = 300;
        requestLogin = false;
        recordLog = false;
        interpol = false;
        baudRate = 9600;
        dataBits = 8;
        parity = 0;
        stopBits = 1;
        _interface = false;
        startWindow = 0;
        modbusSlave = false;
        periodUSIKP=0;
        login="";
        password="";
        devicesColumns = new ArrayList<String>();
        channelsColumns = new ArrayList<String>();
    }

    /**
     * Сохранить настройки в файл.
     */
    public void save(){
        try{
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.writeValue(new File(FILE_NAME), this);
        }
        catch (Exception ex){

        }
    }

    /**
     * Загрузка настроек из файла.
     */
    public static ClassSettings load(){

        String pathFile=new ClassSettings().FILE_NAME;
        File file=new File(pathFile);
        if(file.exists()){
            try{
                XmlMapper xmlMapper = new XmlMapper();
                return xmlMapper.readValue(file, ClassSettings.class);
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return new ClassSettings();
    }

    //region Setters and Getters
    public EnumTypeDB getTypeDB() {
        return typeDB;
    }
    public void setTypeDB(EnumTypeDB typeDB) {
        this.typeDB = typeDB;
    }

    public String getdB() {
        return dB;
    }

    public void setdB(String dB) {
        this.dB = dB;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPortModbus() {
        return portModbus;
    }

    public void setPortModbus(int portModbus) {
        this.portModbus = portModbus;
    }

    public int getPortModem() {
        return portModem;
    }

    public void setPortModem(int portModem) {
        this.portModem = portModem;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isRequestLogin() {
        return requestLogin;
    }

    public void setRequestLogin(boolean requestLogin) {
        this.requestLogin = requestLogin;
    }

    public boolean isRecordLog() {
        return recordLog;
    }

    public void setRecordLog(boolean recordLog) {
        this.recordLog = recordLog;
    }

    public boolean isInterpol() {
        return interpol;
    }

    public void setInterpol(boolean interpol) {
        this.interpol = interpol;
    }

    public List<String> getDevicesColumns() {
        return devicesColumns;
    }

    public void setDevicesColumns(List<String> devicesColumns) {
        this.devicesColumns = devicesColumns;
    }

    public List<String> getChannelsColumns() {
        return channelsColumns;
    }

    public void setChannelsColumns(List<String> channelsColumns) {
        this.channelsColumns = channelsColumns;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public boolean is_interface() {
        return _interface;
    }

    public void set_interface(boolean _interface) {
        this._interface = _interface;
    }

    public int getStartWindow() {
        return startWindow;
    }

    public void setStartWindow(int startWindow) {
        this.startWindow = startWindow;
    }

    public boolean isModbusSlave() {
        return modbusSlave;
    }

    public void setModbusSlave(boolean modbusSlave) {
        this.modbusSlave = modbusSlave;
    }

    public int getPeriodUSIKP() {
        return periodUSIKP;
    }

    public void setPeriodUSIKP(int periodUSIKP) {
        this.periodUSIKP = periodUSIKP;
    }
    //endregion

    public enum EnumTypeDB
    {
        SQLite,
        MSSQL,
        MySQL,
        PostgreSQL;

    }
}
