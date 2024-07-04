package org.example.mmsd_al.Windows;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.mmsd_al.StartApplication;

import java.io.IOException;
import java.util.ArrayList;

public class WindowProcess30 {

    public Label lab;

    private Stage stage;

    private WindowImportArchive sp;
    public WindowProcess30(WindowImportArchive sp){
        this.sp=sp;

    }

    public Stage getStage() {
        return stage;
    }

    //    public static boolean showWindow() {
//        stage = new Stage();
//        Scene scene;
//        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowProcess30.fxml"));
//        fxmlLoader.setControllerFactory(WindowProcess30::call);
//        try {
//            scene = new Scene(fxmlLoader.load());
//        } catch (IOException e) {
//            return false;
//        }
//        stage.setTitle("Подготовка архива");
//        stage.getIcons().add(new Image("/about.png"));
//        stage.initModality(Modality.WINDOW_MODAL);
//        stage.setResizable(false);
//        stage.setScene(scene);
//        stage.showAndWait();
//        return true;
//    }
//
//    public static Object call(Class<?> call) {
//        return new WindowProcess30();
//    }


    @FXML
    public void initialize(){
      lab.textProperty().bindBidirectional(sp.spProperty());
        //lab.textProperty().set(sp.getValue());
    }

    public boolean showWindow(WindowProcess30 windowProcess30) {
        stage = new Stage();
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowProcess30.fxml"));
        fxmlLoader.setControllerFactory(call->windowProcess30);
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            return false;
        }
        stage.setTitle("Подготовка архива");
        stage.getIcons().add(new Image("/about.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
        return true;
    }




}
