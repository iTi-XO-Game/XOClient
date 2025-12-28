module com.mycompany.clientside {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.clientside to javafx.fxml;

    opens com.mycompany.clientside.controllers to javafx.fxml;
    opens controllers to javafx.fxml;

    exports com.mycompany.clientside;
}
