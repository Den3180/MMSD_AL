package org.example.mmsd_al.Classes;

import javafx.beans.property.*;
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
    private SimpleStringProperty _Name;
    private LocalDateTime _DTAct;
    private SimpleIntegerProperty _Address;
    private SimpleStringProperty _AddressHex;
    private EnumFormat _Format;
    private SimpleFloatProperty _Koef;
    private SimpleDoubleProperty _Value;
    private SimpleDoubleProperty _Max;
    private SimpleDoubleProperty _Min;
    private EnumState _State;
    private EnumTypeRegistry _TypeRegistry;
    private SimpleStringProperty _TypeRegistryFullName;
    private ClassDevice _Device;
    private SimpleStringProperty _DeviceName;
    private int[] _BaseValue;
    private double _PreviousValue;
    private SimpleBooleanProperty _Archive;
    private int _Ext;
    private SimpleIntegerProperty _Accuracy;
    private double _NValue;
    private int _CountNumber;
    private SimpleStringProperty _StrBaseValue;
    private SimpleStringProperty _StrDTAct;

    private SimpleStringProperty minStr;
    private SimpleStringProperty maxStr;
    private SimpleStringProperty accuracyStr;
    private SimpleStringProperty valueStr;




    //<editor-fold desc="Setters and Getters">


    public String get_Name() {
        return _Name.get();
    }

    public void set_Name(String _Name) {
        this._Name.set(_Name);
    }

    public SimpleStringProperty _NameProperty() {
        return _Name;
    }

    public String get_DeviceName() {
        return _DeviceName.get();
    }

    public void set_DeviceName(String _DeviceName) {
        this._DeviceName.set(_DeviceName);
        _Device.set_Name(_DeviceName);
    }

    public SimpleStringProperty _DeviceNameProperty() {
        return _DeviceName;
    }

    public String get_StrDTAct() {
        if(_DTAct==LocalDateTime.MIN ) {
           _StrDTAct.set("");
        }
        else{
            _StrDTAct.set(_DTAct.format(DateTimeFormatter. ofPattern("dd.MM.yyyy HH:mm:ss")));
        }
        return _StrDTAct.get();
    }

    public void set_StrDTAct(String _StrDTAct) {
        this._StrDTAct.set(_StrDTAct);
    }

    public SimpleStringProperty _StrDTActProperty() {
        return _StrDTAct;
    }

    public int get_Address() {
        return _Address.get();
    }

    public SimpleIntegerProperty _AddressProperty() {
        return _Address;
    }

    public void set_Address(int _Address) {
        this._Address.set(_Address);
        this._AddressHex.set("0x"+String.format("%04x",this._Address.get()).toUpperCase());
    }

    public String get_AddressHex() {
        return _AddressHex.get();
    }

    public SimpleStringProperty _AddressHexProperty() {
        return _AddressHex;
    }

    public void set_AddressHex(String _AddressHex) {
        this._AddressHex.set(_AddressHex);
    }

    public float get_Koef() {
        return _Koef.get();
    }

    public SimpleFloatProperty _KoefProperty() {
        return _Koef;
    }

    public void set_Koef(float _Koef) {
        this._Koef.set(_Koef);
    }

    public String get_StrBaseValue() {
        if(_BaseValue==null || _BaseValue.length==0)
        {
            _StrBaseValue.set("");
        }
        else{
            StringBuilder res=new StringBuilder("0x");
            String format="%04x";
           if(_TypeRegistry==EnumTypeRegistry.CoilOutput || _TypeRegistry==EnumTypeRegistry.DiscreteInput){
                format="%02x";
           }
            for(int i=0;i<_BaseValue.length;i++){
                res.append(String.format(format,_BaseValue[i]).toUpperCase());
            }
           _StrBaseValue.set(res.toString());
        }
        return _StrBaseValue.get();
    }

    public SimpleStringProperty _StrBaseValueProperty() {
        return _StrBaseValue;
    }

    public void set_StrBaseValue(String _StrBaseValue) {
        this._StrBaseValue.set(_StrBaseValue);
    }

    public double get_Value() {
        return _Value.get();
    }

    public SimpleDoubleProperty _ValueProperty() {
        return _Value;
    }

    public void set_Value(double value) {
        this._Value.set(convertMinus(value));
        if(_Koef.get()!=1) {
            _Value.set(_Value.get()*(double)_Koef.get());
        }
        if(_Accuracy.get()>0){
            _Value.set((double) Math.round(_Value.get()*100)/100);
        }
        else {
            valueStr.set(String.valueOf((int)get_Value()));
        }

        _DTAct=LocalDateTime.now();
        _StrDTAct.set(_DTAct.format(DateTimeFormatter. ofPattern("dd.MM.yyyy HH:mm:ss")));


//        if(_Name.get()!="" && _Name.get()!="Резерв"){
//            Thread thread=new Thread(()->MainWindow.DB.registrySaveValue(this));
//            thread.setDaemon(true);
//            thread.start();
//        }
    }

    public int get_Accuracy() {
        return _Accuracy.get();
    }

    public SimpleIntegerProperty _AccuracyProperty() {
        return _Accuracy;
    }

    public void set_Accuracy(int _Accuracy) {
        this._Accuracy.set(_Accuracy);
        setAccuracyStr(_Accuracy==Integer.MAX_VALUE ? "" : String.valueOf(_Accuracy));
    }

    public double get_Max() {
        return _Max.get();
    }

    public SimpleDoubleProperty _MaxProperty() {
        return _Max;
    }

    public void set_Max(double _Max) {
        this._Max.set(_Max);
        setMaxStr(((Double)_Max).isNaN() ? "" : String.valueOf(_Max));
    }

    public double get_Min() {
        return _Min.get();
    }

    public SimpleDoubleProperty _MinProperty() {
        return _Min;
    }

    public void set_Min(double _Min) {
        this._Min.set(_Min);
        setMinStr(((Double)_Min).isNaN() ? "" : String.valueOf(_Min));
    }

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

    public LocalDateTime get_DTAct() {
        return _DTAct;
    }

    public void set_DTAct(@NotNull LocalDateTime _DTAct) {
        this._DTAct = _DTAct;
        _StrDTAct.set(_DTAct.format(DateTimeFormatter. ofPattern("dd.MM.yyyy HH:mm:ss")));
    }

    public EnumFormat get_Format() {
        return _Format;
    }

    public void set_Format(EnumFormat _Format) {
        this._Format = _Format;
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

//    public void set_TypeRegistryFullName(String _TypeRegistryFullName) {
//        this._TypeRegistryFullName = _TypeRegistryFullName;
//    }

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



    public int[] get_BaseValue() {
        return _BaseValue;
    }

    public void set_BaseValue(int[] _BaseValue) {
        this._BaseValue = _BaseValue;
        var str=get_StrBaseValue();
        this._StrBaseValue.set(str);
    }

    public double get_PreviousValue() {
        return _PreviousValue;
    }

    public void set_PreviousValue(double _PreviousValue) {
        this._PreviousValue = _PreviousValue;
    }



    public int get_Ext() {
        return _Ext;
    }

    public void set_Ext(int _Ext) {
        this._Ext = _Ext;
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
        _Name= new SimpleStringProperty("Канал 1");
        _Address= new SimpleIntegerProperty(0);
        _TypeRegistry=EnumTypeRegistry.HoldingRegistry;
        _Device=new ClassDevice();
        _Format = EnumFormat.UINT;
        _Koef = new SimpleFloatProperty(1);
        _Value = new SimpleDoubleProperty(0);
        _DTAct = LocalDateTime.MIN;
        _PreviousValue = Double.MIN_VALUE;
        _Archive=new SimpleBooleanProperty(false);
        _Accuracy= new SimpleIntegerProperty(0);
        _CountNumber =0;
        _BaseValue=new int[]{};
        _Max=new SimpleDoubleProperty();
        _Min=new SimpleDoubleProperty();
        _AddressHex=new SimpleStringProperty();
        _StrDTAct=new SimpleStringProperty();
        _StrBaseValue=new SimpleStringProperty();
        _DeviceName=new SimpleStringProperty();

        minStr=new SimpleStringProperty();
        maxStr=new SimpleStringProperty();
        accuracyStr=new SimpleStringProperty();
        valueStr=new SimpleStringProperty();
    }

    //<editor-fold desc="Методы">

    /**
     * Заполнение значений каналов при первой выгрузки из базы данных.
     * @param saveValue значение из БД
     */
    public void loadSaveValue(double saveValue){
        _Value.setValue(saveValue);
        if(get_Koef()==1){
            valueStr.set(String.valueOf((int)saveValue));
        }
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

    public boolean is_Archive() {
        return _Archive.get();
    }

    public SimpleBooleanProperty _ArchiveProperty() {
        return _Archive;
    }

    public void set_Archive(boolean _Archive) {
        this._Archive.set(_Archive);
    }

    public String getMinStr() {
        return minStr.get();
    }

    public SimpleStringProperty minStrProperty() {
        return minStr;
    }

    public void setMinStr(String minStr) {
        this.minStr.set(minStr);
    }

    public String getMaxStr() {
        return maxStr.get();
    }

    public SimpleStringProperty maxStrProperty() {
        return maxStr;
    }

    public void setMaxStr(String maxStr) {
        this.maxStr.set(maxStr);
    }

    public String getAccuracyStr() {
        return accuracyStr.get();
    }

    public SimpleStringProperty accuracyStrProperty() {
        return accuracyStr;
    }

    public void setAccuracyStr(String accuracyStr) {
        this.accuracyStr.set(accuracyStr);
    }

    public String getValueStr() {
        return valueStr.get();
    }

    public SimpleStringProperty valueStrProperty() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr.set(valueStr);
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
