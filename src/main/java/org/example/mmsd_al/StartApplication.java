package org.example.mmsd_al;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.mmsd_al.ServiceClasses.ClassMessage;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class StartApplication extends Application {

    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {

        // Берем размер всего экрана
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //int width = screenSize.width;
        //int height = screenSize.height;

        StartApplication.stage =stage;
        //stage.setMinWidth(width);
        //stage.setMinHeight(height);
        stage.setMaximized(true);
        stage.setResizable(true);
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ООО НПК 'ТехноПром' - Система Оперативной Телеметрии и Комплексного Анализа");
        Image image=new Image(Objects.requireNonNull(StartApplication.class.getResourceAsStream("/about.png")));
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}