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
import java.util.Arrays;

import static java.util.stream.StreamSupport.stream;

public class ClassDeviceArchive {
    private int [] dataArr;
    private ModbusMaster master;
    private ClassModbus modbus;

    public ClassDeviceArchive(ClassModbus modbus){
        this.master= modbus.getModbusMaster();
        this.modbus=modbus;
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

        SerialPort serialPort=new SerialPort("COM7");
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            if(!serialPort.isOpened()) return;

            int mask = SerialPort.MASK_RXCHAR;
            serialPort.setEventsMask(mask);

            serialPort.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    if(serialPortEvent.getEventValue()>0){
                        try {
                            int size=serialPortEvent.getEventValue();
                            byte [] mess=new byte[size];
                            mess=serialPortEvent.getPort().readBytes();
                        }
                        catch (Exception ex){
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            });



            byte [] arrayByte= DataUtils.toByteArray((short) startPos);
            int [] record=new int[arrayByte.length];
            for (int i = 0; i < arrayByte.length; i++) {
                if(arrayByte[i]>=0){
                    record[arrayByte.length - i-1]=arrayByte[i];
                }
                else{
                    record[arrayByte.length - i-1]=arrayByte[i] & 0xFF;
                }
            }
            byte [] b=new byte[4];
            b[0]=(byte) addressDev;
            b[1]=0x1E;
            b[2]=arrayByte[1];
            b[3]=arrayByte[0];
//            b[2]=record[0];
//            b[3]=record[1];
//            int crc=CRC16.calc(DataUtils.toByteArray(b));
            int crc=CRC16.calc(b);
            byte [] crcbyte= DataUtils.toByteArray((short)crc);
            byte [] mes=new byte[b.length+crcbyte.length];

            for (int i = 0; i < mes.length; i++) {
                if(i< b.length){
                    mes[i]=b[i];
                }
                else{
                    mes[i]=crcbyte[0];
                    mes[i+1]=crcbyte[1];
                    break;
                }
            }
            serialPort.writeBytes(mes);

        } catch (SerialPortException e) {
            try {
                serialPort.closePort();
            } catch (SerialPortException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

    }
}
