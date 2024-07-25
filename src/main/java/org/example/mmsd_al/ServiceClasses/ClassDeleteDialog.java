package org.example.mmsd_al.ServiceClasses;

import com.fasterxml.jackson.dataformat.xml.XmlAnnotationIntrospector;
import javafx.scene.control.*;
import javafx.util.Pair;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.MainWindow;

import java.util.Optional;

public class ClassDeleteDialog {

    public static boolean deleteObj(Object obj){
        return switch (obj.getClass().getSimpleName()){
            case "ClassDevice" ->deleteDevice((ClassDevice) obj);
            case "ClassChannel" ->deleteChannel((ClassChannel) obj);
            default -> false;
        };
    }

    /**
     * Удаление устройства.
     * @param device устройство
     */
    private static boolean deleteDevice(ClassDevice device){

        ButtonType res= ClassMessage.showMessage("Устройство","Удаление",
                "Удалить устройство: "+device.get_Name()+"?", Alert.AlertType.CONFIRMATION);
        if(res.getButtonData()== ButtonBar.ButtonData.YES){
            if(MainWindow.DB.deviceDel(device)){
                MainWindow.Devices.remove(device);
                TreeView<Pair<Integer,String>> treeView=MainWindow.mainWindow.getTreeView();
                Optional<TreeItem<Pair<Integer, String>>> trItem=treeView.getRoot().getChildren().stream()
                        .filter(el->el.getValue().getKey()==device.getId()).findFirst();
                treeView.getRoot().getChildren().remove(trItem.stream().toList().get(0));

                ClassMessage.showMessage("Устройство","Удаление",
                        "Устройство: "+device.get_Name()+" удалено.", Alert.AlertType.INFORMATION);
            }
            else {
                ClassMessage.showMessage("Устройство","Удаление",
                        "Ошибка! Устройство: "+device.get_Name()+"не удалено.", Alert.AlertType.ERROR);
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Удаление канала.
     * @param channel - канал
     */
    private static boolean deleteChannel(ClassChannel channel){
        ButtonType res= ClassMessage.showMessage("Устройство","Удаление",
                "Удалить канал: "+channel.get_Name()+"?", Alert.AlertType.CONFIRMATION);
        if(res.getButtonData().equals(ButtonBar.ButtonData.YES)){
            if(MainWindow.DB.registryDel(channel)) {
                MainWindow.Channels.remove(channel);
                ClassMessage.showMessage("Канал","Удаление",
                        "Канал: "+channel.get_Name()+" удален.", Alert.AlertType.INFORMATION);
            }
            else{
                ClassMessage.showMessage("Канал","Удаление",
                        "Ошибка! Канал: "+channel.get_Name()+"не удален.", Alert.AlertType.ERROR);
                return false;
            }
            return true;
        }
        return false;
    }
}
