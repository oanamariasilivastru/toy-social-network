module com.example.demo11 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    requires java.sql;

    opens com.example.demo11 to javafx.fxml;
    exports com.example.demo11;

    opens com.example.demo11.domain to javafx.base;
    exports com.example.demo11.domain;
}