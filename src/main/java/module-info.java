module com.example.parcialfinal {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    opens com.example.parcialfinal to javafx.fxml;
    opens com.example.parcialfinal.App to javafx.fxml;
    opens com.example.parcialfinal.Models to javafx.fxml;
    opens com.example.parcialfinal.Repository to javafx.fxml;
    opens com.example.parcialfinal.Controllers to javafx.fxml;

    exports com.example.parcialfinal.Controllers;
    exports com.example.parcialfinal.App;
    exports com.example.parcialfinal.Models;
    exports com.example.parcialfinal.Repository;
}