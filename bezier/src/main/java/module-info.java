module com.example.bezier {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bezier to javafx.fxml;
    exports com.example.bezier;
}