module com.municipio.gestioneventos {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens com.municipio.gestioneventos to javafx.fxml;
    opens com.municipio.gestioneventos.modelo.entidades to org.hibernate.orm.core;

    exports com.municipio.gestioneventos;
}