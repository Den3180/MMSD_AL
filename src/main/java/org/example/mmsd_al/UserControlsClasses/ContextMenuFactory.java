package org.example.mmsd_al.UserControlsClasses;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.ServiceClasses.ClassMessage;
import org.example.mmsd_al.Windows.WindowDevice;

public class ContextMenuFactory {

    public static ContextMenu ContextMenuDevice(TableView tableView, Object obj){

        ContextMenu contextMenu=new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Добавить...");
        MenuItem menuItem2 = new MenuItem("Редактировать...");
        MenuItem menuItem3 = new MenuItem("Удалить...");
        menuItem1.setOnAction(event ->{
            if(obj instanceof ClassDevice){
                WindowDevice.showWindow();
            } else if (obj instanceof ClassChannel) {
                ClassMessage.showMessage("Канал","","Канал не выбран!", Alert.AlertType.CONFIRMATION);
            }
        });
        menuItem2.setOnAction(event -> {
            var selectedElems=tableView.getSelectionModel();
            if(selectedElems.isEmpty()) {
                ClassMessage.showMessage("Устройство","","Устройство не выбрано!", Alert.AlertType.CONFIRMATION);
                return;
            }
            if(obj instanceof ClassDevice){
                WindowDevice.showWindow();
            } else if (obj instanceof ClassChannel) {
                ClassMessage.showMessage("Канал","","Канал не выбран!", Alert.AlertType.CONFIRMATION);
            }
        });
        menuItem3.setOnAction(event -> {
            var selectedElems=tableView.getSelectionModel();
            if(selectedElems.isEmpty()) {
                ClassMessage.showMessage("Устройство","","Устройство не выбрано!", Alert.AlertType.CONFIRMATION);
                return;
            }
            if(obj instanceof ClassDevice){
                WindowDevice.showWindow();
            } else if (obj instanceof ClassChannel) {
                ClassMessage.showMessage("Канал","","Канал не выбран!", Alert.AlertType.CONFIRMATION);
            }
        });
        contextMenu.getItems().setAll(menuItem1,menuItem2,menuItem3);
        return contextMenu;
    }
}
