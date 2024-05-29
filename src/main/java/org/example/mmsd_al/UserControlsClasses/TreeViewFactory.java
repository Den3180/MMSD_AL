package org.example.mmsd_al.UserControlsClasses;

import javafx.scene.control.TreeItem;

import java.util.List;

public class TreeViewFactory {
    public  static <T>  TreeItem createRootTree(List<T> collection, String nameRoot){
        TreeItem<String> root=new TreeItem<>(nameRoot);
        root.setExpanded(true);
        for(var item:collection){
            TreeItem<String> node=new TreeItem<>(item.toString());
            root.getChildren().add(node);
        }
        return root;
    }
}
