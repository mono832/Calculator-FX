module com.example.calculatorfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.scripting;
    requires commons.math3;

    opens com.example.calculatorfx to javafx.graphics;
    exports com.example.calculatorfx;
}