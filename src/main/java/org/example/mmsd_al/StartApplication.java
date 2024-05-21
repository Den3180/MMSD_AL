package org.example.mmsd_al;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class StartApplication extends Application {

    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        StartApplication.stage =stage;
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ООО НПК 'ТехноПром' - Система Оперативной Телеметрии и Комплексного Анализа");
       Image image=new Image(Objects.requireNonNull(StartApplication.class.getResourceAsStream("/about.png")));
        stage.getIcons().add(image);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}