package org.example.mmsd_al.Archive;

import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.utils.CRC16;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import jssc.*;
import org.example.mmsd_al.Classes.ClassModbus;
import org.example.mmsd_al.ServiceClasses.ClassDelay;
import org.example.mmsd_al.ServiceClasses.ClassMessage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.StreamSupport.stream;

public class ClassDeviceArchive {
    private int [] dataArr;
    private ModbusMaster master;
    private ClassModbus modbus;
    private String portName;
    private static ArrayList<int []> note_30;
    private static ArrayList<int []> note_31;
    private static ArrayList<Integer []> resTotal;
    private SerialPort serialPort;
    public final static Object locker=new Object();



    public ClassDeviceArchive(ClassModbus modbus){
        this.master= modbus.getModbusMaster();
        this.modbus=modbus;
        portName=modbus.getPortParametres().getDevice();
        note_30=new ArrayList<>();
        note_31=new ArrayList<>();
        resTotal=new ArrayList<>();

    }
    public ArrayList<int []> getNote_30(){
        return  note_30;
    }

    public ArrayList<int []> getNote_31(){
        return  note_31;
    }

    public ArrayList<Integer []> getResTotal(){
        return  resTotal;
    }

    /**
     * Получить сведения о количестве записей в БКМ.
     * @param devAddress адрес устройства.
     * @param startReg стартовый регистр.
     * @return int[]
     */
    public int[] GetCountNoteArchive(int devAddress, int startReg){
        try {
                dataArr=master.readHoldingRegisters(devAddress,startReg,1);
                modbus.portClose();
                modbus.getPortParametres().setDevice("");
                serialPort=new SerialPort(portName);
                return dataArr;
        } catch (Exception e) {
            return new int[]{0};
        }
    }

    /**
     * Закрыть порт передачи архива.
     */
    public void closeSerialPort(){
        try {
            if(serialPort.isOpened()) serialPort.closePort();
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Чтение сведений об архиве.
     * @param addressDev адресс устройства
     * @param startPos начальная позиция чтения
     */
    public void readArchive_30(int addressDev, int startPos){

        //Открытие и настройка порта.
        try {
            if(!serialPort.isOpened()) {
                //serialPort=new SerialPort(portName);
                serialPort.openPort();
            }
            serialPort.setParams(SerialPort.BAUDRATE_9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            if(!serialPort.isOpened()) return;
            int mask = SerialPort.MASK_RXCHAR;
            serialPort.setEventsMask(mask);
            //Добавить слушателя в отдельном потоке.
            //serialPort.addEventListener(new SerialPortReader(serialPort, note_30,30));
            //Сборка пакета команды.
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
                //Запись в порт.
                serialPort.writeBytes(mess);

            ClassDelay.delay(500);
            //Чтение данных.
            var dataBlock = serialPort.readIntArray();
            System.out.println(Arrays.toString(dataBlock));
            //Сохранение в необработанном виде.
            note_30.add(dataBlock);

//            if(serialPort.isOpened())
//                serialPort.closePort();

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

    /**
     * Чтение архива устройства.
     * @param address адрес устройства
     * @param numRecords номер записи
     * @param numBlock номер блока.
     */
    public void readArchive_31(int address, int numRecords, int numBlock){

        //SerialPort serialPort=new SerialPort(portName);
        try {
            //Открытие и настройка порта.
            if(!serialPort.isOpened()) {
                serialPort.openPort();
            }
            serialPort.setParams(SerialPort.BAUDRATE_9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            if(!serialPort.isOpened()) return;
            int mask = SerialPort.MASK_RXCHAR;
            serialPort.setEventsMask(mask);
            //Сборка пакета команды.
            byte [] record=DataUtils.toByteArray((short)numRecords);
            byte [] blocks=DataUtils.toByteArray((short)numBlock);
            byte [] b=new byte[6];
            b[0]=(byte)address;
            b[1]=0x1F;
            b[2]=record[0];
            b[3]=record[1];
            b[4] = blocks[0];
            b[5] = blocks[1];
            int crc=CRC16.calc(b);
            byte [] crcArr=DataUtils.toByteArray((short)crc);
            byte [] mess =Arrays.copyOf(b,b.length+2);
            mess[6]=crcArr[1];
            mess[7]=crcArr[0];
            //Запись в порт.
            serialPort.writeBytes(mess);
            //Задержка, что порт успел обработать команду и прислать данные.
            ClassDelay.delay(500);
            //Чтение данных.
            var dataBlock = serialPort.readIntArray();
            System.out.println(Arrays.toString(dataBlock));
            //Сохранение в необработанном виде.
            note_31.add(dataBlock);

//            if(serialPort.isOpened())
//                serialPort.closePort();
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

    /**
     * Обработка полученных блоков архива.
     */
    public void processArchive(){

        List<Integer> res=new ArrayList<Integer>();
        int countNote_31 = 0;
        for(int [] note:note_31){
            int i=0;
            int h=0;
            countNote_31++;
            if(note[7]==(int)'P'){
                i=11;
                if(!res.isEmpty()){
                    resTotal.add(res.toArray(new Integer[0]));
                    res.clear();
                }
                while(h<i){
                    res.add(note[h]);
                    h++;
                }
            }
            else{
                i=7;
            }
            try{
                while(i<note.length){
                    if ((i + 2) <= note.length){
                        var val=note[i];
                        note[i]=note[i+1];
                        note[i+1]=val;
                    }
                    if(i<note.length-2){
                        var val1=DataUtils.toHexString((byte) note[i]);
                        var val2=DataUtils.toHexString((byte) note[i+1]);
                        if(val2.startsWith("0")){
                            String temp= val2.substring(1);
                            val2=new String(temp);
                        }
                        String unionVal=new String(val1+val2);
                        Integer valArchive = Integer.valueOf(unionVal, 16);
                        valArchive = valArchive > 32767 ? valArchive - 65535 - 1 : valArchive;
                        res.add(valArchive);
                    }
                    i+=2;
                }
                if (!res.isEmpty() && countNote_31==note_31.size())
                {
                    resTotal.add(res.toArray(new Integer[0]));
                    res.clear();
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        if(saveArchive(resTotal)){
            Platform.runLater(()->
            ClassMessage.showMessage(
                    "Архив",
                    "Сохранение архива",
                    "Архив сохранен",
                    Alert.AlertType.INFORMATION));
        }
    }

    /**
     * Сохранить архив в файл.
     * @param resTotal буфер хранения архива.
     * @return boolean
     */
    private boolean saveArchive(ArrayList<Integer[]> resTotal){
        File dirArch=new File("Archive");
        if(!dirArch.exists()) dirArch.mkdir();
        String pathFile= dirArch.getPath()+File.separator+"arch_dev.ach";
        try(FileOutputStream fout=new FileOutputStream(pathFile)) {
            try(ObjectOutputStream out=new ObjectOutputStream(fout)){
                out.writeObject(resTotal);
                return true;
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Загрузить архив из файла.
     * @return массив данных.
     */
    public static ArrayList<Integer[]> loadArchive(){
        ArrayList<Integer[]> archive;
        File dirArch=new File("Archive");
        //Если каталог не создан и нет архивов.
        if(!dirArch.exists()) {
            return  null;
        }
        String pathFile= dirArch.getPath()+File.separator+"arch_dev.ach";
        try(FileInputStream fin=new FileInputStream(pathFile)){
            try (ObjectInputStream oin = new ObjectInputStream(fin)){
                archive=(ArrayList<Integer[]>)oin.readObject();
                return archive;
            } catch (ClassNotFoundException e) {
                return null;
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * Отправка архива на СМСД.
     * @param resTotal массив данных
     * @param ip адрес СМСД
     * @param port порт СМСД
     * @return
     */
    public static boolean sendArchiveDevice(ArrayList<Integer[]> resTotal, String ip, int port){

        try {
            Socket client = new Socket(ip, port);
            DataOutputStream outwriter=new DataOutputStream(client.getOutputStream());
            for (var el : resTotal){
                for (int i = 0; i < el.length; i++) {
                    outwriter.writeInt(el[i]);
                }
                outwriter.flush();
            }
            client.close();

        } catch (Exception e) {
            System.out.println(e.getMessage() + " "+ "Method: sendArchiveDevice");
        }
        return true;
    }
}
