package org.example.mmsd_al.Windows;

import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jssc.SerialPortList;
import org.example.mmsd_al.MainWindow;
import org.example.mmsd_al.StartApplication;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WindoWConfig {

    private Stage stage;
    private Scene scene;

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
        buadRateList=FXCollections.observableArrayList(1200,2400,4800,9600,19200,38400,57600,115200);
        dataBitsList=FXCollections.observableArrayList(5,6,7,8,9);
        parityList=FXCollections.observableArrayList("None","Odd","Even","Mark","Space");
        stopBitsList=FXCollections.observableArrayList(1,2);
        portList=SerialPortList.getPortNames();
    }

    public void initialize(){
        if(portList.length==0) {
            ports.getItems().setAll(FXCollections.observableArrayList("нет"));
        }
        else {
            ports.getItems().setAll(FXCollections.observableArrayList(SerialPortList.getPortNames()));
        }
        //if(MainWindow.settings.getPortModbus()>portList.length-1))
        //TODO Закончить со списком портов.
        ports.getSelectionModel().selectFirst();
        baudRate.getItems().setAll(buadRateList);
        dataBits.getItems().setAll(dataBitsList);
        parity.getItems().setAll(parityList);
        stopBits.getItems().setAll(stopBitsList);
        baudRate.getSelectionModel().select(Integer.valueOf(MainWindow.settings.getBaudRate()));
        dataBits.getSelectionModel().select(Integer.valueOf(MainWindow.settings.getDataBits()));
        parity.getSelectionModel().select(MainWindow.settings.getParity());
        stopBits.getSelectionModel().select(MainWindow.settings.getStopBits()-1);
        timeout.setText(String.valueOf(MainWindow.settings.getTimeout()));
    }

    public void showWindow(){
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
    }

    public void button_Click(ActionEvent actionEvent) {
        timeout.setText("button_Click");
    }
}
