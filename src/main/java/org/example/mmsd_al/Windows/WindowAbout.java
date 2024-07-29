package org.example.mmsd_al.Windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.StartApplication;

import java.io.IOException;

public class WindowAbout {

    public static boolean showWindow(){

        Stage stage = new Stage();
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowAbout.fxml"));
        fxmlLoader.setControllerFactory(call->new WindowAbout());
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            return false;
        }
        stage.setTitle("О программе");
        stage.getIcons().add(new Image("/about.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
        return true;
    }
}
