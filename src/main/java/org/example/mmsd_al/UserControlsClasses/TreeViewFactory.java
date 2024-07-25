package org.example.mmsd_al.UserControlsClasses;

import javafx.scene.control.TreeItem;
import javafx.util.Pair;
import org.example.mmsd_al.DevicesClasses.ClassDevice;

import java.util.List;

public class TreeViewFactory {
    public  static <T>  TreeItem createRootTree(List<T> collection, Pair<Integer,String> nameRoot){
        TreeItem<Pair<Integer,String> > root=new TreeItem<>(nameRoot);
        root.setExpanded(true);
        for(T item:collection){
            if(item instanceof ClassDevice){
                Pair<Integer,String> el=new Pair<>(((ClassDevice) item).getId(),((ClassDevice)item).get_Name()){
                    @Override
                    public String toString(){
                        return this.getValue();
                    }
                };
                TreeItem<Pair<Integer,String>> node=new TreeItem<>(el);
                root.getChildren().add(node);
            }
        }
        return root;
    }
}
