module com.epn.proyecto_poo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jbcrypt;
    requires okhttp3;
    requires com.google.gson;

    opens com.epn.proyecto_poo to javafx.fxml;
    opens com.epn.proyecto_poo.controlador to javafx.fxml;
    opens com.epn.proyecto_poo.modelo to org.hibernate.orm.core;
    opens com.epn.proyecto_poo.servicio to javafx.fxml;
    exports com.epn.proyecto_poo.modelo;
    exports com.epn.proyecto_poo to javafx.graphics;
    exports com.epn.proyecto_poo.controlador;
    exports com.epn.proyecto_poo.servicio;
    exports com.epn.proyecto_poo.api;
}
