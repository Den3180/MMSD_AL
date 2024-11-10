package org.example.mmsd_al;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.SwipeEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.mmsd_al.Archive.ClassDeviceArchive;
import org.example.mmsd_al.ServiceClasses.Comparators.ChannelCompareAddress;
import org.example.mmsd_al.ServiceClasses.Comparators.ChannelCompareTypeReg;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.Classes.ClassModbus;
import org.example.mmsd_al.DBClasses.ClassDB;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.ServiceClasses.ClassMessage;
import org.example.mmsd_al.Settings.ClassSettings;
import org.example.mmsd_al.UserControlsClasses.TreeViewFactory;
import org.example.mmsd_al.UserControlsClasses.UserControlsFactory;
import org.example.mmsd_al.Windows.WindoWConfig;
import org.example.mmsd_al.Windows.WindowAbout;
import org.example.mmsd_al.Windows.WindowExportArchive;
import org.example.mmsd_al.Windows.WindowImportArchive;
import org.jetbrains.annotations.NotNull;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class MainWindow {

    public  static MainWindow mainWindow;

    public static ClassSettings settings;
    public static ClassDB DB;
    public static ObservableList<ClassDevice> Devices;
    public static ObservableList<ClassChannel> Channels;
    public  static ClassModbus modbus;
    private Stage stage=StartApplication.stage;
    private File dbFile;
    private Timer timerSec;
    private Timer timerMainTime;
    public static Object locker = new Object();
    public static int count=0;

    @FXML
    public Label deviceName;
    @FXML
    public Label lbTime;
    @FXML
    public MenuItem loadArchivBtnMenu;
    @FXML
    private TreeView<Pair<Integer,String>> treeView;
    @FXML
    private MenuBar mainMenu;
    @FXML
    private SplitPane sPane;
    private TableView userControlDevices;
    private TableView userControlChannels;

    public void initialize(){
        mainWindow=this;
        settings=ClassSettings.load();
        DB=new ClassDB();
        dbFile=new File(settings.getdB());
        if(!dbFile.exists()){
           ButtonType buttonType= ClassMessage.showMessage("База данных","СУБД","Файл БД не доступен!\nСоздать БД?",
                                      Alert.AlertType.CONFIRMATION);
           if(buttonType.getButtonData()== ButtonBar.ButtonData.YES){
               ClassDB.create(null);
           } else if (buttonType.getButtonData() == ButtonBar.ButtonData.NO) {
               FileChooser fileChooser=new FileChooser();
               fileChooser.setTitle("База данных");
               fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("База данных","*.db"));
               File dbFile= fileChooser.showOpenDialog(stage.getOwner());
               if(dbFile!=null){
                   settings.setdB(dbFile.getAbsolutePath());
                   settings.save();
               }
           }
        }
        if(!DB.open(settings)){
            ClassMessage.showMessage("База данных","СУБД","БД не доступна! Программа будет закрыта." +
                            "\nПроверьте конфигурацию.",
                    Alert.AlertType.ERROR);
            exitApp();
        }
        //Получить список устройств из базы данных.
        Devices= (FXCollections.observableArrayList(DB.devicesLoad()));
        //Установка порта в таблице устройств.
        Devices.forEach(el->el.set_ComPort(String.valueOf(settings.getPortModbus())));
        //Все каналы устройств в БД.
        Channels=FXCollections.observableArrayList(DB.registriesLoad(0));
        //Разбивка каналов по устройствам.
        for(ClassDevice dev : Devices){
            dev.setChannels( Channels.stream().filter(el->el.get_Device().getId()==dev.getId())
                    .sorted(new ChannelCompareTypeReg().thenComparing(new ChannelCompareAddress())).toList());
            dev.setGroups(dev.getGroups());
        }

        //Построение дерева устройств.
        treeView.setRoot(TreeViewFactory.createRootTree(Devices,new Pair<Integer,String>(0,"Устройство"){
            @Override
            public String toString(){
            return getValue();
            }
        }));

        //Выбор стартовой таблицы.
        switch (settings.getStartWindow()){
            case 0:
                userControlDevices = UserControlsFactory.createTable(Devices, UserControlsFactory.HEADERS_DEVICE,
                        UserControlsFactory.VARIABLES_DEVICE, new ClassDevice());
                sPane.getItems().set(1,userControlDevices);
                deviceName.setText("Не выбрано");
                userControlDevices.setOnMouseClicked(this::tableDevice_MouseClicked);
                break;
        }
        modbus=new ClassModbus();
        mainTimereApp();
        startTimerPoll();
        if(modbus.getMode()== ClassModbus.eMode.NoPortInSystem){
            loadArchivBtnMenu.setDisable(true);
        }
    }

    /**
     * Таймер основного времени.
     */
    private void mainTimereApp(){
        timerMainTime=new Timer("MainTime",true);
        timerMainTime.schedule(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
                Platform.runLater(()-> lbTime.setText(formater.format(new Date())));
            }
        },0,1000);
    }

    /**
     * Запуск таймера основног опроса.
     */
    private void startTimerPoll(){
        timerSec=new Timer("Poll",true);
        timerSec.schedule(new TimerTask() {
            @Override
            public void run() {
                timerSec_Tick();
            }
        },500,5000);
    }

    /**
     * Обработчик кнопок главного меню.
     * @param actionEvent
     */
    @FXML
    public void menuItemClick(@NotNull ActionEvent actionEvent) {
        MenuItem menuItem= (MenuItem)actionEvent.getSource();
        if(menuItem==null) return;
        String nameMenu=menuItem.getText();
        switch (nameMenu){
            case "Выход...":
                exitApp();
                break;
            case "Устройства...":
                sPane.getItems().set(1,userControlDevices);
                break;
            case "Каналы данных...":
                userControlChannels=UserControlsFactory.createTable(Channels,UserControlsFactory.HEADES_CHANNEL,
                        UserControlsFactory.VARIABLES_CHANNEL, new ClassChannel());
                //userControlChannels.setOnMouseClicked(this::channels_MouseClicked);
                sPane.getItems().set(1,userControlChannels);
                break;
            case "База данных...":
                FileChooser fileChooser=new FileChooser();
                fileChooser.setTitle("База данных");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("База данных","*.db"));
                File dbFile= fileChooser.showOpenDialog(stage.getOwner());
                if(dbFile!=null){
                    settings.setdB(dbFile.getAbsolutePath());
                    settings.save();
                    ClassMessage.showMessage("База данных","",
                            "Подключение к БД будет выполнено после перезапуска программы.", Alert.AlertType.INFORMATION);
                    exitApp();
                }
                break;
            case "Параметры...":
                WindoWConfig wConfig=new WindoWConfig();
                if(wConfig.showWindow()){
                    modbus.setPortParametres(modbus.setParametres(settings));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                Devices.forEach(el->el.set_ComPort(String.valueOf(settings.getPortModbus())));
                break;
            case "Создать БД...":
               DirectoryChooser directoryChooser=new DirectoryChooser();
               directoryChooser.setTitle("Создать БД");
               File dirPath=directoryChooser.showDialog(stage);
               if(dirPath==null) break;
               File pathDefDB=new File(dirPath.getAbsolutePath()+"/pkm.db");
               if(pathDefDB.exists()){
                   int i=1;
                   while (true){
                       var pathname=pathDefDB.getParent()+"/pkm"+i+".db";
                       pathDefDB=new File(pathname);
                       i++;
                       if(!pathDefDB.exists()) break;
                   }
               }
               if(ClassDB.create(pathDefDB.getAbsolutePath())){
                   ClassMessage.showMessage("База данных","","База данных создана.", Alert.AlertType.INFORMATION);
                   settings.setdB(pathDefDB.getAbsolutePath());
                   settings.save();
               }
               break;
            case "Отправить архив...":
                ArrayList<Integer[]> archive= ClassDeviceArchive.loadArchive();
                if(archive==null || archive.isEmpty()){
                    ClassMessage.showMessage("Архив","","Нет доступного архива", Alert.AlertType.ERROR);
                    break;
                }
                WindowExportArchive.showWindow(archive);
                break;
            case "Загрузить архив...":
                timerSec.cancel();
                WindowImportArchive.showWindow(modbus);
                startTimerPoll();
                break;
            case "О программе...":
                WindowAbout.showWindow();
                break;
        }
    }

    /**
     * Закрыть приложение.
     */
    private void exitApp(){
        modbus.portClose();
        DB.closeDB();
        stage.close();
        Platform.exit();
    }

    /**
     * Основной таймер опроса устройств.
     */
    private void timerSec_Tick(){
        switch (modbus.getMode()){
            case None, PortClosed -> modbus.portOpen();
            case PortOpen -> modbus.Poll();
        }
    }

    /**
     * Клик мыши по таблице.
     * @param e
     */
    public void tableDevice_MouseClicked(MouseEvent e){
        TableView sourse=(TableView) e.getSource();
        if(e.getButton().equals(MouseButton.PRIMARY)){
            var selestedElem=sourse.getSelectionModel().getSelectedItems();
            if(selestedElem.size()==0) return;
            ClassDevice device=(ClassDevice)selestedElem.get(0);
            deviceName.setText(device.get_Name());
            if(device ==null) return;
            if(e.getClickCount()==2){
                userControlChannels=UserControlsFactory.createTable(FXCollections.observableArrayList(device.getChannels())
                        ,UserControlsFactory.HEADES_CHANNEL,
                        UserControlsFactory.VARIABLES_CHANNEL,
                        new ClassChannel());
                userControlChannels.setUserData(device);
                //userControlChannels.setOnMouseClicked(this::channels_MouseClicked);
                sPane.getItems().set(1,userControlChannels);
            }
        }
    }

    private void channels_MouseClicked(MouseEvent e){
        //XSSFWorkbook workbook = new XSSFWorkbook();
    }

    /**
     * Клик мыши по дереву устройств.
     * @param mouseEvent
     */
    public void treeView_MouseClicked(MouseEvent mouseEvent) {
        var listItems=treeView.getSelectionModel().getSelectedItems();
        if(listItems.isEmpty()) return;
        TreeItem item = (TreeItem) listItems.get(0);
        var idNode=  ((Pair<Integer,String>)item.getValue()).getKey();
        if(idNode == 0){
            sPane.getItems().set(1,userControlDevices);
            deviceName.setText("Не выбрано");
       }
       else{
           var ch=Channels.stream().filter(e->e.get_Device().getId()==idNode).toList();
            userControlChannels=UserControlsFactory.createTable(FXCollections.observableArrayList(ch),UserControlsFactory.HEADES_CHANNEL,
                                                               UserControlsFactory.VARIABLES_CHANNEL,new ClassChannel());
            sPane.getItems().set(1,userControlChannels);
            var id=((Pair<Integer,String>)item.getValue()).getKey();
            ClassDevice deviceCurrent=Devices.filtered(dev->dev.getId()==id).get(0);
            userControlChannels.setUserData(deviceCurrent);
            deviceName.setText(item.getValue().toString());
        }
    }

    public TreeView<Pair<Integer,String>> getTreeView(){
        return treeView;
    }
}