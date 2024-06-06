package org.example.mmsd_al.Classes;

import javafx.scene.control.TableView;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.MainWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;


public class ClassChannel {

    private int id;
    private String _Name;
    private LocalDateTime _DTAct;
    private int _Address;
    private String _AddressHex;
    private EnumFormat _Format;
    private float _Koef;
    private double _Value;
    private double _Max;
    private double _Min;
    private EnumState _State;
    private EnumTypeRegistry _TypeRegistry;
    private String _TypeRegistryFullName;
    private ClassDevice _Device;
    private String _DeviceName;
    private int[] _BaseValue;
    private double _PreviousValue;
    private boolean _Archive;
    private int _Ext;
    private int _Accuracy;
    private double _NValue;
    private int _CountNumber;
    private String _StrBaseValue;
    private String _StrDTAct;



    //<editor-fold desc="Setters and Getters">


    public void set_CountNumber(int _CountNumber) {
        this._CountNumber = _CountNumber;
    }

    public int get_CountNumber() {
        return _CountNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String get_Name() {
        return _Name;
    }

    public void set_Name(String _Name) {
        this._Name = _Name;
    }

    public LocalDateTime get_DTAct() {
        return _DTAct;
    }

    public void set_DTAct(@NotNull LocalDateTime _DTAct) {
        this._DTAct = _DTAct;
    }

    public void set_StrDTAct(String _StrDTAct) {
        this._StrDTAct = _StrDTAct;
    }

    public String get_StrDTAct() {
        if(_DTAct==LocalDateTime.MIN ) {
            _StrDTAct="";
            return _StrDTAct;
        }
        SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        _StrDTAct= _DTAct.format(DateTimeFormatter. ofPattern("dd.MM.yyyy HH:mm:ss"));
        return _StrDTAct;

    }

    public int get_Address() {
        return _Address;
    }

    public void set_Address(int _Address) {
        this._Address = _Address;
    }

    public void set_AddressHex(String _AddressHex) {
        this._AddressHex = _AddressHex;
    }

    public String get_AddressHex() {
        return "0x"+String.format("%04x",_Address).toUpperCase();
    }

    public EnumFormat get_Format() {
        return _Format;
    }

    public void set_Format(EnumFormat _Format) {
        this._Format = _Format;
    }

    public float get_Koef() {
        return _Koef;
    }

    public void set_Koef(float _Koef) {
        this._Koef = _Koef;
    }

    public void set_StrBaseValue(String _StrBaseValue) {
        this._StrBaseValue = _StrBaseValue;
    }


    public String get_StrBaseValue() {
        if(_BaseValue==null || _BaseValue.length==0) return "";
        StringBuilder res=new StringBuilder("0x");
        String format="%04x";
        if(_TypeRegistry==EnumTypeRegistry.CoilOutput || _TypeRegistry==EnumTypeRegistry.DiscreteInput){
            format="%02x";
        }
        for(int i=0;i<_BaseValue.length;i++){
            res.append(String.format(format,_BaseValue[i]).toUpperCase());
        }
        _StrBaseValue=res.toString();
        return _StrBaseValue;
    }

    public double get_Value() {
        return _Value;
    }

    public void set_Value(double value) {
        _Value = convertMinus(value);
        if(_Koef!=1) {
            _Value*=(double) _Koef;
        }
        else {
            var tt=Math.round(_Value);
            _Value= Math.round(_Value);
        }
        if(_Accuracy>0) _Value=(double) Math.round(_Value*100)/100;
        _DTAct=LocalDateTime.now();
        if(_Name!="" && _Name!="Резерв"){
            //MainWindow.DB.registrySaveValue(this);
        }
        TableView taView=MainWindow.mainWindow.getUserControlChannels();
        if(taView==null) return;
       int index=taView.getItems().indexOf(this);
       if(index<0) return;
        taView.getItems().set(index,this);
       var elem=1;



    }

    public double get_Max() {
        return _Max;
    }

    public void set_Max(double _Max) {
        this._Max = _Max;
    }

    public double get_Min() {
        return _Min;
    }

    public void set_Min(double _Min) {
        this._Min = _Min;
    }

    public EnumState get_State() {
        return _State;
    }

    public void set_State(EnumState _State) {
        this._State = _State;
    }

    public EnumTypeRegistry get_TypeRegistry() {
        return _TypeRegistry;
    }

    public String getTypeRegistryName(){
        return switch (_TypeRegistry){
            case Unknown ->  "не известно";
            case DiscreteInput -> "Discrete Input";
            case CoilOutput -> "Coil Output";
            case InputRegistry -> "Input Registry";
            case HoldingRegistry -> "Holding Registry";
        };
    }

    public String getTypeRegistryShortName(){
        return switch (_TypeRegistry){
            case Unknown ->  "не известно";
            case DiscreteInput -> "DI";
            case CoilOutput -> "DO";
            case InputRegistry -> "AI";
            case HoldingRegistry -> "AO";
        };
    }

    public void set_TypeRegistryFullName(String _TypeRegistryFullName) {
        this._TypeRegistryFullName = _TypeRegistryFullName;
    }

    public String get_TypeRegistryFullName() {
        return switch (_TypeRegistry){
            case Unknown ->  "не известно";
            case DiscreteInput -> "Discrete Input";
            case CoilOutput -> "Coil Output";
            case InputRegistry -> "Input Registry";
            case HoldingRegistry -> "Holding Registry";
        };
    }

    public void set_TypeRegistry(EnumTypeRegistry _TypeRegistry) {
        this._TypeRegistry = _TypeRegistry;
    }

    public ClassDevice get_Device() {
        return _Device;
    }

    public void set_Device(ClassDevice _Device) {
        this._Device = _Device;
    }

    public void set_DeviceName(String _DeviceName) {
        this._DeviceName = _DeviceName;
        _Device.set_Name(_DeviceName);
    }

    public String get_DeviceName() {
        return _DeviceName;
    }

    public int[] get_BaseValue() {
        return _BaseValue;
    }

    public void set_BaseValue(int[] _BaseValue) {
        this._BaseValue = _BaseValue;
    }

    public double get_PreviousValue() {
        return _PreviousValue;
    }

    public void set_PreviousValue(double _PreviousValue) {
        this._PreviousValue = _PreviousValue;
    }

    public boolean get_Archive(){
        return this._Archive;
    }

    public void set_Archive(boolean _Archive) {
        this._Archive = _Archive;
    }

    public int get_Ext() {
        return _Ext;
    }

    public void set_Ext(int _Ext) {
        this._Ext = _Ext;
    }

    public int get_Accuracy() {
        return _Accuracy;
    }

    public void set_Accuracy(int _Accuracy) {
        this._Accuracy = _Accuracy;
    }

    public double get_NValue() {
        return _NValue;
    }

    public void set_NValue(double _NValue) {
        this._NValue = _NValue;
    }
//</editor-fold>

    public ClassChannel(){
        id=0;
        _Name="Канал 1";
        _Address=0;
        _TypeRegistry=EnumTypeRegistry.HoldingRegistry;
        _Device=new ClassDevice();
        _Format = EnumFormat.UINT;
        _Koef = 1;
        _Value = 0;
        _DTAct = LocalDateTime.MIN;
        _PreviousValue = Double.MIN_VALUE;
        _Archive = false;
        _Accuracy=0;
        _CountNumber =0;
        _BaseValue=new int[]{};
    }

    //<editor-fold desc="Методы">

    /**
     * Заполнение значений каналов при первой выгрузки из базы данных.
     * @param saveValue значение из БД
     */
    public void loadSaveValue(double saveValue){
        _Value=saveValue;
    }

    public void sendValue(double value){

    }

    private double convertMinus(double val){

        if (val>32767)
        {
            return val - 65535 - 1;
        }
        return val;
    }
    //</editor-fold>

    //<editor-fold desc="Перечисления">
    /**
     * Тип регистров.
      */
    public enum EnumTypeRegistry
    {
        Unknown,
        DiscreteInput,
        CoilOutput,
        InputRegistry,
        HoldingRegistry
    }

    /** <summary>
    * Формат значения канала.
    */
    public enum EnumFormat
    {
        UINT,
        SINT,
        Float,
        swFloat,
        UInt32
    }

    /**
    * Состояние значения канала.
    */
    public enum EnumState
    {
        Unknown,
        Norma,
        Over,
        Less
    }
    //</editor-fold>
}
