package org.example.mmsd_al.UserControlsClasses;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.DevicesClasses.ClassDevice;
import org.example.mmsd_al.MainWindow;



public class UserControlsFactory {

    public static final String [] HEADERS_DEVICE =new String[]{
            "№", "Устройство", "Модель","Пикет", "С.Ш.", "В.Д",
            "Высота", "Протокол", "Порт/IP","Адрес/ID", "SIM карта", "Период опроса",
            "Пакетов", "Наличие связи"
    };

    public static final String[] VARIABLES_DEVICE =new String[]{
            "countNumber", "_Name",  "_ModelName","_Picket", "_Latitude", "_Longitude",
            "_Elevation", "_ProtocolName", "_ComPort","_Address", "_SIM", "_Period",
            "_PacketStatistics", "_LinkStateName"
    };

    public static final String[] HEADES_CHANNEL=new String[]{
            "№", "Наименование", "Устройство","Тип регистра", "Адрес", "Адр.(hex)",
            "Формат", "Данные", "Коэф.","Знаков", "Min", "Max",
            "Значение", "Актуальность"//,"Архив","Шлюз"
    };

    public static  final String [] VARIABLES_CHANNEL=new String[]{
            "_CountNumber", "_Name", "_DeviceName","_TypeRegistryFullName", "_Address", "_AddressHex",
            "_Format", "_StrBaseValue", "_Koef","_Accuracy","_Min","_Max",
            "_Value","_StrDTAct"//,"_Archive","_Ext"
    };

    /**
     * Создать таблицу.
     * @param list источник данных
     * @param headers заголовки таблицы
     * @param variables переменные класса
     * @param obj образец класса
     * @return TableView
     * @param <T>
     */
    public static  <T> TableView<T> createTable(ObservableList<T> list, String[]headers, String[]variables, T obj){
        if(list.stream().count()==0) return new TableView<>();
        TableView<T> tableView=new TableView<>(list);
        //Установка стилей.
        tableView.getStylesheets().add("style.css");
        //Настройка заголовков столбцов, ширины столбцов.
        for(int i=0;i<headers.length;i++){
            try {
                var curField=obj.getClass().getDeclaredField(variables[i]);
                curField.setAccessible(true);
                var nv=curField.get(obj);
                tableView.getColumns().add((TableColumn<T, ?>) setColTable(obj,nv,headers[i],variables[i]));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        tableView.setTableMenuButtonVisible(true);
        ObservableList<T> rr= tableView.getItems();
        if(obj instanceof ClassChannel){
            int i=0;
            for (var el:rr){
                ((ClassChannel)el).set_CountNumber(++i);
            }
        }

        tableView.setRowFactory(userTable->{
            PseudoClass up = PseudoClass.getPseudoClass("up");
            PseudoClass down = PseudoClass.getPseudoClass("down");
            TableRow<T> row = new TableRow<>();
            if(obj instanceof ClassDevice){
                ChangeListener<String> changeListener = (obs, oldPrice, newPrice) -> {

                    row.pseudoClassStateChanged(up, newPrice.equals("На связи"));
                    row.pseudoClassStateChanged(down, newPrice.equals("Нет связи")
                    //row.pseudoClassStateChanged(up, oldPrice.equals("На связи"));
                    //row.pseudoClassStateChanged(down, oldPrice.equals("Нет связи")

                    );
                };
                row.itemProperty().addListener((obs, previousUser, currentUser) -> {

                    if (previousUser != null) {
                        ((ClassDevice)previousUser)._LinkStateNameProperty().removeListener(changeListener);
                    }
                    if (currentUser != null) {
                        ((ClassDevice)currentUser)._LinkStateNameProperty().addListener(changeListener);
                        row.pseudoClassStateChanged(up, ((ClassDevice)currentUser)._LinkStateNameProperty().get().equals("На связи"));
                        row.pseudoClassStateChanged(down, ((ClassDevice)currentUser)._LinkStateNameProperty().get().equals("Нет связи"));
//                        var temp=((ClassDevice) currentUser).get_LinkStateName();
//                        ClassDevice us=MainWindow.Devices.filtered (el->el.getId()==((ClassDevice)currentUser).getId()).get(0);
//                        us.set_LinkStateName("");
//                        us.set_LinkStateName(temp);
                    }
                    else {
                        row.pseudoClassStateChanged(up, false);
                        row.pseudoClassStateChanged(down, false);
                    }
                });

            }
            //row.setContextMenu(ContextMenuFactory.ContextMenuDevice(obj));
            return row;
        });

        tableView.setContextMenu(ContextMenuFactory.ContextMenuDevice(tableView));
        return tableView;
    }

    /**
     * Настройка колонок и ячеек таблицы.
     * @param obj1
     * @param obj2
     * @param hdr
     * @param vrb
     * @return
     * @param <T>
     * @param <S>
     */
    private static <T,S> TableColumn<T,S> setColTable(T obj1, S obj2, String hdr, String vrb){
        TableColumn<T,S> col = new TableColumn<T,S>(hdr);

        col.setMinWidth(20);
        if(HEADERS_DEVICE[1].equals(hdr) && obj1 instanceof ClassDevice ||HEADES_CHANNEL[1].equals(hdr)){
            col.setPrefWidth(300);
            col.setMaxWidth(800);
        }
        col.setCellValueFactory(new PropertyValueFactory<T,S>(vrb));


//        col.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<S>() {
//            @Override
//            public String toString(S object) {
//                return object.toString();
//            }
//
//            @Override
//            public S fromString(String string) {
//                return (S)string;
//            }
//        }));
//        col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<T,S>>() {
//            @Override
//            public void handle(TableColumn.CellEditEvent event) {
//
//            }
//        });
        return col;
    }
}
