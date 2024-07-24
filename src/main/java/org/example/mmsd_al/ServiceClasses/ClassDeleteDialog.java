package org.example.mmsd_al.ServiceClasses;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.MainWindow;

public class ClassDeleteDialog {

    private ClassDevice device;

    public ClassDeleteDialog(Object obj){
        deleteObj(obj);
    }

    public static void deleteObj(Object obj){
        switch (obj.getClass().getSimpleName()){
            case "ClassDevice" ->deleteDevice((ClassDevice) obj);
            case "ClassChannel" ->deleteChannel((ClassChannel) obj);
        }
    }

    /**
     * Удаление устройства.
     * @param device устройство
     */
    private static void deleteDevice(ClassDevice device){

        ButtonType res= ClassMessage.showMessage("Устройство","Удаление",
                "Удалить устройство: "+device.get_Name()+"?", Alert.AlertType.CONFIRMATION);
        if(res.getButtonData()== ButtonBar.ButtonData.YES){
            if(MainWindow.DB.deviceDel(device)){
                MainWindow.Devices.remove(device);
                ClassMessage.showMessage("Устройство","Удаление",
                        "Устройство: "+device.get_Name()+" удалено.", Alert.AlertType.INFORMATION);
            }
            else {
                ClassMessage.showMessage("Устройство","Удаление",
                        "Ошибка! Устройство: "+device.get_Name()+"не удалено.", Alert.AlertType.ERROR);
            }
        }
    }

    private static void deleteChannel(ClassChannel channel){
          MainWindow.Channels.remove(channel);
          ClassDevice dev=channel.get_Device();
          dev.getChannels().remove(channel);
    }
}
