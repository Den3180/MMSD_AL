package org.example.mmsd_al.Archive;

import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.utils.CRC16;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import jssc.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.Classes.ClassModbus;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.ServiceClasses.ClassDelay;
import org.example.mmsd_al.ServiceClasses.ClassMessage;
import org.example.mmsd_al.StartApplication;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                //if(!modbus.getModbusMaster().isConnected()) modbus.portOpen();
                dataArr=master.readHoldingRegisters(devAddress,startReg,1);
                modbus.portClose();
                modbus.getPortParametres().setDevice("");
                serialPort=new SerialPort(portName);
                return dataArr;
        } catch (Exception e) {
            modbus.portClose();
            modbus.getPortParametres().setDevice("");
            serialPort=new SerialPort(portName);
            modbus.portOpen();
            master=modbus.getModbusMaster();
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
    public void processArchive(ClassDevice device){
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
        if(saveArchive(resTotal, device)){
            Platform.runLater(()->ClassMessage.showMessage("Архив","Сохранение архива","Архив сохранен",
                                                            Alert.AlertType.INFORMATION));
            SaveToExcel(resTotal,device);
        }
    }

    /**
     * Сохранить архив в файл.
     *
     * @param resTotal буфер хранения архива.
     * @param device
     * @return boolean
     */
    private boolean saveArchive(ArrayList<Integer[]> resTotal, ClassDevice device){
        File dirArch=new File("Archive");
        if(!dirArch.exists()) dirArch.mkdir();
        String pathFile= device+"_"+device.get_Address()+".ach";

        File fileArch=new File(dirArch.getPath()+File.separator+pathFile);
        int i=0;
        //Проверка на совпадение имени файла и генерации уникального, если есть совпадение.
        while (fileArch.exists()){
            pathFile=device+"_"+device.get_Address()+"_"+"("+i+")"+".xlsx";
            fileArch=new File(dirArch.getPath()+File.separator+pathFile);
            i++;
        }
        //Откурытие потока и сохранение в файл.
        try(FileOutputStream fout=new FileOutputStream(fileArch.getPath())) {
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
            dirArch.mkdir();
        }        
        FileChooser fileArch=new FileChooser();
        fileArch.setTitle("Архив");
        fileArch.getExtensionFilters().addAll
                (new FileChooser.ExtensionFilter("Архив","*.ach"));
        File archFile= fileArch.showOpenDialog(StartApplication.stage);
        if(archFile==null) return null;

//        String pathFile= dirArch.getPath()+File.separator+"arch_dev.ach";
        String pathFile= archFile.getAbsolutePath();
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


    /**
     * Сохранить архив в Excel.
     *
     * @param arch
     * @param device
     */
    public static void SaveToExcel(ArrayList<Integer[]> arch, ClassDevice device) {

        //Выбрать тип каналов устройства InputRegistry.
        var channels=device.getChannels().stream().filter(ch->ch.get_TypeRegistry()== ClassChannel.EnumTypeRegistry.InputRegistry).toArray();
        try (var workbook = new XSSFWorkbook()) {
            // Создаем бланк Excel листа
            XSSFSheet sheet = workbook.createSheet(device.toString());
            // Создаем пустую карту с теми типами данных что там будут.
            Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
            // Делаем заголовочную строку.
            int countLine=2;
            data.put(1, new Object[]{"Параметр", "Устройство", "Значение", "Дата"});
            //Заполняем карту данных.
            for (var note : arch){
                //Парсим дату из массива данных архива.
                var centuryNow = (LocalDate.now().getYear()/ 100) * 100;
                var fullYear=centuryNow+note[58];
                var month=note[59];
                var day=note[60];
                var hour=note[61];
                var minute=note[62];
                var second=note[63];
                LocalDateTime date=LocalDateTime.of(fullYear,month,day,hour,minute,second);
                String dateStr=date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
                int countParam=0;
                //Заполняем карту параметрами из архива для текущей записи.
                for(int i=39;i<note.length;i++){
                    data.put(countLine++,
                            new Object[]{
                                    ((ClassChannel)channels[countParam++]).get_Name(), device.toString(), note[i],dateStr});
                    if(countParam>=channels.length){
                        break;
                    }
                }
            }
            // Создание списка ключей.
            Set<Integer> keyset = data.keySet();
            //Настройка стилей для первой строки.
            XSSFCellStyle style=workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            Font fnt= workbook.createFont();
            fnt.setFontName("Courier New");
            fnt.setBold(true);
            fnt.setFontHeight((short)300);
            fnt.setColor(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex());
            style.setFont(fnt);

            //Создание строк.
            int rownum = 0;
            for (Integer key : keyset) {
                // Создание новой строки.
                Row row = sheet.createRow(rownum);
                Object[] objArr = data.get(key);
                int cellnum = 0;
                //Создание ячеек в каждой строке.
                for (Object obj : objArr) {
                    Cell cell = row.createCell(cellnum++);
                    if(rownum==0) {
                         cell.setCellStyle(style);
                         }

                    if (obj instanceof String){
                        cell.setCellValue((String) obj);
                    }
                    else if (obj instanceof Integer){
                        var st=workbook.createCellStyle();
                        st.setAlignment(HorizontalAlignment.CENTER);
                        cell.setCellStyle(st);
                        cell.setCellValue((Integer) obj);
                    }

                    //Индикация цветом начала каждой записи.
                    String cellValue = row.getCell(0).getStringCellValue();
                    if(cellValue.equals("Инв. № объекта")){
                        var headerStyle = workbook.createCellStyle();
                            headerStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
                            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        if(cell.getColumnIndex()==2){
                                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                            }
                            cell.setCellStyle(headerStyle);
                    }
                }
                //Настройка ширины столбцов.
                if(rownum==1) {
                    sheet.setColumnWidth(0, 20000);
                    sheet.setColumnWidth(1, 7000);
                    sheet.setColumnWidth(2, 7000);
                    sheet.setColumnWidth(3, 7000);
                }
                rownum++;
            }

            // Try block to check for exceptions
            try {
                File dirArch=new File("Архивы");
                if(!dirArch.exists()) dirArch.mkdir();
                //Полуить заводской номер устройства.
                var devPlantNumber=data.get(5)[2];
                //Путь к файлу архива.
                String path=device+"_"+device.get_Address()+"_"+devPlantNumber+".xlsx";
                //Объект информации о файле.
                File fileArch=new File(dirArch.getPath()+File.separator+path);
                int i=0;
                //Проверка на совпадение имени файла и генерации уникального, если есть совпадение.
                while (fileArch.exists()){
                    path=device+"_"+device.get_Address()+"_"+devPlantNumber+"("+i+")"+".xlsx";
                    fileArch=new File(dirArch.getPath()+File.separator+path);
                    i++;
                }
                //Создание потока и сохранение файла.
                FileOutputStream out = new FileOutputStream(
                        new File(dirArch.getPath()+File.separator+path));
                workbook.write(out);
                // Закрытие потока.
                out.close();
                Platform.runLater(()->
                        ClassMessage.showMessage(
                                "Архив",
                                "Импорт в Excel",
                                "Импорт архива завершен!",
                                Alert.AlertType.INFORMATION));
            }
            catch (Exception e) {
                e.printStackTrace();
                ClassMessage.showMessage("Архив","","Ошибка импорта архива!", Alert.AlertType.ERROR);
            }
        }
        catch (Exception exception){
            ClassMessage.showMessage("Архив","","Ошибка импорта архива!  "+ exception.getMessage(), Alert.AlertType.ERROR);
        }
    }
}

