package org.example.mmsd_al.ServiceClasses;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class ClassDialog {
    /**
     * Диалог сохранения в файл.
     * @param stage
     * @return
     */
    public static File openDialog(Stage stage){

        File file;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открыть");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml","*.XML"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt","*.TXT"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        file=fileChooser.showOpenDialog(stage);
//        if(file!=null) return file;
//        String path=file.getAbsolutePath();
//        StringBuilder sb=new StringBuilder(path);
//
//        if(path.endsWith("*.xml"|""))
//        if(file.getex)
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
                new FileChooser.ExtensionFilter("XML Files", "*.xml","*.XML"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt","*.TXT"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        return fileChooser.showSaveDialog(stage);
    }
}
