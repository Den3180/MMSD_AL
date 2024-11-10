package org.example.mmsd_al.Windows;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.mmsd_al.StartApplication;

import java.io.IOException;

public class WindowProcess30 {

    @FXML
    public Label lab;
    @FXML
    public ProgressBar pBar;
    @FXML
    public Label headerLab;
    @FXML
    private Stage stage;

    //Величина прогрессбара.
    private final double valueProgressBar;

    private WindowImportArchive sp;
    private double progress;
    private double progressText;


    public WindowProcess30(WindowImportArchive sp){
        //Ссылка на родительское окно. 
        this.sp=sp;
        //Установка величины прогрессбара.
        valueProgressBar= (double) 1 /(this.sp.getNumRecords()*2);
    }


    /**
     * Получить текущее окно.
     * @return 
     */
    public Stage getStage() {
        return stage;
    }

    @FXML
    public void initialize(){
        // Устанавливаем прослушку на изменение свойства
      sp.intpropProperty().addListener((obj,oval,nval)->{
          progressText +=Math.round(valueProgressBar*100);
          progress+= valueProgressBar;
          if(progress>0.5){
              headerLab.setText("Загрузка архива...");
          }
          if(progress<1){
              lab.setText(String.valueOf(Math.min((int) progressText, 100)));
              pBar.setProgress(progress);
          }
          else{
              lab.setText(String.valueOf(100));
              pBar.setProgress(1);
          }
      });
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
        stage.setTitle("Архив");
        stage.getIcons().add(new Image("/about.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
        return true;
    }
}
