package org.example.mmsd_al.Windows;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.MainWindow;
import org.example.mmsd_al.ServiceClasses.ClassMessage;
import org.example.mmsd_al.StartApplication;

import java.io.IOException;

public class WindowDevice {

    @FXML
    private TextField nameDev;
    @FXML
    private TextField picket_km;
    @FXML
    private TextField picket_m;
    @FXML
    private TextField latitude;
    @FXML
    private TextField longitude;
    @FXML
    private TextField elevation;
    @FXML
    private ComboBox<String> model;
    @FXML
    private ComboBox<ClassDevice.EnumProtocol> protocol;
    @FXML
    private TextField ipAddress;
    @FXML
    private TextField ipPort;
    @FXML
    private TextField address;
    @FXML
    private TextField txtSIM;
    @FXML
    private TextField period;


    private ClassDevice device;
    private ObservableList<String> modelList;
    private ObservableList<ClassDevice.EnumProtocol> protocolsList;

    public WindowDevice(ClassDevice device){
        this.device=device;
        modelList= FXCollections.observableArrayList(ClassDevice.EnumModel.getModelList());
        protocolsList= FXCollections.observableArrayList(ClassDevice.EnumProtocol.values());
    }

    public void initialize(){
        //Заполнение комбобоксов.
        model.setItems(modelList);
        protocol.setItems(protocolsList);
        //Проверка на наличие устройства(режим добавить).
        if(device==null) {
            model.getSelectionModel().selectFirst();
            protocol.getSelectionModel().selectFirst();
            device=new ClassDevice();
            nameDev.setText("Устройство");
            return;
        }
        //Заполнение полей(режим редактировать).
        model.getSelectionModel().select(device.get_ModelName());
        protocol.getSelectionModel().select(device.get_Protocol());
        nameDev.setText(device.get_Name());
        latitude.setText(String.valueOf(device.get_Latitude()));
        longitude.setText(String.valueOf(device.get_Longitude()));
        elevation.setText(String.valueOf(device.get_Elevation()));
        ipAddress.setText(device.get_IPAddress());
        ipPort.setText(String.valueOf(device.get_IPPort()));
        address.setText(String.valueOf(device.get_Address()));
        txtSIM.setText(device.get_SIM());
        period.setText(String.valueOf(device.get_Period()));
        //Заполнение полей пикетов.
        if(!device.get_Picket().isEmpty()){
            var tempest=device.get_Picket();
            var arrest=tempest.split("\\+");
            picket_km.setText(arrest[0].substring(2));
            picket_m.setText(arrest[1]);
        }
    }

    /**
     * Открыть окно устройств.
     * @param device текущее устройство
     * @return true - окно открыто, false - ошибка открытия окна
     */
    public static boolean showWindow(ClassDevice device){

        Stage stage = new Stage();
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowDevice.fxml"));
        fxmlLoader.setControllerFactory(call->new WindowDevice(device));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            return false;
        }
        stage.setTitle("Устройство");
        stage.getIcons().add(new Image("/about.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
        return true;
    }

    @FXML
    public void btn_Click(ActionEvent actionEvent) {
        Button button=(Button) actionEvent.getSource();
        Window window= (((Button)actionEvent.getSource()).getScene()).getWindow();
        if(button.isCancelButton()){
            ((Stage)window).close();
            return;
        }
        //Добавить устройстсво.
        if(device.getId()==0){
            ClassDevice dev= setDevice();
            boolean res= MainWindow.DB.deviceAdd(dev);
            if(res){
                MainWindow.Devices.add(dev);
                TreeView<Pair<Integer, String>> treeView= MainWindow.mainWindow.getTreeView();
                var p=new Pair<Integer,String >(dev.getId(), dev.get_Name()){
                    @Override
                    public String toString(){
                        return getValue();
                    }
                };
                TreeItem<Pair<Integer, String>> node = new TreeItem<>(p);
                node.setGraphic(new ImageView("/hardware.png"));
                treeView.getRoot().getChildren().add(node);
                ClassMessage.showMessage("Устройство","Добавление устройства","Устройство добавлено!",
                        Alert.AlertType.INFORMATION);
            }
            else{
                ClassMessage.showMessage("Устройство","Добавление устройства","Ошибка! Устройство не добавлено!",
                        Alert.AlertType.ERROR);
            }
        }
        else{
           boolean res=MainWindow.DB.deviceEdit(setDevice());
           if(!res){
               ClassMessage.showMessage("Устройство","Изменение устройства","Ошибка! Устройство не изменено!",
                       Alert.AlertType.ERROR);
           }
        }
        ((Stage)window).close();
    }

    /**
     * Заполнение полей устройства.
     * @return
     */
    private ClassDevice setDevice(){
        if(device.getId()==0){
            device.setCountNumber(MainWindow.Devices.size()+1);
        }
        device.set_Name(nameDev.getText());
        if(!picket_km.getText().isEmpty() && !picket_m.getText().isEmpty()){
            device.set_Picket("ПК"+picket_km.getText()+"+"+picket_m.getText());
        }
        else{
            device.set_Picket("");
        }
        device.set_Latitude(latitude.getText().isEmpty() ? 0D:Double.parseDouble(latitude.getText()));
        device.set_Longitude(longitude.getText().isEmpty() ? 0D:Double.parseDouble(longitude.getText()));
        device.set_Elevation(elevation.getText().isEmpty() ? 0D:Double.parseDouble(elevation.getText()));
        device.set_Model((ClassDevice.EnumModel.values()[model.getSelectionModel().getSelectedIndex()]));
        device.set_Protocol((ClassDevice.EnumProtocol.values()[protocol.getSelectionModel().getSelectedIndex()]));
        device.set_IPAddress(ipAddress.getText());
        device.set_IPPort(ipPort.getText().isEmpty() ? 502 : Integer.parseInt(ipPort.getText()));
        device.set_Address(address.getText().isEmpty() ? 1 : Integer.parseInt(address.getText()));
        device.set_SIM(txtSIM.getText());
        device.set_Period(period.getText().isEmpty() ? 0 : Integer.parseInt(period.getText()));
        device.set_ComPort(String.valueOf(MainWindow.settings.getPortModbus()));
        return device;
    }
}
