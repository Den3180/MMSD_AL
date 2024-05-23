package org.example.mmsd_al.ServiceClasses;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Properties;

public class ClassDialog {
    /**
     * Диалог сохранения в файл.
     * @param stage
     * @return
     */
    public static File openDialog(Stage stage){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открыть");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files(*.xml)", "*.xml"),
                new FileChooser.ExtensionFilter("Text Files(*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file=fileChooser.showOpenDialog(stage);
        if(file==null || file.getAbsolutePath().contains(".")) return file;
        StringBuilder sbExt = new StringBuilder(fileChooser.getSelectedExtensionFilter().getExtensions().get(0));
        if(sbExt.indexOf("*")!=-1){
            return new File(file.getAbsolutePath()+sbExt.substring(1));
        }
        return file;
    }

    /**
     * Диалог загрузки из файла.
     * @param stage
     * @return
     */
    public static File saveDialog(Stage stage){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files(*.xml)", "*.xml"),
                new FileChooser.ExtensionFilter("Text Files(*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file=fileChooser.showSaveDialog(stage);

        if(file==null || file.getAbsolutePath().contains(".")) return file;
        StringBuilder sbExt = new StringBuilder(fileChooser.getSelectedExtensionFilter().getExtensions().get(0));
        if(sbExt.indexOf("*")!=-1){
            return new File(file.getAbsolutePath()+sbExt.substring(1));
        }
        return file;
    }
}
