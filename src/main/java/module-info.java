module org.oop.arknoid_oop {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires java.desktop;
    requires java.sql;
    requires jbcrypt;
    requires java.logging;

    opens org.oop.arknoid_oop to javafx.fxml,javafx.base;
    exports org.oop.arknoid_oop;
    exports org.oop.arknoid_oop.Controller;
    opens org.oop.arknoid_oop.Controller to javafx.fxml,javafx.base;
    opens org.oop.arknoid_oop.Entity to javafx.fxml,javafx.base;
}