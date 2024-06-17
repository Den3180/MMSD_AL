package org.example.mmsd_al.Windows;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.mmsd_al.Archive.ClassDeviceArchive;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.MainWindow;
import org.example.mmsd_al.StartApplication;

import java.io.IOException;

public class WindowImportArchive {

    private Stage stage;
    private Scene scene;
    private int numRecords;
    private ClassDeviceArchive deviceArchive;
    private ObservableList<ClassDevice> deviceWithArchive;

    @FXML
    private ComboBox devArchive;
    @FXML
    private TextField availableRecords;
    @FXML
    private TextField countLoadRecords;
    @FXML
    private TextField startRecords;


    public WindowImportArchive(){
        deviceWithArchive= FXCollections.observableArrayList(MainWindow.Devices.filtered(
                dev->dev.get_Model().equals(ClassDevice.EnumModel.BKM_5)));
        deviceArchive = new ClassDeviceArchive();

    }

    @FXML
    public void initialize(){
        devArchive.setItems(deviceWithArchive);
        devArchive.getSelectionModel().select(0);
        //availableRecords.setText(String.valueOf(numRecords));
    }

    public boolean showWindow() {
        stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowImportArchive.fxml"));
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

    }
}
