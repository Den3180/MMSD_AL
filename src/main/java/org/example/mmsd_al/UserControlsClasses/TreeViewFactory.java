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

    public  static <T>  TreeItem createRootTree(List<T> collection, Pair<Integer,String> nameRoot) {

            TreeItem<Pair<Integer, String>> root = new TreeItem<>(nameRoot);
            if (collection.get(0) instanceof ClassDevice) {
            root.setGraphic(new ImageView("/folder.png"));
            root.setExpanded(true);
            int i = 0;
            for (T item : collection) {
                if (item instanceof ClassDevice) {
                    Pair<Integer, String> el = new Pair<>(((ClassDevice) item).getId(), ((ClassDevice) item).get_Name()) {
                        @Override
                        public String toString() {
                            return this.getValue();
                        }
                    };
                    TreeItem<Pair<Integer, String>> node = new TreeItem<>(el);
                    node.setGraphic(new ImageView("/hardware.png"));
                    root.getChildren().add(node);
                }
            }
        }
        return root;
    }
}
