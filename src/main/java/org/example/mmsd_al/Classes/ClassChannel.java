package org.example.mmsd_al.Classes;

import org.example.mmsd_al.DevicesClasses.ClassDevice;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class ClassChannel {

    private int id;

    private String _Name;

    private SimpleDateFormat _DTAct;
    private int _Address;
    private EnumFormat _Format;
    private float _Koef;
    private double _Value;
    private double _Max;
    private double _Min;
    private EnumState _State;
    private EnumTypeRegistry _TypeRegistry;
    private ClassDevice _Device;
    private int[] _BaseValue;
    private double _PreviousValue;
    private boolean _Archive;
    private int _Ext;
    private int _Accuracy;
    private double _NValue;


    //<editor-fold desc="Setters and Getters">

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

    public SimpleDateFormat get_DTAct() {
        return _DTAct;
    }

    public void set_DTAct(SimpleDateFormat _DTAct) {
        this._DTAct = _DTAct;
    }

    public String getStrDTAct(){
        //if(_DTAct== ) return "";
       return "";
//        SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//        String rr= sdf.format(Calendar.getInstance().getTime());
    }

    public int get_Address() {
        return _Address;
    }

    public void set_Address(int _Address) {
        this._Address = _Address;
    }

    public String getAddressHex(){
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

    public double get_Value() {
        return _Value;
    }

    public void set_Value(double _Value) {
        this._Value = _Value;
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

    public String getTypeRegistryFulltName(){
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

    public String getDeviceName(){
        return _Device.get_Name();
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
