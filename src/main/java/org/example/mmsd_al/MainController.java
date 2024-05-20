package org.example.mmsd_al;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class MainController {

    private Stage stage=StartApplication.stage;

    @FXML
    private TreeView treeView;
    @FXML
    private MenuBar mainMenu;

    public void initialize(){

    }

    @FXML
    public void button_Click(ActionEvent actionEvent) {
        Button bb=(Button)actionEvent.getSource();
        Stage stage = (Stage) bb.getScene().getWindow();
        stage.close();
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