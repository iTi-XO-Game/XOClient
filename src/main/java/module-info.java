module com.mycompany.clientside {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.google.gson;
    requires java.desktop;
    requires javafx.media;

    opens com.mycompany.clientside.controllers to javafx.fxml;
    opens com.mycompany.clientside.models to com.google.gson;
    exports com.mycompany.clientside;
}
