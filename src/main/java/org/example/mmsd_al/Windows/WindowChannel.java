package org.example.mmsd_al.Windows;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.MainWindow;
import org.example.mmsd_al.StartApplication;

import java.io.IOException;

public class WindowChannel {

    @FXML
    private CheckBox chMax;
    @FXML
    private CheckBox chMin;
    @FXML
    private CheckBox chArchive;
    @FXML
    private TextField ext;
    @FXML
    private TextField accuracy;
    @FXML
    private TextField min;
    @FXML
    private TextField max;
    @FXML
    private TextField koef;
    @FXML
    private ComboBox<ClassChannel.EnumFormat> format;
    @FXML
    private TextField address;
    @FXML
    private ComboBox<ClassChannel.EnumTypeRegistry> regType;
    @FXML
    private ComboBox <ClassDevice> listDevice;
    @FXML
    private TextField channelName;

    private ClassChannel channel;
    private ObservableList<ClassDevice> devices;
    private ObservableList<ClassChannel.EnumTypeRegistry> typeRegistries;
    private  ObservableList<ClassChannel.EnumFormat> formats;

    public WindowChannel(ClassChannel channel){
        this.channel=channel;
        devices= FXCollections.observableArrayList(MainWindow.Devices);
        typeRegistries=FXCollections.observableArrayList(ClassChannel.EnumTypeRegistry.values());
        formats=FXCollections.observableArrayList(ClassChannel.EnumFormat.values());
    }

    public void initialize(){

        //заполнение комбобоксов.
        listDevice.setItems(devices);
        regType.setItems(typeRegistries);
        format.setItems(formats);
        //Проверка на наличие канала(режим добавить).
        if(channel==null){
            listDevice.getSelectionModel().selectFirst();
            regType.getSelectionModel().selectFirst();
            format.getSelectionModel().selectFirst();
            min.setDisable(true);
            max.setDisable(true);
            channel=new ClassChannel();
            return;
        }
        //Заполнение окна в режиме редактировать.
        listDevice.getSelectionModel().select(channel.get_Device());
        regType.getSelectionModel().select(channel.get_TypeRegistry());
        format.getSelectionModel().select(channel.get_Format());
        channelName.setText(channel.get_Name());
        address.setText(String.valueOf(channel.get_Address()));
        koef.setText(String.valueOf(channel.get_Koef()));

        if(((Double)channel.get_Max()).isNaN()){
            max.setText("");
            chMax.setSelected(false);
            max.setDisable(true);
        }
        else {
            max.setText(String.valueOf(channel.get_Max()));
            chMax.setSelected(true);
            max.setDisable(false);
        }

        if(((Double)channel.get_Min()).isNaN()){
            min.setText("");
            chMin.setSelected(false);
            min.setDisable(true);
        }
        else{
            min.setText(String.valueOf(channel.get_Min()));
            chMin.setSelected(true);
            min.setDisable(false);
        }
        accuracy.setText(channel.get_Accuracy()==Integer.MAX_VALUE ? "" : String.valueOf(channel.get_Accuracy()));
        ext.setText(channel.get_Ext()==Integer.MAX_VALUE ? "" : String.valueOf(channel.get_Ext()));
        chArchive.setSelected(channel.is_Archive());
    }

    /**
     * Открыть окно устройств.
     * @param channel текущее устройство
     * @return true - окно открыто, false - ошибка открытия окна
     */
    public static boolean showWindow(ClassChannel channel){

        Stage stage = new Stage();
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowChannel.fxml"));
        fxmlLoader.setControllerFactory(call->new WindowChannel(channel));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            return false;
        }
        stage.setTitle("Канал");
        stage.getIcons().add(new Image("/about.png"));
        stage.initModality(Modality.WINDOW_MODAL);
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
        ClassChannel ch= fillInTheChannelFields();
        int y=0;
        //Добавить регистр.
        if(channel.getId()==0){

        }
        //Редактировать регистр.
        else{

        }
        ((Stage)window).close();
    }

    private ClassChannel fillInTheChannelFields(){
        ClassChannel channel=new ClassChannel();
        channel.setId(this.channel.getId());
        channel.set_DTAct(this.channel.get_DTAct());
        channel.set_Name(channelName.getText().isEmpty() ? "Канал" : channelName.getText());
        channel.set_Device(this.channel.get_Device());
        channel.set_TypeRegistry(this.channel.get_TypeRegistry());
        channel.set_Address(address.getText().isEmpty() ? 0 : Integer.parseInt(address.getText()));
        channel.set_Koef(koef.getText().isEmpty() ? 1 : Float.parseFloat(koef.getText()));
        channel.set_Max(max.getText().isEmpty() ? 1 : Double.parseDouble(max.getText()));
        channel.set_Max(min.getText().isEmpty() ? 1 : Double.parseDouble(min.getText()));
        channel.set_Accuracy(accuracy.getText().isEmpty() ? 1 : Integer.parseInt(accuracy.getText()));
        channel.set_Ext(ext.getText().isEmpty() ? 0 : Integer.parseInt(ext.getText()));
        channel.set_Archive(chArchive.isSelected());
        return channel;
    }

    //Нажать чекбокс.
    @FXML
    public void chBox_Clicked(ActionEvent actionEvent) {

        CheckBox checkBox=(CheckBox) actionEvent.getSource();
        if(checkBox.getId()=="1"){

        }
        else{

        }
    }
}
