package org.example.mmsd_al.ServiceClasses;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

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
                new FileChooser.ExtensionFilter("XML Files", "*.xml"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        return fileChooser.showOpenDialog(stage);
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
                new FileChooser.ExtensionFilter("XML Files", "*.xml"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        return fileChooser.showSaveDialog(stage);
    }
}
