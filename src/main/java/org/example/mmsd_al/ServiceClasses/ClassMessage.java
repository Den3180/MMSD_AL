package org.example.mmsd_al.ServiceClasses;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class ClassMessage {

    public static ButtonType showMessage (String title, String header, String text, Alert.AlertType type){

        Alert alert=new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.getButtonTypes().setAll(chooseTypeMessage(type));
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
        return alert.getResult();
    }

    private static List<ButtonType> chooseTypeMessage(Alert.AlertType type){

        List<ButtonType> buttonTypeList=new ArrayList<>();
        switch (type){
            case CONFIRMATION :
                     buttonTypeList.add(new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
                     buttonTypeList.add(new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE));
              break;
            case WARNING:
            case ERROR:
            case INFORMATION:
            case NONE:
                buttonTypeList.add(new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
                break;
        }
        return buttonTypeList;
    }
}
