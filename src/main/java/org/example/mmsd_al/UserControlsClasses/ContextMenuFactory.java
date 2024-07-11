package org.example.mmsd_al.UserControlsClasses;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.Windows.WindowDevice;

import java.util.Collection;

public class ContextMenuFactory {

    public static ContextMenu ContextMenuDevice(TableView tableView){

        ContextMenu contextMenu=new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Choice 1");
        MenuItem menuItem2 = new MenuItem("Choice 2");
        MenuItem menuItem3 = new MenuItem("Choice 3");
        contextMenu.getItems().setAll(menuItem1,menuItem2,menuItem3);
        contextMenu.setOnAction(event -> {
            var selectedElems=tableView.getSelectionModel();
            if(selectedElems.isEmpty()) return;
           var elem=selectedElems.getSelectedItem();
           if(elem instanceof ClassDevice){
               WindowDevice.showWindow();
           }
           else if(elem instanceof ClassChannel){
            //TODO Окно настроек регистров.
           }
        });
        return contextMenu;
    }
}
