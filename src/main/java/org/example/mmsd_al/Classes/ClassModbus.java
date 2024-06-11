package org.example.mmsd_al.Classes;

import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import javafx.collections.ObservableList;
import jssc.SerialPortList;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.DevicesClasses.ClassGroupRequest;
import org.example.mmsd_al.MainWindow;
import org.example.mmsd_al.Settings.ClassSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class ClassModbus {

    private eMode Mode;
    private SerialParameters portParametres;
    private ModbusMaster RTUMaster;
    //Массив для считываемых из регистров данных.
    private int[] data;
    private boolean[] dataBool;
    private ClassDevice CustomDevice;
    private int[] CustomMessage;
    private long LastTickPoll;
    private Timer TimerSec;
    private int DelaySec;
    private boolean WaitAnswer; //only COM
    private final int numOfRegMax=125;
    private final int numOfRegMin=1;
    private int timeOut=1000;
    public static Object locker = new Object();

    //<editor-fold desc="Setters/Getters">
    public void setMode(eMode mode) {
        this.Mode = mode;
    }

    public eMode getMode() {
        return Mode;
    }

    public void setPortParametres(SerialParameters portParametres) {
        this.portParametres = portParametres;
        portClose();
    }

    public SerialParameters setParametres(ClassSettings settings) {
        String portName= SerialPortList.getPortNames()[settings.getPortModbus()];
        SerialParameters portParametres =new SerialParameters();
        portParametres.setDevice(portName);
        portParametres.setBaudRate(SerialPort.BaudRate.getBaudRate(settings.getBaudRate()));
        portParametres.setDataBits(settings.getDataBits());
        portParametres.setParity(SerialPort.Parity.getParity(settings.getParity()));
        portParametres.setStopBits(settings.getStopBits());
        timeOut=settings.getTimeout();
        return portParametres;
    }



    //</editor-fold>

    public ClassModbus() {
        portParametres = setParametres(MainWindow.settings);
        //setPortParametres(MainWindow.settings);
        RTUMaster=null;
        Mode=eMode.None;
        //TimerSec=new Timer(true);
    }

    private void TimerSec_Elapsed(){

    }

    /**
     * Открыть порт.
     * @return
     */
    public boolean portOpen(){
        try {

        if(RTUMaster!=null) {
            RTUMaster.disconnect();
            RTUMaster=null;
        }

            RTUMaster = ModbusMasterFactory.createModbusMasterRTU(portParametres);
            RTUMaster.setResponseTimeout(timeOut);
            RTUMaster.connect();
        }
        catch (Exception ex){
            return false;
        }
        Mode=eMode.PortOpen;
        return true;
    }

    /**
     * Закрыть порт.
     */
    public void portClose()
    {
        if (RTUMaster != null && RTUMaster.isConnected()) {
            try {
                RTUMaster.disconnect();
                System.out.println("Связь с RTUMaster: "+ RTUMaster.isConnected());
                RTUMaster=null;
                Mode=eMode.PortClosed;
            } catch (ModbusIOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void Poll(){
        for(ClassDevice device : MainWindow.Devices){

            if(device.get_Protocol()==ClassDevice.EnumProtocol.SMS) continue;
            if(device.getChannels().isEmpty()) continue;
            if(RTUMaster==null) continue;
            if(device.get_Period()==0) continue;
            //if(device.getCounGroup()>0) continue;
            //if (someDeviceInTheProcess(MainWindow.Devices, device)) continue;
            //if(device.getInProcess()==true) continue;
            //List<ClassGroupRequest> groups=device.getGroups();

            if(RTUMaster.isConnected()){
                ReadGroupRegistry(device,RTUMaster);
            }
        }
    }

    /**
     * Чтение регистров по группам и в пакетах по 100.
     * @param device текущее устройство
     * @param master текущий мастер(RTU,TCP,ASCII)
     */
    private void ReadGroupRegistry(ClassDevice device, ModbusMaster master){

        device.setInProcess(true);
        //int countGroup= device.getCounGroup();
        for(ClassGroupRequest group : device.getGroup()){

            //device.setCounGroup(--countGroup);
//            if(device.getCounGroup()<=0){
//               device.setInProcess(false);
//            }
            device.packetSended();
            int numOfPoint = group.GetSize();
            try{
                    if (group.get_TypeRegistry() == ClassChannel.EnumTypeRegistry.InputRegistry && numOfPoint < numOfRegMax)
                    {
                        data = master.readInputRegisters(device.get_Address(), group.getStartAddress(),numOfPoint);

                        System.out.println(device +" " +group.get_TypeRegistry() + " : " + Arrays.toString(data));
                    }
                    if(group.get_TypeRegistry()== ClassChannel.EnumTypeRegistry.HoldingRegistry && numOfPoint < numOfRegMax){
                        data = master.readHoldingRegisters(device.get_Address(), group.getStartAddress(),numOfPoint);

                        System.out.println(device +" " +group.get_TypeRegistry() + " "+ group.GetSize() +" : " + Arrays.toString(data));
                    }
                    if(group.get_TypeRegistry()== ClassChannel.EnumTypeRegistry.CoilOutput && numOfPoint < numOfRegMax){
                        dataBool=master.readCoils(device.get_Address(), group.getStartAddress(), numOfPoint);
                        System.out.println(device +" " +group.get_TypeRegistry() + " "+ group.GetSize() +" : " + Arrays.toString(dataBool));
                    }
                    if(group.get_TypeRegistry()== ClassChannel.EnumTypeRegistry.DiscreteInput && numOfPoint < numOfRegMax){
                        dataBool=master.readDiscreteInputs(device.get_Address(), group.getStartAddress(), numOfPoint);
                        System.out.println(device +" " +group.get_TypeRegistry() + " "+ group.GetSize() +" : " + Arrays.toString(dataBool));
                    }

            } catch (Exception e) {
                System.out.println("ReadGroupRegistry: "+ e.getMessage());
                Mode=eMode.None;
                device.PacketNotReceived();
                if(RTUMaster==null || !RTUMaster.isConnected()){
                    try {
                        portClose();
                        Thread.sleep(timeOut);
                        portOpen();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                continue;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            device.PacketReceived();
            switch (group.get_TypeRegistry()){
                case InputRegistry,HoldingRegistry ->{
                    for (int j = 0; j < group.get_Channels().size(); j++)
                    {
                        ClassChannel channel = group.get_Channels().get(j);
                        int Offset = group.getOffset(j);
                        channel.set_BaseValue(GetDataFromBuffer(Offset, channel.get_Format()));
                        channel.set_Value(GetDecimalFromBuffer(Offset, channel.get_Format()));
                    }
                    data=new int[]{};
                }
                case CoilOutput,DiscreteInput ->{
                    for (int j = 0; j < group.get_Channels().size(); j++){
                        ClassChannel channel=group.get_Channels().get(j);
                        int Offset= group.getOffset(j);
                        channel.set_Value(dataBool[Offset]? 1:0);
                    }
                    dataBool=new boolean[] {};
                }
            }
        }
    }

    private double GetDecimalFromBuffer(int offset, ClassChannel.EnumFormat format) {
        double V=0;
       try{

           V= switch (format){
               case UINT -> data[offset];
               case SINT -> data[offset];
               case Float, swFloat,UInt32 -> data[offset];
           };
       }
       catch(Exception ex){
           var tt=ClassGroupRequest.CGR;
           System.out.println(ex.getMessage());
       }
        return V;
    }

    private int[] GetDataFromBuffer(int offset, ClassChannel.EnumFormat format) {

        int[] d=new int[]{0};
        if(data==null) return d;
        switch (format){
            case UINT, SINT ->{
                d=new int[1];
                d[0]=data[offset];
            }
            case UInt32,Float ->{
                d = new int[2];
                d[0] = data[offset];
                d[1] = data[offset + 1];
            }
            case swFloat -> {
                d = new int[2];
                d[0] = data[offset+1];
                d[1] = data[offset];
            }
        }
        return d;
    }

    /**
     * Проверка, ведет ли какое нибудь устройство опрос в текущий момент.
     * @param devices
     * @param currDevice
     * @return
     */
    private boolean someDeviceInTheProcess(ObservableList<ClassDevice> devices, ClassDevice currDevice)
    {
        for(var dev : devices)
        {
            if (dev.getInProcess() == true && currDevice.getId() != dev.getId())
                return true;
        }
        return false;
    }

    public List<ClassGroupRequest> GetGroups(){

        return null;
    }



    public enum eMode{
        None,
        PortOpen,
        RequestServerID,
        MasterInit,
        PortClosed
    }

    public enum eFunction
    {
        None,
        ReadCoils,
        ReadDiscreteInputs,
        ReadHoldingRegisters,
        ReadInputRegisters,
        WriteSingleCoil,
        WriteSingleRegister,
        WriteMultipleCoils,
        WriteMultipleRegisters
    }

}
