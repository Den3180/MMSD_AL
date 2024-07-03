package org.example.mmsd_al.Archive;

import jssc.*;
import org.example.mmsd_al.MainWindow;

import java.util.ArrayList;
import java.util.Arrays;

public class SerialPortReader implements SerialPortEventListener {
    private SerialPort serialPort;
    private ArrayList<int []> note;
    private int typeCommand;

    public SerialPortReader(SerialPort serialPort, ArrayList<int []> note,int typeCommand) {
        this.serialPort = serialPort;
        this.note=note;
        this.typeCommand=typeCommand;

    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        //TODO Проверка пакетов на кол-во байт и на КС. Если проверка не пройдена - запрос заново
        // либо всей группы пакетов, либо только поврежденного. Продумать алгоритм.
        if (serialPortEvent.isRXCHAR()) {
            if (serialPortEvent.getEventValue() > 0) {
                try {
                    if(typeCommand==30) {
                       int [] buffer = serialPort.readIntArray();
                       synchronized (ClassDeviceArchive.locker){
                           note.add(buffer);
                           System.out.println(Arrays.toString(buffer));
                       }
                    }
                } catch (SerialPortException ex) {
                    ex.printStackTrace();
                }
            }
        }
        try {
            serialPort.removeEventListener();
            serialPort.closePort();
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }
}
