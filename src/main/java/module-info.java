module com.example.gestioneventos {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.municipio.gestioneventos to javafx.fxml;
    exports com.municipio.gestioneventos;
}