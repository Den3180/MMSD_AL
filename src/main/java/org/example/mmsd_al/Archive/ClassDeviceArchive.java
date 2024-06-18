package org.example.mmsd_al.Archive;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterRTU;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterSerial;
import com.intelligt.modbus.jlibmodbus.net.ModbusConnection;
import com.intelligt.modbus.jlibmodbus.net.stream.base.LoggingInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.LoggingOutputStream;
import com.intelligt.modbus.jlibmodbus.net.transport.ModbusTransport;
import com.intelligt.modbus.jlibmodbus.utils.CRC16;
import com.intelligt.modbus.jlibmodbus.utils.SerialPortInfo;
import org.example.mmsd_al.MainWindow;

public class ClassDeviceArchive {
    private int [] dataArr;
    private ModbusMaster master;

    public ClassDeviceArchive(ModbusMaster master){
        this.master=master;
    }

    /**
     * Получить сведения о количестве записей в БКМ.
     * @param devAddress
     * @param startReg
     * @return
     */
    public int[] GetCountNoteArchive(int devAddress, int startReg){
        try {
                return master.readHoldingRegisters(devAddress,startReg,1);
        } catch (ModbusProtocolException e) {
            throw new RuntimeException(e);
        } catch (ModbusNumberException e) {
            throw new RuntimeException(e);
        } catch (ModbusIOException e) {
            throw new RuntimeException(e);
        }
    }

    public void ReadArchive_30(int addressDev, int startPos){

    }
}
