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
        if(file==null) return file;
        if(file.getAbsolutePath().contains(".")) return file;
        String selectedExt = fileChooser.getSelectedExtensionFilter().getExtensions().get(0);
        String path=file.getAbsolutePath()+selectedExt;
        return new File(path);
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
        var hh= fileChooser.getSelectedExtensionFilter().getExtensions().get(0);
        if(file==null) return file;
        if(file.getAbsolutePath().contains(".")) return file;
        String selectedExt = fileChooser.getSelectedExtensionFilter().getExtensions().get(0).split("/*")[0];
        if(selectedExt.startsWith("*"))
            selectedExt=selectedExt.substring(1);
        String path=file.getAbsolutePath()+selectedExt;
        return new File(path);
    }
}
