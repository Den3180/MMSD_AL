package org.example.mmsd_al.Windows;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import jssc.SerialPortList;
import org.example.mmsd_al.Archive.ClassDeviceArchive;
import org.example.mmsd_al.Classes.ClassModbus;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.MainWindow;
import org.example.mmsd_al.ServiceClasses.ClassDelay;
import org.example.mmsd_al.StartApplication;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class WindowImportArchive  {

    private Stage stage;
    private Scene scene;
    private int numRecords;
    private ClassDeviceArchive deviceArchive;
    private ObservableList<ClassDevice> deviceWithArchive;
    private int addressRegAO = 133;
    private int addressRegAI = 53;
    private int endPos;
    private int deviceAddress;
    private ClassModbus modbus;
    ClassDevice device;

    @FXML
    public Button apply;
    @FXML
    private ComboBox devArchiveComboBox;
    @FXML
    public TextField availableRecords;
    @FXML
    private TextField countLoadRecords;
    @FXML
    private TextField startRecords;


    public WindowImportArchive(ClassModbus modbus){
        this.modbus = modbus;
        deviceWithArchive= FXCollections.observableArrayList(MainWindow.Devices.filtered(
                dev->dev.get_Model().equals(ClassDevice.EnumModel.BKM_5)));
        deviceArchive = new ClassDeviceArchive(this.modbus);
        endPos=0;
    }

    int [] dataNoteCount=null;

    @FXML
    public void initialize(){
        devArchiveComboBox.setItems(deviceWithArchive);
        devArchiveComboBox.getSelectionModel().select(0);
        device=(ClassDevice) devArchiveComboBox.getSelectionModel().getSelectedItem();
        deviceAddress=device.get_Address();
        int [] dataNoteCount=deviceArchive.GetCountNoteArchive(deviceAddress,addressRegAO);
        availableRecords.setText(String.valueOf(dataNoteCount[0]));
        if(availableRecords.getText()!="нет данных"){
            countLoadRecords.setText("0");
            startRecords.setText("0");
        }
        if(Objects.equals(availableRecords.getText(), "0")) {
            apply.setDisable(true);
            countLoadRecords.setDisable(true);
            startRecords.setDisable(true);
        }
    }

    public static boolean showWindow(ClassModbus modbus) {
        Stage stage = new Stage();
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowImportArchive.fxml"));
        fxmlLoader.setControllerFactory(call->new WindowImportArchive(modbus));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            return false;
        }
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                var currentPort=MainWindow.settings.getPortModbus();
                modbus.getPortParametres().setDevice(SerialPortList.getPortNames()[currentPort]);
            }
        });
        stage.setTitle("Загрузка архива");
        stage.getIcons().add(new Image("/about.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
        return true;
    }

    @FXML
    public void click_Button(ActionEvent actionEvent) {
        Button button=(Button) actionEvent.getSource();
        Window window= (button.getScene()).getWindow();
        ((Stage)window).close();
        if(!button.isCancelButton()){
            Thread thread=new Thread(this::getDeviceArchive);
            thread.setDaemon(true);
            thread.start();
        }
        else {
            var currentPort=MainWindow.settings.getPortModbus();
            modbus.getPortParametres().setDevice(SerialPortList.getPortNames()[currentPort]);
        }
    }



     private SimpleIntegerProperty intprop=new SimpleIntegerProperty(0);

    private void getDeviceArchive(){

        //TODO Добавить блок try-catch. Выход за границы массива происходит.
        int startPos= Integer.parseInt(startRecords.getText());
        numRecords=Integer.valueOf(countLoadRecords.getText());
        int endpos=startPos+numRecords;
        WindowProcess30 windowProcess30=new WindowProcess30(this);
        Platform.runLater(()->windowProcess30.showWindow(windowProcess30));

        AtomicInteger finalStartPos = new AtomicInteger();
        while(startPos<endpos){
            deviceArchive.readArchive_30(deviceAddress,startPos);
            Platform.runLater(()->setIntprop(finalStartPos.get()));
            finalStartPos.getAndIncrement();
            startPos++;
        }

        startPos=Integer.parseInt(startRecords.getText());
        int countNotCurr=0;
        var note_30=deviceArchive.getNote_30();
        while(startPos<endpos){
            int [] noteHeader=note_30.get(countNotCurr);
            countNotCurr++;
            int sizeBlockNote = noteHeader[4] * 100 + noteHeader[5];
            int numBlocks = sizeBlockNote % 200 == 0 ? sizeBlockNote / 200 : sizeBlockNote / 200 + 1;
            int countBlock = 0;
            while (countBlock < numBlocks){
                deviceArchive.readArchive_31(deviceAddress,startPos,countBlock);
                countBlock++;
            }
            Platform.runLater(()->setIntprop(finalStartPos.get()));
            finalStartPos.getAndIncrement();
            startPos++;
        }
            ClassDelay.delay(1000);
        Platform.runLater(()->windowProcess30.getStage().close());
        deviceArchive.closeSerialPort();
        deviceArchive.processArchive();
        var currentPort=MainWindow.settings.getPortModbus();
        modbus.getPortParametres().setDevice(SerialPortList.getPortNames()[currentPort]);
    }

    public int getIntprop() {
        return intprop.get();
    }

    public SimpleIntegerProperty intpropProperty() {
        return intprop;
    }

    public void setIntprop(int intprop) {
        this.intprop.set(intprop);
    }

    public int getNumRecords() {
        return numRecords;
    }
}
