module com.epn.proyecto_poo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jbcrypt;

    opens com.epn.proyecto_poo to javafx.fxml;
    opens com.epn.proyecto_poo.modelo to org.hibernate.orm.core;
    exports com.epn.proyecto_poo.modelo;
    exports com.epn.proyecto_poo to javafx.graphics;

}