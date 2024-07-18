package org.example.mmsd_al.Windows;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.MainWindow;
import org.example.mmsd_al.StartApplication;
import org.sqlite.core.DB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class WindowDevice {

    @FXML
    private TextField nameDev;
    @FXML
    private TextField picket_km;
    @FXML
    private TextField picket_m;
    @FXML
    private TextField latitude;
    @FXML
    private TextField longitude;
    @FXML
    private TextField elevation;
    @FXML
    private ComboBox model;
    @FXML
    private ComboBox protocol;
    @FXML
    private TextField ipAddress;
    @FXML
    private TextField ipPort;
    @FXML
    private TextField address;
    @FXML
    private TextField txtSIM;
    @FXML
    private TextField period;


    private ClassDevice device;
    private ObservableList<String> modelList;
    private ObservableList<ClassDevice.EnumProtocol> protocolsList;

    public WindowDevice(ClassDevice device){
        this.device=device;
        modelList= FXCollections.observableArrayList(ClassDevice.EnumModel.getModelList());
        protocolsList= FXCollections.observableArrayList(ClassDevice.EnumProtocol.values());
    }

    public void initialize(){
        //Заполнение комбобоксов.
        model.setItems(modelList);
        protocol.setItems(protocolsList);
        //Проверка на наличие устройства(режим добавить).
        if(device==null) {
            model.getSelectionModel().selectFirst();
            protocol.getSelectionModel().selectFirst();
            device=new ClassDevice();
            return;
        }
        //Заполнение полей(режим редактировать).
        model.getSelectionModel().select(device.get_ModelName());
        protocol.getSelectionModel().select(device.get_Protocol());
        nameDev.setText(device.get_Name());
        latitude.setText(String.valueOf(device.get_Latitude()));
        longitude.setText(String.valueOf(device.get_Longitude()));
        elevation.setText(String.valueOf(device.get_Elevation()));
        ipAddress.setText(device.get_IPAddress());
        ipPort.setText(String.valueOf(device.get_IPPort()));
        address.setText(String.valueOf(device.get_Address()));
        txtSIM.setText(device.get_SIM());
        period.setText(String.valueOf(device.get_Period()));
        //Заполнение полей пикетов.
        if(!device.get_Picket().isEmpty()){
            var tempest=device.get_Picket();
            var arrest=tempest.split("\\+");
            picket_km.setText(arrest[0].substring(2));
            picket_m.setText(arrest[1]);
        }
    }

    public static boolean showWindow(ClassDevice device){

        Stage stage = new Stage();
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowDevice.fxml"));
        fxmlLoader.setControllerFactory(call->new WindowDevice(device));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            return false;
        }
        stage.setTitle("Устройство");
        stage.getIcons().add(new Image("/about.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
        return true;
    }

    @FXML
    public void btn_Click(ActionEvent actionEvent) {
        Button button=(Button) actionEvent.getSource();
        Window window= (((Button)actionEvent.getSource()).getScene()).getWindow();
        if(button.isCancelButton()){
            ((Stage)window).close();
            return;
        }
        //Добавить устройтсво.
        if(device.getId()==0){
            MainWindow.DB.deviceAdd(setDevice());
        }
        else{
            int i=0;
        }
        ((Stage)window).close();
    }

    private ClassDevice setDevice(){
        ClassDevice dev=new ClassDevice();
        device.set_Name(nameDev.getText());
        device.set_Picket("ПК"+picket_km.getText()+"+"+picket_m.getText());
        var lat=latitude.getText().isEmpty() ? 0D:Double.parseDouble(latitude.getText());
        device.set_Latitude(lat);
        return dev;
    }
}
