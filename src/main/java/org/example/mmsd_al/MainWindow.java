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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;


public class MainWindow {

    public static ClassSettings settings;
    public static ClassDB DB;
    public static ObservableList<ClassDevice> Devices;
    public static ObservableList<ClassChannel> Channels;
    public  static ClassModbus modbus;
    private Stage stage=StartApplication.stage;
    private File dbFile;
    private Timer timerSec;

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
        Devices= FXCollections.observableArrayList(DB.devicesLoad());
        Channels=FXCollections.observableArrayList(DB.registriesLoad(0));
        timerSec=new Timer(true);
        modbus=new ClassModbus();

        for(ClassDevice dev : Devices){
            dev.setChannels(Channels.stream().filter(el->el.get_Device().getId()==dev.getId())
                    .sorted(new ChannelCompareTypeReg().thenComparing(new ChannelCompareAddress())).toList());
        }

        timerSec.schedule(new TimerTask() {
            @Override
            public void run() {
                Runnable r=()->timerSec_Tick();
                Thread thread=new Thread(r);
                thread.setDaemon(true);
                thread.start();
             //Platform.runLater(()->timerSec_Tick());
            }
        },0,1000);


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
    }

    @FXML
    public void button_Click(ActionEvent actionEvent) {
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
                break;
            case "Создать БД...":
                break;
            case "Отправить архив...":
                break;
            case "Загрузить архив...":
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

    private void timerSec_Tick(){
        Platform.runLater(()-> lbTest.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH.mm.ss"))));
        if(modbus.getMode()==ClassModbus.eMode.None){
            modbus.portOpen();
            return;
        }
        modbus.Poll();
    }

    public void tableDevice_MouseClicked(MouseEvent e){
        TableView sourse=(TableView) e.getSource();
        ClassDevice device=(ClassDevice) sourse.getSelectionModel().getSelectedItems().get(0);
        if(device ==null) return;
        if(e.getButton().equals(MouseButton.PRIMARY)){
            if(e.getClickCount()==2){
                userControlChannels=UserControlsFactory.createTable(FXCollections.observableArrayList(device.getChannels())
                        ,UserControlsFactory.HEADES_CHANNEL,
                        UserControlsFactory.VARIABLES_CHANNEL,
                        new ClassChannel());
                sPane.getItems().set(1,userControlChannels);
            }
        }
    }

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