package org.example.mmsd_al.UserControlsClasses;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.mmsd_al.Classes.ClassChannel;
import org.example.mmsd_al.DevicesClasses.ClassDevice;

import java.time.LocalTime;

public class UserControlsFactory {

    
    //Индекс смещения при скроллинге.     
    private static int indexScroll=0;
    //Предыдущая позиция мыши.    
    private static double prevY=0D;
    //Длительность нажатия кнопки/пальцем. 
    private static int secondsOfPressedMouse =0;
    //
    private static int indexScrollPrev =0;

    /**
     * Заголовки для таблицы устройств.
     */
    public static final String [] HEADERS_DEVICE =new String[]{
            "№", "Устройство", "Модель","Пикет", "С.Ш.", "В.Д",
            "Высота", "Протокол", "Порт/IP","Адрес/ID", "SIM карта", "Период опроса",
            "Пакетов", "Наличие связи"
    };

    /**
     * Название переменных для таблицы устройств.
     */
    public static final String[] VARIABLES_DEVICE =new String[]{
            "countNumber", "_Name",  "_ModelName","_Picket", "_Latitude", "_Longitude",
            "_Elevation", "_ProtocolName", "_ComPort","_Address", "_SIM", "_Period",
            "_PacketStatistics", "_LinkStateName"
    };

    /**
     * Заголовки для таблицы регистров.
     */
    public static final String[] HEADES_CHANNEL=new String[]{
            "№", "Наименование", "Устройство","Тип регистра", "Адрес", "Адр.(hex)",
            "Формат", "Данные", "Коэф.","Знаков", "Min", "Max",
            "Значение", "Актуальность"//,"Архив","Шлюз"
    };

    /**
     * Названия переменных для таблицы регистров.
     */
    public static  final String [] VARIABLES_CHANNEL=new String[]{
            "_CountNumber", "_Name", "_DeviceName","_TypeRegistryFullName", "_Address", "_AddressHex",
            "_Format", "_StrBaseValue", "_Koef","accuracyStr","minStr","maxStr",
            "valueStr","_StrDTAct"//,"_Archive","_Ext"
    };


    /**
     * Создать таблицу.
     * @param list источник данных
     * @param headers заголовки таблицы
     * @param variables переменные класса
     * @param obj образец класса
     * @return TableView
     * @param <T> текущий тип
     */
    public static  <T> TableView<T> createTable(ObservableList<T> list, String[]headers, String[]variables, T obj){

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
        ObservableList<T> rr= tableView.getItems();
        if(obj instanceof ClassChannel){
            int i=0;
            for (var el:rr){
                ((ClassChannel)el).set_CountNumber(++i);
            }
        }
        //Создание строк в таблице с привязкой прослушки на свойство _LinkStateNameProperty.
        tableView.setRowFactory(userTable->{
            PseudoClass up = PseudoClass.getPseudoClass("up");
            PseudoClass down = PseudoClass.getPseudoClass("down");
            //PseudoClass def = PseudoClass.getPseudoClass("def");
            TableRow<T> row = new TableRow<>();
            if(obj instanceof ClassDevice){
                ChangeListener<String> changeListener = (obs, oldPrice, newPrice) -> {

                    row.pseudoClassStateChanged(up, newPrice.equals("На связи"));
                    row.pseudoClassStateChanged(down, newPrice.equals("Нет связи")
                    );
                };
                //Настройка прослушивания для строки. Используется только при построении таблицы.
                row.itemProperty().addListener((device, previousUser, currentUser) -> {

                    if (previousUser != null) {
                        ((ClassDevice)previousUser)._LinkStateNameProperty().removeListener(changeListener);
                    }
                    if (currentUser != null) {
                        ((ClassDevice)currentUser)._LinkStateNameProperty().addListener(changeListener);
                        row.pseudoClassStateChanged(up, ((ClassDevice)currentUser)._LinkStateNameProperty().get().equals("На связи"));
                        row.pseudoClassStateChanged(down, ((ClassDevice)currentUser)._LinkStateNameProperty().get().equals("Нет связи"));
                    }
                    else {
                        row.pseudoClassStateChanged(up, false);
                        row.pseudoClassStateChanged(down, false);
                    }
                });
            }
            if(obj instanceof ClassChannel){
                ChangeListener<String> changeListener = (channel, oldPrice, newPrice) -> {

                    row.pseudoClassStateChanged(up, newPrice.equals("2"));
                    row.pseudoClassStateChanged(down, newPrice.equals("1"));
                    //row.pseudoClassStateChanged(def, newPrice.equals("0"));
                };

                row.itemProperty().addListener((device, previousUser, currentUser) -> {

                    if (previousUser != null) {
                        ((ClassChannel)previousUser).alertFlagProperty().removeListener(changeListener);
                    }
                    if (currentUser != null) {
                        ((ClassChannel)currentUser).alertFlagProperty().addListener(changeListener);
                        row.pseudoClassStateChanged(up, ((ClassChannel)currentUser).alertFlagProperty().get().equals("2"));
                        row.pseudoClassStateChanged(down, ((ClassChannel)currentUser).alertFlagProperty().get().equals("1"));
                        //row.pseudoClassStateChanged(def, ((ClassChannel)currentUser).alertFlagProperty().get().equals("0"));
                    }
                    else {
                        row.pseudoClassStateChanged(up, false);
                        row.pseudoClassStateChanged(down, false);
                    }
                });
            }
            return row;
        });
        //Настройка контекстного меню.
        tableView.setContextMenu(ContextMenuFactory.ContextMenuDevice(tableView,obj));
        //Видимость кнопок меню.
        tableView.setTableMenuButtonVisible(true);
        //Прокручивание списка мышью/пальцем. Шаг прокручивания 1.
        tableView.setOnMouseDragged(e->{
            int step=prevY<e.getY() ? 1 : -1;
            indexScroll+=step;
            tableView.scrollTo(indexScroll);
            prevY=e.getY();
        });
        //Нажатие кнопки мыши.
        tableView.setOnMousePressed(e->{
            secondsOfPressedMouse =LocalTime.now().getSecond();
            indexScrollPrev =indexScroll;
        });  
       //Отпускание кнопки мыши.
        tableView.setOnMouseReleased(e->{
            var offset= indexScrollPrev <indexScroll ? indexScroll - indexScrollPrev : indexScrollPrev - indexScroll;
            //Отсекаем случайные подвижки скрола при открытии контекстного меню.
            if(LocalTime.now().getSecond()- secondsOfPressedMouse >=1 && offset < 3){
                ContextMenu contextMenu=tableView.getContextMenu();
                contextMenu.show(tableView,e.getSceneX(),e.getSceneY());
            }
        });
        
        //Обнуление идекса прокручивания и предыдущей координаты мыши по Y при содании новой таблицы.
        indexScroll=0;
        indexScrollPrev =0;
        prevY=0;
        return tableView;
    }

    /**
     * Настройка колонок и ячеек таблицы.
     * @param obj1 объект-образец
     * @param obj2 текущее поле типа
     * @param hdr заголовок
     * @param vrb переменная
     * @return объект колонки таблицы
     * @param <T> текущий тип таблицы
     * @param <S> текущий параметр поля
     */
    private static <T,S> TableColumn<T,S> setColTable(T obj1, S obj2, String hdr, String vrb){
        TableColumn<T,S> col = new TableColumn<T,S>(hdr);

        col.setMinWidth(20);
        if(HEADERS_DEVICE[1].equals(hdr) && obj1 instanceof ClassDevice ||HEADES_CHANNEL[1].equals(hdr)){
            col.setPrefWidth(300);
            col.setMaxWidth(800);
        }
        col.setCellValueFactory(new PropertyValueFactory<T,S>(vrb));
        return col;
    }
}
