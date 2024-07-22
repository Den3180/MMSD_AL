package org.example.mmsd_al.UserControlsClasses;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.ServiceClasses.ClassDeleteDialog;
import org.example.mmsd_al.ServiceClasses.ClassMessage;
import org.example.mmsd_al.Windows.WindowChannel;
import org.example.mmsd_al.Windows.WindowDevice;

public class ContextMenuFactory {

    public static ContextMenu ContextMenuDevice(TableView tableView, Object obj){

        ContextMenu contextMenu=new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Добавить...");
        MenuItem menuItem2 = new MenuItem("Редактировать...");
        MenuItem menuItem3 = new MenuItem("Удалить...");
        menuItem1.setOnAction(event ->{
            if(obj instanceof ClassDevice){
                WindowDevice.showWindow(null);
            } else if (obj instanceof ClassChannel) {
                WindowChannel.showWindow(null);
            }
        });
        menuItem2.setOnAction(event -> {
            TableViewSelectionModel selectedElems=tableView.getSelectionModel();
            if(selectedElems.isEmpty()) {
                ClassMessage.showMessage("Устройство","","Устройство не выбрано!", Alert.AlertType.ERROR);
                return;
            }
            if(obj instanceof ClassDevice){
                var dev=(ClassDevice)selectedElems.getSelectedItems().get(0);
                WindowDevice.showWindow(dev);
            } else if (obj instanceof ClassChannel) {
                var channel=(ClassChannel) selectedElems.getSelectedItems().get(0);
                WindowChannel.showWindow(channel);
            }
        });
        menuItem3.setOnAction(event -> {
            var selectedElems=tableView.getSelectionModel();
            if(selectedElems.isEmpty()) {
                ClassMessage.showMessage("Устройство","","Устройство не выбрано!", Alert.AlertType.CONFIRMATION);
                return;
            }
            ClassDeleteDialog.deleteObj(selectedElems.getSelectedItem());
        });
        contextMenu.getItems().setAll(menuItem1,menuItem2,menuItem3);
        return contextMenu;
    }
}
