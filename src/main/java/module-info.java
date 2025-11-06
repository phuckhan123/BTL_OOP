module org.oop.arknoid_oop {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires java.desktop;

    opens org.oop.arknoid_oop to javafx.fxml;
    exports org.oop.arknoid_oop;
    exports org.oop.arknoid_oop.Controllers;
    opens org.oop.arknoid_oop.Controllers to javafx.fxml;
}