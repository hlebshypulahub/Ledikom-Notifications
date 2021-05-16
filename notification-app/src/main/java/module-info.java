module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires org.postgresql.jdbc;
    requires org.apache.commons.io;

    opens org.openjfx.ledicom.entities to javafx.base;
    opens org.openjfx.ledicom to javafx.fxml;
    opens org.openjfx;
}