module com.municipio.gestioneventos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.naming;
    requires java.sql;

    // Módulos requeridos para Hibernate 6
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    // Abrir paquetes para que JavaFX e Hibernate puedan acceder a ellos
    opens com.municipio.gestioneventos to javafx.fxml;
    opens com.municipio.gestioneventos.controlador to javafx.fxml;
    opens com.municipio.gestioneventos.modelo.entidades to org.hibernate.orm.core, javafx.base;

    // Exportar paquetes para visibilidad del proyecto
    exports com.municipio.gestioneventos;
    exports com.municipio.gestioneventos.controlador;
    exports com.municipio.gestioneventos.modelo.entidades;
}