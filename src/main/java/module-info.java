module org.oop.arknoid_oop {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires java.desktop;
    requires javafx.media;

    opens org.oop.arknoid_oop to javafx.fxml;
    exports org.oop.arknoid_oop;
    exports org.oop.arknoid_oop.Controllers;
    opens org.oop.arknoid_oop.Controllers to javafx.fxml;
}