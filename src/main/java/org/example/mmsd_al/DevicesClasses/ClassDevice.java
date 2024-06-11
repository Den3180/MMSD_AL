package org.example.mmsd_al.DevicesClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.MainWindow;

import java.io.File;
import java.nio.channels.Channels;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Класс устройств.
 */
@JsonIgnoreProperties(value = {"_DTAct","groups"}, allowSetters = true)
public class ClassDevice {

    private int id;

    private List<ClassGroupRequest> groups;

    private SimpleStringProperty _Name;
    private int _Address;
    private EnumProtocol _Protocol;
    private SimpleIntegerProperty _Period;
    private SimpleStringProperty _IPAddress;
    private int _IPPort;
    private EnumLink _LinkState;
    private int _TxCounter;
    private int _RxCounter;
    private int _PacketLost;
    private LocalDate _DTAct;
    private SimpleStringProperty _ComPort;
    private SimpleStringProperty _SIM;
    private EnumModel _Model;
    private LocalDate _DTConnect;
    private boolean _WaitAnswer;
    private SimpleStringProperty _Picket;
    private SimpleDoubleProperty _Latitude;
    private SimpleDoubleProperty _Longitude;
    private SimpleDoubleProperty _Elevation;
    private SimpleStringProperty _LinkStateName;
    private SimpleStringProperty _PacketStatistics;
    private int countNumber;
    private SimpleStringProperty _ProtocolName;
    private SimpleStringProperty _ModelName;
    private List<ClassChannel> channels;
    private int counGroup;
    private boolean inProcess;


    //region Setters and Getters


    public void setGroups(List<ClassGroupRequest> groups) {
        this.groups = groups;
    }

    public List<ClassGroupRequest> getGroup(){
        return  this.groups;
    }

    public void setInProcess(boolean inProcess) {
        this.inProcess = inProcess;
    }

    public boolean getInProcess(){
        return this.inProcess;
    }


    public void setCounGroup(int counGroup) {
        this.counGroup = counGroup;
    }

    public int getCounGroup() {
        return counGroup;
    }

    public String get_ModelName() {
        return _ModelName.get();
    }

    public void set_ModelName(EnumModel model) {
        String modName = switch (model){
            case None -> "";
            case BKM_3 -> "БКМ-3";
            case BKM_4 -> "БКМ-4";
            case SKZ -> "СКЗ";
            case SKZ_IP -> "СКЗ-ИП";
            case BSZ -> "БСЗЭ";
            case USIKP -> "УСИКП";
            case BKM_5 -> "БКМ-5";
            case KIP -> "КИП";
            case MMPR -> "ММПР";
        };
        this._ModelName.set(modName);
    }

    public SimpleStringProperty _ModelNameProperty() {
        return _ModelName;
    }

    public void set_ProtocolName(EnumProtocol protocol) {
        String protName= switch (protocol){
            case RTU -> "Modbus RTU";
            case TCP -> "Modbus TCP";
            case SMS -> "GSM SMS";
            case GPRS -> "GPRS";
            case GPRS_SMS ->"GPRS SMS";
            default -> "не известно";
        };
        _ProtocolName.set(protName);
    }

    public String get_ProtocolName() {
        return _ProtocolName.get();
    }

    public SimpleStringProperty _ProtocolNameProperty() {
        return _ProtocolName;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void set_Name(String _Name){
        this._Name.set(_Name);
    }

    public String get_Name() {
        return _Name.get();
    }

    public SimpleStringProperty _NameProperty() {
        return _Name;
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
        return _Period.get();
    }

    public void set_Period(int _Period) {
        this._Period.set(_Period);
    }

    public SimpleIntegerProperty _PeriodProperty() {
        return _Period;
    }

    public String get_IPAddress() {
        return _IPAddress.get();
    }

    public void set_IPAddress(String _IPAddress) {
        this._IPAddress.set(_IPAddress);
    }

    public SimpleStringProperty _IPAddressProperty() {
        return _IPAddress;
    }

    public int get_IPPort() {
        return _IPPort;
    }

    public void set_IPPort(int _IPPort) {
        this._IPPort = _IPPort;
    }

    public String get_ComPort() {
        return _ComPort.get();
    }

    public void set_ComPort(String _ComPort) {
        this._ComPort.set(_ComPort);
    }

    public SimpleStringProperty _ComPortProperty() {
        return _ComPort;
    }

    public String get_SIM() {
        return _SIM.get();
    }

    public void set_SIM(String _SIM) {
        this._SIM.set(_SIM);
    }

    public SimpleStringProperty _SIMProperty() {
        return _SIM;
    }

    public EnumModel get_Model() {
        return _Model;
    }

    public void set_Model(EnumModel _Model) {
        this._Model = _Model;
        set_ModelName(_Model);
    }

    public String get_Picket() {
        return _Picket.get();
    }

    public void set_Picket(String _Picket) {
        this._Picket.set(_Picket);
    }

    public SimpleStringProperty _PicketProperty() {
        return _Picket;
    }

    public double get_Longitude() {
        return _Longitude.get();
    }

    public void set_Longitude(double _Longitude) {
        this._Longitude.set(_Longitude);
    }

    public SimpleDoubleProperty _LongitudeProperty() {
        return _Longitude;
    }

    public double get_Latitude() {
        return _Latitude.get();
    }

    public void set_Latitude(double _Latitude) {
        this._Latitude.set(_Latitude);
    }

    public SimpleDoubleProperty _LatitudeProperty() {
        return _Latitude;
    }

    public double get_Elevation() {
        return _Elevation.get();
    }

    public void set_Elevation(double _Elevation) {
        this._Elevation.set(_Elevation);
    }

    public SimpleDoubleProperty _ElevationProperty() {
        return _Elevation;
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
        String lnk= switch (get_LinkState()){
            case LinkNo-> "Нет связи";
            case LinkYes-> "На связи";
            case LinkConnect->"Подключение";
            case Unknown-> "Неизвестно";
        };
        _LinkStateName.set(lnk);
        return _LinkStateName.get();
    }

    public void set_LinkStateName(String _LinkStateName) {
        this._LinkStateName.set(_LinkStateName);
    }

    public SimpleStringProperty _LinkStateNameProperty() {
        return _LinkStateName;
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
        this._PacketStatistics.set(_PacketStatistics);
    }

    public String get_PacketStatistics() {
        this._PacketStatistics.set(_TxCounter+"/"+_RxCounter);
        return _PacketStatistics.get() ;
    }

    public SimpleStringProperty _PacketStatisticsProperty() {
        return _PacketStatistics;
    }

    public String commParam(){
        if ((_Protocol == EnumProtocol.TCP) || (_Protocol == EnumProtocol.GPRS)
                || (_Protocol == EnumProtocol.GPRS_SMS))   return _IPAddress.get() + ":" + _IPPort;
        else if (_ComPort.isEmpty().get())                 return _ComPort.get();
        else if (_Protocol == EnumProtocol.SMS)            return "COM" + MainWindow.settings.getPortModem();
        else if (MainWindow.settings.getPortModbus() == 0) return "Нет";
        else                                               return "COM" + MainWindow.settings.getPortModbus();
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
        _Name=new SimpleStringProperty();
        _ModelName=new SimpleStringProperty();
        channels=new ArrayList<ClassChannel>();
        groups=new ArrayList<>();
        _Protocol=EnumProtocol.RTU;
        _Period=new SimpleIntegerProperty(60);
        _IPAddress=new SimpleStringProperty("192.168.0.1");
        _IPPort=502;
        _Address=1;
        _LinkState=EnumLink.Unknown;
        _ComPort = new SimpleStringProperty();
        _SIM =new SimpleStringProperty();
        _Model = EnumModel.None;
        _DTConnect = LocalDate.MIN;
        _WaitAnswer = false;
        _Picket = new SimpleStringProperty();
        _Latitude=new SimpleDoubleProperty();
        _Longitude=new SimpleDoubleProperty();
        _Elevation=new SimpleDoubleProperty();
        _ProtocolName=new SimpleStringProperty();
        _PacketStatistics=new SimpleStringProperty("0/0");
        _LinkStateName=new SimpleStringProperty("Неизвестно");
        inProcess=false;
    }

    /**
     * Счетчик запросов.
     */
    public void packetSended(){
        _TxCounter++;
        if (_TxCounter > 10000) _TxCounter = 0;
        //OnPropertyChanged("PacketStatistics");
        this._PacketStatistics.set(_TxCounter+"/"+_RxCounter);
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
        this._PacketStatistics.set(_TxCounter+"/"+_RxCounter);
        //OnPropertyChanged("PacketStatistics");
        if (_LinkState != EnumLink.LinkYes)
        {
            //OnPropertyChanged("LinkState");
            //OnPropertyChanged("LinkStateName");
            _LinkState=EnumLink.LinkYes;
            get_LinkStateName();
//            _LinkStateName.setValue(_LinkStateName.get());
        }
        _PacketLost = 0;
        _DTAct = LocalDate.now();
    }

    /**
     * Счетчик потеряных пакетов.
     */
    public void PacketNotReceived() {
        int PACKET_LOST_MAX = 1000;
        int PACKET_LOST_ALARM = 3;
        _WaitAnswer=false;
        if(_PacketLost<PACKET_LOST_MAX) _PacketLost++;
        if(_PacketLost<PACKET_LOST_ALARM) return;
        if(_LinkState!=EnumLink.LinkNo){
            _LinkState=EnumLink.LinkNo;
            get_LinkStateName();
        }
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

    /**
     * Разбиение списка регистров устройства на группы по 100 или по типу.
     * Этими группами будет проводиться опрос.
     * @return
     */
    public List<ClassGroupRequest> getGroups() {
        //Максимальное количество в группе.
        int MaxCount = 100;
        //Максимально допустимый интервал между адресами регистров в группе.
        int MaxSpace = 1;
        //Коллекция групп регистров.
        List<ClassGroupRequest> Groups = new ArrayList<ClassGroupRequest>();
        //Список каналов устройства.
        List<ClassChannel> lstChannels = new ArrayList<ClassChannel>(channels);
        //Выход, если список каналов пуст.
        if(lstChannels.size()==0) return Groups;
        //Добавляем новую группу. Аргумент: тип первого регистра списка каналов.
        Groups.add(new ClassGroupRequest(lstChannels.get(0).get_TypeRegistry()));
        //Проходим по списку каналов устройства, проверяем их на соответсвие MaxCount
        //и MaxSpace и тип регистра и добавляем в группы запроса.
        for(ClassChannel item : lstChannels){
         ClassGroupRequest group=Groups.get(Groups.size()-1);
         if(!group.get_TypeRegistry().equals(item.get_TypeRegistry()) || (group.GetSize()>MaxCount) ||
            (item.get_Address()-group.getLastAddress())>MaxSpace){
             Groups.add(new ClassGroupRequest(item.get_TypeRegistry()));
            }
         Groups.get(Groups.size()-1).addChannel(item);
        }
        counGroup=Groups.size();
        return Groups;
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
        KIP,
        MMPR


    }
    //endregion
}
