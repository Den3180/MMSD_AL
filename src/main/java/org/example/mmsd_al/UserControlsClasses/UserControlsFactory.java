package org.example.mmsd_al.UserControlsClasses;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.DevicesClasses.ClassDevice;

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
     * @param obj образец сласса
     * @return TableView
     * @param <T>
     */
    public static  <T> TableView<T> createTable(ObservableList<T> list, String[]headers, String[]variables, T obj){
        if(list.stream().count()==0) return new TableView<>();
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
        ObservableList<T> rr= tableView.getItems();
        if(obj instanceof ClassChannel){
            int i=0;
            for (var el:rr){
                ((ClassChannel)el).set_CountNumber(++i);
            }
        }

        Callback<TableView<T>, TableRow<T>> rowFactory=new Callback<TableView<T>, TableRow<T>>() {
            @Override
            public TableRow<T> call(TableView<T> param) {

                    TableRow<T> row= new TableRow<T>(){
                    @Override
                    protected void updateItem(T user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user == null || empty) {
                            setStyle("");
                        } else {
                            if(user instanceof ClassDevice){
                                if (((ClassDevice)user).get_Name().equals("WWWW")) {
                                    setStyle("-fx-background-color: green;");
                                }
                                else {
                                    setStyle("");
                                }
                            }
                        }
                    }
                };
//                    row.pseudoClassStateChanged(PseudoClass.getPseudoClass("highlighted"),
//                            ((ClassDevice)list.get(0)).get_Address()==1);
                    return row;
            }
        };

        tableView.setRowFactory(rowFactory);
        //tableView.setStyle("-fx-selection-bar: green; -fx-selection-bar-non-focused: salmon;");
        //tableView.setStyle("-fx-background-color: #93f9b911;-fx-text-background-color: red;");
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
