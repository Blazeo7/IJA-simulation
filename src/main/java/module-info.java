module com.ija {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign;
    requires com.fasterxml.jackson.databind;

    opens com.ija to javafx.fxml;

    exports com.ija;
    exports com.ija.Models;
    exports com.ija.Views;
    exports com.ija.Enums;
    exports com.ija.Interfaces;
    exports com.ija.Commands;
}