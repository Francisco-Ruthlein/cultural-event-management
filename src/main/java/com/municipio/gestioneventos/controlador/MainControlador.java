package com.municipio.gestioneventos.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainControlador {

    @FXML private StackPane contenidoCentral;
    @FXML private Label headerTitulo;
    @FXML private Label headerSubtitulo;

    @FXML
    public void mostrarInicio() {
        headerTitulo.setText("Panel principal");
        headerSubtitulo.setText("Resumen general del sistema");
        contenidoCentral.getChildren().clear();
        Label label = new Label("Bienvenido al sistema de gestión de eventos");
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
        contenidoCentral.getChildren().add(label);
    }

    @FXML
    public void mostrarEventos() {
        headerTitulo.setText("Eventos");
        headerSubtitulo.setText("Gestión de eventos culturales");
        cargarVista("eventos-view.fxml");
    }

    @FXML
    public void mostrarPersonas() {
        headerTitulo.setText("Personas");
        headerSubtitulo.setText("Gestión de personas del sistema");
        cargarVista("personas-view.fxml");
    }

    @FXML
    public void mostrarInscripciones() {
        headerTitulo.setText("Inscripciones");
        headerSubtitulo.setText("Registro de participantes en eventos");
        cargarVista("inscripciones-view.fxml");
    }

    @FXML
    public void mostrarCalendario() {
        headerTitulo.setText("Calendario");
        headerSubtitulo.setText("Visualización de eventos por fecha");
        cargarVista("calendario-view.fxml");
    }

    @FXML
    public void nuevoEvento() {
        headerTitulo.setText("Nuevo evento");
        headerSubtitulo.setText("Completá los datos del evento");
        cargarVista("nuevo-evento-view.fxml");
    }

    private void cargarVista(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/municipio/gestioneventos/" + fxml)
            );
            contenidoCentral.getChildren().clear();
            contenidoCentral.getChildren().add(loader.load());
        } catch (IOException e) {
            Label error = new Label("Vista en construcción...");
            error.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
            contenidoCentral.getChildren().clear();
            contenidoCentral.getChildren().add(error);
        }
    }
}