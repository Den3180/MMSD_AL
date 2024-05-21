open module org.example.mmsd_al {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    //org.example.mmsd_al to javafx.fxml;
    exports org.example.mmsd_al;
    exports org.example.mmsd_al.Classes;
}