module com.municipio.gestioneventos {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;

    opens com.municipio.gestioneventos to javafx.fxml;
    opens com.municipio.gestioneventos.modelo.entidades to
            org.hibernate.orm.core, javafx.base;
    opens com.municipio.gestioneventos.controlador to javafx.fxml;

    exports com.municipio.gestioneventos;
    exports com.municipio.gestioneventos.controlador;
    exports com.municipio.gestioneventos.modelo.entidades;
}