package org.example.mmsd_al.UserControlsClasses;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import org.example.mmsd_al.DevicesClasses.ClassDevice;

import java.util.ArrayList;
import java.util.List;

public class TreeViewFactory {


    /**
     * Создает дерево устройств при первичном запуске программы.
     * @param collection коллекция устройств
     * @param nameRoot имя устройства и его индекс
     * @param <T>
     * @return
     */
    public  static <T>  TreeItem createRootTree(List<T> collection, Pair<Integer,String> nameRoot) {

        //Создаем корневой элемент дерева.
        TreeItem<Pair<Integer, String>> root = new TreeItem<>(nameRoot);
        //Добавляем иконку корневого элемента.
        root.setGraphic(new ImageView("/folder.png"));
        //Разворачиваем корневой элемент.
        root.setExpanded(true);
        //Если список устройств пуст, то заканчиваем посторение дерева.
        if(collection.isEmpty()) return root;
        //Построение подузлов дерева.
        if (collection.get(0) instanceof ClassDevice) {
            int i = 0;
            for (T item : collection) {
                if (item instanceof ClassDevice) {
                    //Формируем узел для каждого устройства.
                    Pair<Integer, String> el = new Pair<>(((ClassDevice) item).getId(), ((ClassDevice) item).get_Name()) {
                        @Override
                        public String toString() {
                            return this.getValue();
                        }
                    };
                    TreeItem<Pair<Integer, String>> node = new TreeItem<>(el);
                    //Добавляем иконку для подузла.
                    node.setGraphic(new ImageView("/hardware.png"));
                    //Добавляем подузел в дерево.
                    root.getChildren().add(node);
                }
            }
        }
        return root;
    }
}
