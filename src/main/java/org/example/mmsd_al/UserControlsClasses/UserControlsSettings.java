package org.example.mmsd_al.UserControlsClasses;



public class UserControlsSettings {

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
}
