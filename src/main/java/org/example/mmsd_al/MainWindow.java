package org.example.mmsd_al;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.example.mmsd_al.DBClasses.ClassDB;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.ServiceClasses.ClassDialog;
import org.example.mmsd_al.Settings.ClassSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;


public class MainWindow {

    private Stage stage=StartApplication.stage;
    public static ClassSettings settings;
    public static ClassDB DB;

    @FXML
    private TreeView treeView;
    @FXML
    private MenuBar mainMenu;

    public void initialize(){
        settings=new ClassSettings();
    }

    @FXML
    public void button_Click(ActionEvent actionEvent) {

//
        ClassDevice devout=new ClassDevice();
        devout.set_Name("Dev5555");
        devout.set_ComPort("Com3");
        devout.set_DTAct(LocalDate.now());
        devout.saveProfile(ClassDialog.saveDialog(stage).getAbsolutePath());

        File selectedFile = ClassDialog.openDialog(stage);
        ClassDevice devin=ClassDevice.load(selectedFile.getAbsolutePath());
Stage st=new Stage();
st.show();
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