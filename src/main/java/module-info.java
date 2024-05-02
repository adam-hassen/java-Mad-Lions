module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires jasperreports;
    requires stripe.java;
    requires AnimateFX;
    requires itext;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;

}