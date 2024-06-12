package org.example.mmsd_al.Windows;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import jssc.SerialPortList;
import org.example.mmsd_al.MainWindow;
import org.example.mmsd_al.Settings.ClassSettings;
import org.example.mmsd_al.StartApplication;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WindoWConfig {

    private Stage stage;
    private Scene scene;
    private boolean settingsChange;
    private ClassSettings settings;

    @FXML
    private TextField timeout;
    @FXML
    private ComboBox ports;
    @FXML
    private ComboBox baudRate;
    @FXML
    private ComboBox dataBits;
    @FXML
    private ComboBox parity;
    @FXML
    private ComboBox stopBits;

    private ObservableList<Integer> buadRateList;
    private ObservableList<Integer> dataBitsList;
    private ObservableList<String> parityList;
    private ObservableList<Integer> stopBitsList;
    private String[] portList;

    public WindoWConfig(){
        buadRateList=FXCollections.observableArrayList(4800,9600,19200,38400,57600,115200);
        dataBitsList=FXCollections.observableArrayList(5,6,7,8,9);
        parityList=FXCollections.observableArrayList("None","Odd","Even","Mark","Space");
        stopBitsList=FXCollections.observableArrayList(1,2);
        portList=SerialPortList.getPortNames();
        settingsChange=false;
        settings=MainWindow.settings;
    }

    public void initialize(){
        int portInSettings=settings.getPortModbus();
        if(portList.length==0) {
            ports.getItems().setAll(FXCollections.observableArrayList("нет"));
        }
        else {
            ports.getItems().setAll(FXCollections.observableArrayList(SerialPortList.getPortNames()));
        }
        if(portInSettings>portList.length-1){
            ports.getSelectionModel().selectFirst();
        }
        else{
            ports.getSelectionModel().select(portInSettings);
        }
        baudRate.getItems().setAll(buadRateList);
        dataBits.getItems().setAll(dataBitsList);
        parity.getItems().setAll(parityList);
        stopBits.getItems().setAll(stopBitsList);
        baudRate.getSelectionModel().select(Integer.valueOf(settings.getBaudRate()));
        dataBits.getSelectionModel().select(Integer.valueOf(settings.getDataBits()));
        parity.getSelectionModel().select(settings.getParity());
        stopBits.getSelectionModel().select(settings.getStopBits()-1);
        timeout.setText(String.valueOf(settings.getTimeout()));
    }

    public boolean showWindow(){
        stage =new Stage();
        FXMLLoader fxmlLoader=new FXMLLoader(StartApplication.class.getResource("WindoWConfig.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Параметры");
        stage.getIcons().add(new Image("/about.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
        return stage.getUserData()==null ? false: (boolean)stage.getUserData();
    }

    public void button_Click(ActionEvent actionEvent) {
        settings.setPortModbus(ports.getSelectionModel().getSelectedIndex());
        settings.setBaudRate(Integer.valueOf(buadRateList.get(baudRate.getSelectionModel().getSelectedIndex())));
        settings.setDataBits(Integer.valueOf(dataBitsList.get(dataBits.getSelectionModel().getSelectedIndex())));
        settings.setParity(parity.getSelectionModel().getSelectedIndex());
        settings.setStopBits(stopBits.getSelectionModel().getSelectedIndex()+1);
        settings.setTimeout(Integer.valueOf(timeout.getText()));
        settingsChange= !settings.equals(ClassSettings.load());
        if(settingsChange==true){
            settings.save();
        }
        Window window= (((Button)actionEvent.getSource()).getScene()).getWindow();
        window.setUserData(settingsChange);
        ((Stage)window).close();
    }
}
