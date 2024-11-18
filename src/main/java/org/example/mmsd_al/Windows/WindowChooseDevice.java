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
import org.example.mmsd_al.Classes.ClassModbus;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.MainWindow;
import org.example.mmsd_al.StartApplication;

import java.io.IOException;

public class WindowChooseDevice {

    private ObservableList<ClassDevice> deviceWithArchive;
    private ClassDevice device;
    @FXML
    public Button apply;
    @FXML
    private ComboBox devArchiveComboBox;

    public WindowChooseDevice(){
        deviceWithArchive= FXCollections.observableArrayList(MainWindow.Devices.filtered(
                dev->dev.get_Model().equals(ClassDevice.EnumModel.BKM_5)));
    }

    @FXML
    public void initialize(){
        //Связываем список устройств с комбобоксом.
        devArchiveComboBox.setItems(deviceWithArchive);
        //Стартовый элемент нулевой.
        devArchiveComboBox.getSelectionModel().select(0);
        device=(ClassDevice) devArchiveComboBox.getSelectionModel().getSelectedItem();
    }

    public static Object showWindow() {
        Stage stage = new Stage();
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowChooseDevice.fxml"));
        fxmlLoader.setControllerFactory(call->new WindowChooseDevice());
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            return false;
        }
        stage.setTitle("Выбор устройства");
        stage.getIcons().add(new Image("/about.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();

        return stage.getUserData();
    }

    @FXML
    public void click_Button(ActionEvent actionEvent) {
        //Получить источник события.
        Button button=(Button) actionEvent.getSource();
        //Получить главное окно.
        Window window= (button.getScene()).getWindow();
        //Закрыть главное окно.
        ((Stage)window).close();
        //Если кнопка, которая нажата это не кнопка отмены.
        if(!button.isCancelButton()){
            device=(ClassDevice) devArchiveComboBox.getSelectionModel().getSelectedItem();
            window.setUserData(device);
        }
    }
}
