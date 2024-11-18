package org.example.mmsd_al.Windows;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import jssc.SerialPortList;
import org.example.mmsd_al.Archive.ClassDeviceArchive;
import org.example.mmsd_al.Classes.ClassModbus;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.MainWindow;
import org.example.mmsd_al.ServiceClasses.ClassDelay;
import org.example.mmsd_al.StartApplication;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class WindowImportArchive  {

    private Stage stage;
    private Scene scene;
    private int numRecords;
    private ClassDeviceArchive deviceArchive;
    private ObservableList<ClassDevice> deviceWithArchive;
    private int addressRegAO = 133;
    private int addressRegAI = 53;
    private int endPos;
    private int deviceAddress;
    private ClassModbus modbus;
    private ClassDevice device;
    private int [] dataNoteCount=null;

    private SimpleIntegerProperty intprop=new SimpleIntegerProperty(0);

    @FXML
    public Button apply;
    @FXML
    private ComboBox devArchiveComboBox;
    @FXML
    public TextField availableRecords;
    @FXML
    private TextField countLoadRecords;
    @FXML
    private TextField startRecords;



    public WindowImportArchive(ClassModbus modbus){
        //Ссылка на существующий объект модбас.
        this.modbus = modbus;
        //Список устройств, которые имеют архив.
        deviceWithArchive= FXCollections.observableArrayList(MainWindow.Devices.filtered(
                dev->dev.get_Model().equals(ClassDevice.EnumModel.BKM_5)));
        //Создание объекта для работы с архивом(скачивание, передача и т.д.)
        deviceArchive = new ClassDeviceArchive(this.modbus);
        //Начальное значение конечной отметки.
        endPos=0;
    }


    @FXML
    public void initialize(){
        //Связываем список устройств с комбобоксом.
        devArchiveComboBox.setItems(deviceWithArchive);
        //Стартовый элемент нулевой.
        devArchiveComboBox.getSelectionModel().select(0);
        //Ссылку на выбранное устройство делаем.
        device=(ClassDevice) devArchiveComboBox.getSelectionModel().getSelectedItem();
        //Адрес устройства фиксируем.
        deviceAddress=device.get_Address();
        //Получаем сведения о количестве записей в архиве.
        int [] dataNoteCount=deviceArchive.GetCountNoteArchive(deviceAddress,addressRegAO);
        //Заполняем поле доступных записей.
        availableRecords.setText(String.valueOf(dataNoteCount[0]));
        //Если поле доступных данных заполнено.
        if(availableRecords.getText()!="нет данных"){
            //Поле для указание количества записей для скачивания.
            countLoadRecords.setText("0");
            //Поле для указания ноера начальной записи.
            startRecords.setText("0");
        }
        //Если количество доступных записей 0, то поля делаются не доступны.
        if(Objects.equals(availableRecords.getText(), "0")) {
            apply.setDisable(true);
            countLoadRecords.setDisable(true);
            startRecords.setDisable(true);
        }
    }

    public static boolean showWindow(ClassModbus modbus) {
        Stage stage = new Stage();
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("WindowImportArchive.fxml"));
        fxmlLoader.setControllerFactory(call->new WindowImportArchive(modbus));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            return false;
        }
        //Настройка действий при закрытии окна.
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //Установка настроек соединения.
                var currentPort=MainWindow.settings.getPortModbus();
                modbus.getPortParametres().setDevice(SerialPortList.getPortNames()[currentPort]);
            }
        });
        stage.setTitle("Загрузка архива");
        stage.getIcons().add(new Image("/about.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
        return true;
    }

    @FXML
    public void click_Button(ActionEvent actionEvent) {
        //Получить источник события.
        Button button=(Button) actionEvent.getSource();
        //Получить главное окно.
        Window window= (button.getScene()).getWindow();
        //Закрыть главное окно.
        ((Stage)window).close();
        //Если кнопка, которая нажата это не кнопка отмены.
        if(!button.isCancelButton()){
            //Получения данных архива устройства в отдельном потоке.
            Thread thread=new Thread(this::getDeviceArchive);
            //Делаем поток демоном.
            thread.setDaemon(true);
            //Запускаем поток.
            thread.start();
        }
        else {
            //Получаем номер текущего порта.
            var currentPort=MainWindow.settings.getPortModbus();
            //Устанавливаем настройки соединения.
            if(currentPort<SerialPortList.getPortNames().length) {
                modbus.getPortParametres().setDevice(SerialPortList.getPortNames()[currentPort]);
            }
        }
    }

    /**
     * Получить данные архива устройства.
     */
    private void getDeviceArchive(){

        //TODO Добавить блок try-catch. Выход за границы массива происходит.
        int startPos= Integer.parseInt(startRecords.getText());
        numRecords=Integer.valueOf(countLoadRecords.getText());
        int endpos=startPos+numRecords;
        //Создание и запуск окна прогресса загрузки архива.
        WindowProcess30 windowProcess30=new WindowProcess30(this);
        Platform.runLater(()->windowProcess30.showWindow(windowProcess30));
        AtomicInteger finalStartPos = new AtomicInteger();
        while(startPos<endpos){
            deviceArchive.readArchive_30(deviceAddress,startPos);
            Platform.runLater(()->setIntprop(finalStartPos.get()));
            finalStartPos.getAndIncrement();
            startPos++;
        }

        startPos=Integer.parseInt(startRecords.getText());
        int countNotCurr=0;
        var note_30=deviceArchive.getNote_30();
        while(startPos<endpos){
            int [] noteHeader=note_30.get(countNotCurr);
            countNotCurr++;
            int sizeBlockNote = noteHeader[4] * 100 + noteHeader[5];
            int numBlocks = sizeBlockNote % 200 == 0 ? sizeBlockNote / 200 : sizeBlockNote / 200 + 1;
            int countBlock = 0;
            while (countBlock < numBlocks){
                deviceArchive.readArchive_31(deviceAddress,startPos,countBlock);
                countBlock++;
            }
            Platform.runLater(()->setIntprop(finalStartPos.get()));
            finalStartPos.getAndIncrement();
            startPos++;
        }
            ClassDelay.delay(1000);
        Platform.runLater(()->windowProcess30.getStage().close());
        deviceArchive.closeSerialPort();
        deviceArchive.processArchive(device);
        var currentPort=MainWindow.settings.getPortModbus();
        modbus.getPortParametres().setDevice(SerialPortList.getPortNames()[currentPort]);
    }

    public int getIntprop() {
        return intprop.get();
    }

    public SimpleIntegerProperty intpropProperty() {
        return intprop;
    }

    public void setIntprop(int intprop) {
        this.intprop.set(intprop);
    }

    public int getNumRecords() {
        return numRecords;
    }

    @FXML
    public void ChangeItemCombobox(ActionEvent actionEvent) {
        device=(ClassDevice) devArchiveComboBox.getSelectionModel().getSelectedItem();
        deviceAddress=device.get_Address();
        int [] dataNoteCount=deviceArchive.GetCountNoteArchive(deviceAddress,addressRegAO);
        availableRecords.setText(String.valueOf(dataNoteCount[0]));
        //Если поле доступных данных заполнено.
        if(availableRecords.getText()!="нет данных"){
            //Поле для указание количества записей для скачивания.
            countLoadRecords.setText("0");
            //Поле для указания ноера начальной записи.
            startRecords.setText("0");
        }
        //Если количество доступных записей 0, то поля делаются не доступны.
        if(Objects.equals(availableRecords.getText(), "0")) {
            apply.setDisable(true);
            countLoadRecords.setDisable(true);
            startRecords.setDisable(true);
        }
        else{
            apply.setDisable(false);
            countLoadRecords.setDisable(false);
            startRecords.setDisable(false);
        }
    }
}
