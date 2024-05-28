package org.example.mmsd_al.DevicesClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.MainWindow;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Класс устройств.
 */
@JsonIgnoreProperties(value = "_DTAct", allowSetters = true)
public class ClassDevice {

    private int id;

    private String _Name;
    private int _Address;
    private EnumProtocol _Protocol;
    private int _Period;
    private String _IPAddress;
    private int _IPPort;
    private EnumLink _LinkState;
    private int _TxCounter;
    private int _RxCounter;
    private int _PacketLost;
    private LocalDate _DTAct;
    private String _ComPort;
    private String _SIM;
    private EnumModel _Model;
    private LocalDate _DTConnect;
    private boolean _WaitAnswer;
    private String _Picket;
    private double _Latitude =  00.000000D;
    private double _Longitude = 00.000000D;
    private double _Elevation;
    private String _LinkStateName;
    private String _PacketStatistics;
    private int countNumber;
    private String _ProtocolName;
    private String _ModelName;
    private List<ClassChannel> channels;

    //region Setters and Getters

    public String get_ModelName() {
        return _ModelName;
    }

    public void set_ModelName(EnumModel model) {
        this._ModelName = switch (model){
            case None -> "";
            case BKM_3 -> "БКМ-3";
            case BKM_4 -> "БКМ-4";
            case SKZ -> "СКЗ";
            case SKZ_IP -> "СКЗ-ИП";
            case BSZ -> "БСЗЭ";
            case USIKP -> "УСИКП";
            case BKM_5 -> "БКМ-5";
            case KIP -> "КИП";
        };
    }

    public void set_ProtocolName(EnumProtocol protocol) {
        switch (protocol){
            case RTU -> _ProtocolName="Modbus RTU";
            case TCP -> _ProtocolName="Modbus TCP";
            case SMS -> _ProtocolName="GSM SMS";
            case GPRS -> _ProtocolName="GPRS";
            case GPRS_SMS -> _ProtocolName="GPRS SMS";
            default -> _ProtocolName="не известно";
        }
    }

    public String get_ProtocolName() {
        return _ProtocolName;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void set_Name(String _Name){
        this._Name=_Name;
    }

    public int get_Address() {
        return _Address;
    }

    public void set_Address(int _Address) {
        this._Address = _Address;
    }

    public EnumProtocol get_Protocol() {
        return _Protocol;
    }

    public void set_Protocol(EnumProtocol _Protocol) {
        this._Protocol = _Protocol;
        set_ProtocolName(_Protocol);
    }

    public int get_Period() {
        return _Period;
    }

    public void set_Period(int _Period) {
        this._Period = _Period;
    }

    public String get_IPAddress() {
        return _IPAddress;
    }

    public void set_IPAddress(String _IPAddress) {
        this._IPAddress = _IPAddress;
    }

    public int get_IPPort() {
        return _IPPort;
    }

    public void set_IPPort(int _IPPort) {
        this._IPPort = _IPPort;
    }

    public String get_ComPort() {
        return _ComPort;
    }

    public void set_ComPort(String _ComPort) {
        this._ComPort = _ComPort;
    }

    public String get_SIM() {
        return _SIM;
    }

    public void set_SIM(String _SIM) {
        this._SIM = _SIM;
    }

    public EnumModel get_Model() {
        return _Model;
    }

    public void set_Model(EnumModel _Model) {
        this._Model = _Model;
        set_ModelName(_Model);
    }

    public String get_Picket() {
        return _Picket;
    }

    public void set_Picket(String _Picket) {
        this._Picket = _Picket;
    }

    public double get_Longitude() {
        return _Longitude;
    }

    public void set_Longitude(double _Longitude) {
        this._Longitude = _Longitude;
    }

    public double get_Latitude() {
        return _Latitude;
    }

    public void set_Latitude(double _Latitude) {
        this._Latitude = _Latitude;
    }

    public double get_Elevation() {
        return _Elevation;
    }

    public void set_Elevation(double _Elevation) {
        this._Elevation = _Elevation;
    }

    public LocalDate get_DTAct() {
        return _DTAct;
    }

    public void set_DTAct(LocalDate _DTAct) {
        this._DTAct = _DTAct;
    }

    public int getCountNumber(){
        return countNumber;
    }

    public void setCountNumber(int countNumber) {
        this.countNumber = countNumber;
    }

    public EnumLink get_LinkState(){
        return _LinkState;
    }

    public String get_LinkStateName() {
        switch (get_LinkState()){
            case LinkNo: return "Нет связи";
            case LinkYes: return  "На связи";
            case LinkConnect: return "Подключение";
            default:return  "Неизвестно";
        }
    }

    public void set_LinkStateName(String _LinkStateName) {
        this._LinkStateName = _LinkStateName;
    }

    public String ProtocolName(){
        switch (_Protocol){
            case RTU: return "Modbus RTU";
            case TCP: return "Modbus TCP";
            case SMS: return "GSM SMS";
            case GPRS: return "GPRS";
            case GPRS_SMS: return "GPRS SMS";
            default: return "не известно";
        }
    }

    public String get_Name() {
        return _Name;
    }

    public int get_TxCounter() {
        return _TxCounter;
    }

    public int get_RxCounter() {
        return _RxCounter;
    }

    public boolean get_WaitAnswer() {
        return _WaitAnswer;
    }

    public List<ClassChannel> getChannels() {
        return channels;
    }
    public void setChannels(List<ClassChannel> channels) {
        this.channels = channels;
    }

    public void set_PacketStatistics(String _PacketStatistics) {
        this._PacketStatistics = "0/0";
    }

    public String get_PacketStatistics() {
        return _TxCounter+"/"+_RxCounter;
    }

    public String commParam(){
        if ((_Protocol == EnumProtocol.TCP) || (_Protocol == EnumProtocol.GPRS)
                || (_Protocol == EnumProtocol.GPRS_SMS))        return _IPAddress + ":" + _IPPort;
        else if (_ComPort != "")                                return _ComPort;
        else if (_Protocol == EnumProtocol.SMS)
                                                                return "COM" + MainWindow.settings.getPortModem();
        else if (MainWindow.settings.getPortModbus() == 0)           return "Нет";
        else                                                    return "COM" + MainWindow.settings.getPortModbus();
    }

    public String Modelname(){
        switch (_Model){
            case BKM_3: return "БКМ-3";
            case BKM_4: return "БКМ-4";
            case SKZ: return "СКЗ";
            case SKZ_IP: return "СКЗ-ИП";
            case BSZ: return "БСЗЭ";
            case USIKP: return "УСИКП";
            case BKM_5:return "БКМ-5";
            case KIP:return "КИП";
            default: return "не известно";
        }
    }


    //endregion

    public ClassDevice(){
        id=0;
        channels=new ArrayList<ClassChannel>();
        _Protocol=EnumProtocol.RTU;
        _Period=60;
        _IPAddress="192.168.0.1";
        _IPPort=502;
        _Address=1;
        _LinkState=EnumLink.Unknown;
        _ComPort = "";
        _SIM = "";
        _Model = EnumModel.None;
        _DTConnect = LocalDate.MIN;
        _WaitAnswer = false;
        _Picket = "";
       // _ProtocolName=ProtocolName();
    }

    /**
     * Счетчик запросов.
     */
    public void packetSended(){
        _TxCounter++;
        if (_TxCounter > 10000) _TxCounter = 0;
        //OnPropertyChanged("PacketStatistics");
        _WaitAnswer = true;
    }

    /**
     * Увеличение счетчика пакетов, выставление индикации, если пришел пакет, добавление события.
     */
    public void PacketReceived()
    {
        _WaitAnswer = false;
        _RxCounter++;
        if (_RxCounter > 10000) _RxCounter = 0;
        //OnPropertyChanged("PacketStatistics");
        if (_LinkState != EnumLink.LinkYes)
        {
            _LinkState = EnumLink.LinkYes;
            //OnPropertyChanged("LinkState");
            //OnPropertyChanged("LinkStateName");
        }
        _PacketLost = 0;
        _DTAct = LocalDate.now();
    }

    /**
     * Сохранить профиль устройства в файл(XML-формат).
     * @param pathFile
     * @return boolean
     */
    public boolean saveProfile(String pathFile){
        try {
            File file=new File(pathFile);
            XmlMapper xmlMapper=new XmlMapper();
            xmlMapper.writeValue(file, this);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    /**
     * Загрузить профиль устройства из файла.
     * @param pathFile
     * @return ClassDevice
     */
    public static ClassDevice load(String pathFile){
        File file=new File(pathFile);
        AtomicReference<ClassDevice> device = new AtomicReference<>();
        if(file.exists()){
            try{
                XmlMapper xmlMapper = new XmlMapper();
                device.set(xmlMapper.readValue(file, ClassDevice.class));
                return device.get();
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return device.get();
    }

    @Override
    public String toString(){
        return this.get_Name();
    }

    //region Перечисления
    /**
     * Перечисление статусов подключения.
     */
    public enum EnumLink{
        Unknown,
        LinkNo,
        LinkYes,
        LinkConnect;
    }
    /**
     * Перечисление протоколов подключения.
     */
    public enum EnumProtocol
    {
        None,
        RTU,
        TCP,
        SMS,
        GPRS,
        GPRS_SMS
    }


    /**
     * Перечисление типов устройств.
     */
    public enum  EnumModel{
        None,
        BKM_3,
        BKM_4,
        SKZ,
        SKZ_IP,
        BSZ,
        USIKP,
        BKM_5,
        KIP;

    }
    //endregion
}
