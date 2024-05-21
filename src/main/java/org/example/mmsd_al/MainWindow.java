package org.example.mmsd_al;

import org.example.mmsd_al.Settings.ClassSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class MainWindow {

    private Stage stage=StartApplication.stage;
    public static ClassSettings settings;

    @FXML
    private TreeView treeView;
    @FXML
    private MenuBar mainMenu;

    public void initialize(){
        settings=new ClassSettings();
    }

    @FXML
    public void button_Click(ActionEvent actionEvent) {
//        Button bb=(Button)actionEvent.getSource();
//        Stage stage = (Stage) bb.getScene().getWindow();
//        stage.close();

        settings.setBaudRate(88888888);
        settings.setPassword("Password");
        settings.save();
        var s2= ClassSettings.load();
        System.out.println(s2.getBaudRate() + " "+s2.getPassword());
    }

    @FXML
    public void menuItemClick(ActionEvent actionEvent) {
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