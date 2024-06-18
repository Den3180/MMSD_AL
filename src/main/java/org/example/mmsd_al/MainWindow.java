package org.example.mmsd_al;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
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
import org.example.mmsd_al.Windows.WindowImportArchive;
import org.jetbrains.annotations.NotNull;


import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


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
    public static Object locker = new Object();
    public static int count=0;

    @FXML
    private TreeView treeView;
    @FXML
    private MenuBar mainMenu;
    @FXML
    private SplitPane sPane;
    private TableView userControlDevices;
    private TableView userControlChannels;

    @FXML
    private Label lbTest;

    public void initialize(){
        mainWindow=this;
        settings=ClassSettings.load();
        DB=new ClassDB();
        dbFile=new File(settings.getdB());
        if(!dbFile.exists()){
           ButtonType buttonType= ClassMessage.showMessage("База данных","СУБД","Файл БД не доступен!\nСоздать БД?",
                                      Alert.AlertType.CONFIRMATION);
           if(buttonType.getButtonData()== ButtonBar.ButtonData.OK_DONE){
               ClassDB.create(null);
           }
        }
        if(!DB.open(settings)){
            ClassMessage.showMessage("База данных","СУБД","БД не доступна!\nПроверьте конфигурацию",
                    Alert.AlertType.CONFIRMATION);
        }
        //Получить список устройств из базы данных.
        Devices= (FXCollections.observableArrayList(DB.devicesLoad()));
        //Установка порта в таблице устройств.
        Devices.forEach(el->el.set_ComPort(String.valueOf(settings.getPortModbus())));
        //Все каналы устройств в БД.
        Channels=FXCollections.observableArrayList(DB.registriesLoad(0));
        //Разбивка каналов по устройствам.
        for(ClassDevice dev : Devices){
            dev.setChannels(Channels.stream().filter(el->el.get_Device().getId()==dev.getId())
                    .sorted(new ChannelCompareTypeReg().thenComparing(new ChannelCompareAddress())).toList());
            dev.setGroups(dev.getGroups());
        }

        //Построение дерева устройств.
        treeView.setRoot(TreeViewFactory.createRootTree(Devices,new Pair<Integer,String>(0,"Устройство"){
            @Override
            public String toString(){
            return this.getValue();
            }
        }));

        //Выбор стартовой таблицы.
        switch (settings.getStartWindow()){
            case 0:
                userControlDevices = UserControlsFactory.createTable(Devices, UserControlsFactory.HEADERS_DEVICE,
                        UserControlsFactory.VARIABLES_DEVICE, new ClassDevice());
                sPane.getItems().set(1,userControlDevices);
                userControlDevices.setOnMouseClicked(this::tableDevice_MouseClicked);
                break;
        }
        modbus=new ClassModbus();
        startTimerPoll();
    }

    /**
     * запуск таймера основног опроса.
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

    @FXML
    public void button_Click(ActionEvent actionEvent) {
        ObservableList cols=userControlDevices.getColumns();
        var fact=userControlDevices.getRowFactory();
        TableColumn col=(TableColumn)cols.get(0);
        col.setVisible(false);
        if (fact != null) {

        }

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
                break;
            case "Каналы данных...":
                break;
            case "База данных...":
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
                break;
            case "Отправить архив...":
                break;
            case "Загрузить архив...":
                timerSec.cancel();
                //WindowImportArchive windowChooseNote=new WindowImportArchive(new Object());
                //windowChooseNote.
                //windowChooseNote.showWindow(new Object());
                WindowImportArchive.showWindow(modbus.getModbusMaster());
                startTimerPoll();
                break;
            case "О программе...":
                break;
        }
    }


    /**
     * Закрыть приложение.
     */
    private void exitApp(){
        DB.closeDB();
        stage.close();
        Platform.exit();
    }
    int temp=0;
    private void timerSec_Tick(){
        Platform.runLater(()-> lbTest.setText("Круг: "+ ++temp));
//        Platform.runLater(()-> lbTest.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH.mm.ss"))));

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
            if(device ==null) return;
            if(e.getClickCount()==2){
                userControlChannels=UserControlsFactory.createTable(FXCollections.observableArrayList(device.getChannels())
                        ,UserControlsFactory.HEADES_CHANNEL,
                        UserControlsFactory.VARIABLES_CHANNEL,
                        new ClassChannel());
                sPane.getItems().set(1,userControlChannels);
            }
        }
    }

    /**
     * Клик мыши по дереву устройств.
     * @param mouseEvent
     */
    public void treeView_MouseClicked(MouseEvent mouseEvent) {
       TreeItem item= (TreeItem) treeView.getSelectionModel().getSelectedItems().get(0);
       var idNode=  ((Pair<Integer,String>)item.getValue()).getKey();
        if(idNode == 0){
            sPane.getItems().set(1,userControlDevices);
       }
       else{
           var ch=Channels.stream().filter(e->e.get_Device().getId()==idNode).toList();
            userControlChannels=UserControlsFactory.createTable(FXCollections.observableArrayList(ch),UserControlsFactory.HEADES_CHANNEL,
                                                               UserControlsFactory.VARIABLES_CHANNEL,new ClassChannel());
            sPane.getItems().set(1,userControlChannels);
       }
    }
}