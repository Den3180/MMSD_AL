package org.example.mmsd_al.UserControlsClasses;


import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

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

    /**
     * Создать таблицу.
     * @param list источник данных
     * @param headers заголовки таблицы
     * @param variables переменные класса
     * @param obj образец сласса
     * @return TableView
     * @param <T>
     */
    public static  <T> TableView<T> createTable(ObservableList<T> list, String[]headers, String[]variables, T obj){
        TableView<T> tableView=new TableView<>(list);
        //tableView.setEditable(true);
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
        col.setCellValueFactory(new PropertyValueFactory<T,S>(vrb));
        col.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<S>() {
            @Override
            public String toString(S object) {
                return object.toString();
            }

            @Override
            public S fromString(String string) {
                return (S)string;
            }
        }));
        col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<T,S>>() {
            @Override
            public void handle(TableColumn.CellEditEvent event) {

            }
        });
        return col;
    }
}