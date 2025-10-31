module org.oop.arknoid_oop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;
    requires java.desktop;

    opens org.oop.arknoid_oop to javafx.fxml;
    exports org.oop.arknoid_oop;
    exports org.oop.arknoid_oop.Controllers;
    opens org.oop.arknoid_oop.Controllers to javafx.fxml;
}