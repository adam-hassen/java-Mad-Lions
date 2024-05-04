module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    requires stripe.java;
    requires AnimateFX;

    requires java.desktop;
    requires org.apache.pdfbox;
    requires kernel;
    requires layout;
    requires io;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;

}