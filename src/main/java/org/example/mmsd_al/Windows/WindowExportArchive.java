package org.example.mmsd_al.Windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.example.mmsd_al.Archive.ClassDeviceArchive;
import org.example.mmsd_al.Classes.ClassModbus;
import org.example.mmsd_al.StartApplication;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class WindowExportArchive {

    public TextField port;
    public TextField ip;
    private ArrayList<Integer[]> resTotal;

    public WindowExportArchive(ArrayList<Integer[]> resTotal){
        this.resTotal=resTotal;
    }


    public static boolean showWindow(ArrayList<Integer[]> resTotal) {
        Stage stage = new Stage();
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowExportArchive.fxml"));
        fxmlLoader.setControllerFactory(call->new WindowExportArchive(resTotal));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            return false;
        }
        stage.setTitle("Экспорт архива");
        stage.getIcons().add(new Image("/about.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
        return true;
    }

    public void click_Button(ActionEvent actionEvent) {

        Button button=(Button) actionEvent.getSource();
        Window window= (button.getScene()).getWindow();
        ((Stage)window).close();
        if(button.isCancelButton()){
            return;
        }
        //TODO Проверка на заполненность полей.

        var iptmp=this.ip.getText();
        var porttmp=this.port.getText();
        String ip=iptmp==null || iptmp.isEmpty()? "127.0.0.1" : iptmp;
        int port=porttmp==null || porttmp.isEmpty()? 0 : Integer.parseInt(this.port.getText());
        ClassDeviceArchive.sendArchiveDevice(resTotal,ip,port);
    }
}
