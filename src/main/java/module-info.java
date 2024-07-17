open module org.example.mmsd_al {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires com.fasterxml.jackson.dataformat.xml;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires annotations;
    requires jdk.httpserver;
    requires jdk.net;
    requires javafx.graphics;
    requires jlibmodbus;
    requires jssc;
    requires javafx.base;
    requires jdk.compiler;
    requires transitive org.xerial.sqlitejdbc;


    exports org.example.mmsd_al;
    exports org.example.mmsd_al.Classes;
    exports org.example.mmsd_al.UserControlsClasses;
    exports org.example.mmsd_al.ServiceClasses.Comparators;
}