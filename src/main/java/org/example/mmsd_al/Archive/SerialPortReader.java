package org.example.mmsd_al.Archive;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.example.mmsd_al.MainWindow;

import java.util.ArrayList;
import java.util.Arrays;

public class SerialPortReader implements SerialPortEventListener {
    private SerialPort serialPort;
    private ArrayList<int []> note_30;

    public SerialPortReader(SerialPort serialPort, ArrayList<int []> note_30) {
        this.serialPort = serialPort;
        this.note_30=note_30;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.isRXCHAR()) {
            if (serialPortEvent.getEventValue() > 0) {
                try {
                    int[] buffer = serialPort.readIntArray();
                    note_30.add(buffer);
                    System.out.println(Arrays.toString(buffer));
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
