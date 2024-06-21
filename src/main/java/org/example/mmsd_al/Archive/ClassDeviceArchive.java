package org.example.mmsd_al.Archive;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterRTU;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterSerial;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.net.ModbusConnection;
import com.intelligt.modbus.jlibmodbus.net.stream.base.LoggingInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.LoggingOutputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.net.transport.ModbusTransport;
import com.intelligt.modbus.jlibmodbus.serial.SerialUtils;
import com.intelligt.modbus.jlibmodbus.utils.CRC16;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;
import com.intelligt.modbus.jlibmodbus.utils.SerialPortInfo;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.example.mmsd_al.Classes.ClassModbus;
import org.example.mmsd_al.MainWindow;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import static java.util.stream.StreamSupport.stream;

public class ClassDeviceArchive {
    private int [] dataArr;
    private ModbusMaster master;
    private ClassModbus modbus;
    private String portName;
    private static ArrayList<int []> note_30;


    public ClassDeviceArchive(ClassModbus modbus){
        this.master= modbus.getModbusMaster();
        this.modbus=modbus;
        portName=modbus.getPortParametres().getDevice();
        note_30=new ArrayList<>();
    }

    public ArrayList<int []> getNote_30(){
        return  note_30;
    }

    /**
     * Получить сведения о количестве записей в БКМ.
     * @param devAddress
     * @param startReg
     * @return
     */
    public int[] GetCountNoteArchive(int devAddress, int startReg){
        try {
                dataArr=master.readHoldingRegisters(devAddress,startReg,1);
                modbus.portClose();
                modbus.getPortParametres().setDevice("");
                return dataArr;
        } catch (ModbusProtocolException e) {
            throw new RuntimeException(e);
        } catch (ModbusNumberException e) {
            throw new RuntimeException(e);
        } catch (ModbusIOException e) {
            throw new RuntimeException(e);
        }
    }

    public void ReadArchive_30(int addressDev, int startPos){

        SerialPort serialPort=new SerialPort(portName);
        try {
            if(!serialPort.isOpened())
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            if(!serialPort.isOpened()) return;
            int mask = SerialPort.MASK_RXCHAR;
            serialPort.setEventsMask(mask);
            serialPort.addEventListener(new SerialPortReader(serialPort, note_30));
                byte [] record=DataUtils.toByteArray((short)startPos);
                byte [] b=new byte[4];
                b[0]=(byte)addressDev;
                b[1]=0x1E;
                b[2]=record[0];
                b[3]=record[1];
                int crc=CRC16.calc(b);
                byte [] crcArr=DataUtils.toByteArray((short)crc);
                byte [] mess =Arrays.copyOf(b,b.length+2);
                mess[4]=crcArr[1];
                mess[5]=crcArr[0];
                serialPort.writeBytes(mess);
        } catch (SerialPortException e) {
            try {
                if(serialPort.isOpened()){
                    serialPort.closePort();
                }
            } catch (SerialPortException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
