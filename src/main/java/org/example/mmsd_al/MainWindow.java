package org.example.mmsd_al;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.mmsd_al.DBClasses.ClassDB;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.ServiceClasses.ClassMessage;
import org.example.mmsd_al.Settings.ClassSettings;
import org.example.mmsd_al.UserControlsClasses.UserControlsSettings;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static javafx.scene.input.KeyCode.T;


public class MainWindow {

    public static ClassSettings settings;
    public static ClassDB DB;
    public static ObservableList<ClassDevice> Devices;
    private Stage stage=StartApplication.stage;
    private File dbFile;

    @FXML
    private TreeView treeView;
    @FXML
    private MenuBar mainMenu;
    @FXML
    private AnchorPane mainTable;
    @FXML
    private TableView userControlDevices;

    public void initialize(){
        settings=ClassSettings.load();
        DB=new ClassDB();
        dbFile=new File(settings.getdB());
        if(!dbFile.exists()){
           ButtonType buttonType= ClassMessage.showMessage("База данных","СУБД","Файл БД не доступен!\nСоздать БД?",
                                      Alert.AlertType.CONFIRMATION);
           if(buttonType.getButtonData()== ButtonBar.ButtonData.OK_DONE){
               ClassDB.create(null);
           }
        }
        if(!DB.open(settings)){
            ClassMessage.showMessage("База данных","СУБД","БД не доступна!\nПроверьте конфигурацию",
                    Alert.AlertType.CONFIRMATION);
        }
        Devices= FXCollections.observableArrayList(DB.devicesLoad());

        switch (settings.getStartWindow()){
            case 0:
                userControlDevices = getTable(Devices, UserControlsSettings.HEADERS_DEVICE,UserControlsSettings.VARIABLES_DEVICE);
                mainTable.getChildren().add(userControlDevices);
                break;
        }

    }

    private <T> TableView<T> getTable(ObservableList<T> list,String[]headers, String[]variables){
        TableView<T> tableView=new TableView<>(list);
        //tableView.setEditable(true);
        var elemList=list.get(0);
           for(int i=0;i<headers.length;i++){
               try {
                        var curField=elemList.getClass().getDeclaredField(variables[i]);
                        curField.setAccessible(true);
                        var nv=curField.get(elemList);
                        tableView.getColumns().add(setCellTable(elemList,nv,headers[i],variables[i]));
                   } catch (Exception e) {
                       throw new RuntimeException(e);
                   }
           }
        return tableView;
    }

    private <T,S> TableColumn<T,S> setCellTable(T obj1, S obj2,String hdr, String vrb){
        TableColumn<T,S> col = new TableColumn<T,S>(hdr);
        col.setCellValueFactory(new PropertyValueFactory<T,S>(vrb));
        col.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<S>() {
            @Override
            public String toString(S object) {
                return object.toString();
            }

            @Override
            public S fromString(String string) {
                return (S)string;
            }
        }));
        col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<T,S>>() {
            @Override
            public void handle(TableColumn.CellEditEvent event) {

            }
        });
            return col;
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
        stage.close();
        Platform.exit();
    }
}