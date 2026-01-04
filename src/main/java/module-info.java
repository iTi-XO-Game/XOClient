module com.mycompany.clientside {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.desktop;
    requires javafx.media;

    opens com.mycompany.clientside.controllers to javafx.fxml;
    exports com.mycompany.clientside;
}
