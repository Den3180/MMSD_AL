package org.example.mmsd_al.Windows;

import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.util.Callback;
import org.example.mmsd_al.Archive.ClassDeviceArchive;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.MainWindow;
import org.example.mmsd_al.StartApplication;

import java.io.IOException;

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
    private ModbusMaster master;

    @FXML
    private ComboBox devArchiveComboBox;
    @FXML
    private TextField availableRecords;
    @FXML
    private TextField countLoadRecords;
    @FXML
    private TextField startRecords;


    public WindowImportArchive(ModbusMaster master){
        this.master = master;
        deviceWithArchive= FXCollections.observableArrayList(MainWindow.Devices.filtered(
                dev->dev.get_Model().equals(ClassDevice.EnumModel.BKM_5)));
        deviceArchive = new ClassDeviceArchive(this.master);
        endPos=0;
    }

    @FXML
    public void initialize(){
        devArchiveComboBox.setItems(deviceWithArchive);
        devArchiveComboBox.getSelectionModel().select(0);
        ClassDevice device=(ClassDevice) devArchiveComboBox.getSelectionModel().getSelectedItem();
        deviceAddress=device.get_Address();
        int [] dataNoteCount=deviceArchive.GetCountNoteArchive(deviceAddress,addressRegAO);
        availableRecords.setText(String.valueOf(dataNoteCount[0]));
        if(availableRecords.getText()!="нет данных"){
            countLoadRecords.setText("0");
            startRecords.setText("0");
        }
    }

    public static boolean showWindow(ModbusMaster master) {
        Stage stage = new Stage();
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowImportArchive.fxml"));
        fxmlLoader.setControllerFactory(call->new WindowImportArchive(master));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            return false;
        }
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
        if(button.isCancelButton()){
            Window window= (button.getScene()).getWindow();
            ((Stage)window).close();
        }
        getDeviceArchive();
    }

    private void getDeviceArchive(){
        int startPos= Integer.parseInt(startRecords.getText());
        int endpos=startPos+Integer.valueOf(countLoadRecords.getText());
        while(startPos<endpos){
            deviceArchive.ReadArchive_30(deviceAddress,startPos);
        }
    }
}
