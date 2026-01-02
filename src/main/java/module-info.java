module com.mycompany.clientside {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.google.gson;

    opens com.mycompany.clientside.serverhandler.message to com.google.gson;
    opens com.mycompany.clientside.controllers to javafx.fxml;
    exports com.mycompany.clientside;
}
