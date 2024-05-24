package org.example.mmsd_al;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.mmsd_al.DBClasses.ClassDB;
import org.example.mmsd_al.ServiceClasses.ClassMessage;
import org.example.mmsd_al.Settings.ClassSettings;
import org.jetbrains.annotations.NotNull;

import java.io.File;


public class MainWindow {

    public static ClassSettings settings;
    public static ClassDB DB;
    private Stage stage=StartApplication.stage;
    private File dbFile;

    @FXML
    private TreeView treeView;
    @FXML
    private MenuBar mainMenu;

    public void initialize(){
        settings=ClassSettings.load();
        DB=new ClassDB();
        dbFile=new File(settings.getdB());
        if(!dbFile.exists()){
           ButtonType buttonType= ClassMessage.showMessage("База данных",null,"Файл БД не доступен!\nСоздать БД?",
                                      Alert.AlertType.CONFIRMATION);
           if(buttonType.getButtonData()== ButtonBar.ButtonData.OK_DONE){
               ClassDB.create(null);
           }
        }
    }

    @FXML
    public void button_Click(ActionEvent actionEvent) {

    }

    @FXML
    public void menuItemClick(@NotNull ActionEvent actionEvent) {
        MenuItem menuItem= (MenuItem)actionEvent.getSource();
        if(menuItem==null) return;
        String nameMenu=menuItem.getText();
        switch (nameMenu){
            case "Выход...":
                exitApp();
                break;
            case "Устройства...":
                break;
            case "Каналы данных...":
                break;
            case "База данных...":
                break;
            case "Параметры...":
                break;
            case "Создать БД...":
                break;
            case "Отправить архив...":
                break;
            case "Загрузить архив...":
                break;
            case "О программе...":
                break;
        }
    }

    private void exitApp(){
       //Platform.exit();
        stage.close();
    }
}