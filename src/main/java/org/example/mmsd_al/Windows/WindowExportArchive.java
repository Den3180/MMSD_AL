package org.example.mmsd_al.Windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.mmsd_al.Classes.ClassModbus;
import org.example.mmsd_al.StartApplication;

import java.awt.*;
import java.io.IOException;

public class WindowExportArchive {

    public TextField port;
    public TextField ip;


    public static boolean showWindow() {
        Stage stage = new Stage();
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowExportArchive.fxml"));
        fxmlLoader.setControllerFactory(call->new WindowExportArchive());
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            return false;
        }
        stage.setTitle("Экспорт архива");
        stage.getIcons().add(new Image("/about.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
        return true;
    }

    public void click_Button(ActionEvent actionEvent) {
        //TODO Дописать отправку по сокету.
        String ip=this.ip.getText();
        int port=Integer.parseInt(this.port.getText());
    }
}
