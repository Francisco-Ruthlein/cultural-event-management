package com.municipio.gestioneventos.controlador;

import com.municipio.gestioneventos.modelo.entidades.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class InscripcionesControlador {

    @FXML private ComboBox<Evento> comboEventos;
    @FXML private TableView<Participante> tablaParticipantes;
    @FXML private TableColumn<Participante, String> colNombre;
    @FXML private TableColumn<Participante, String> colDni;
    @FXML private TableColumn<Participante, String> colEmail;
    @FXML private TableColumn<Participante, String> colTelefono;

    private GestorEventos gestor = new GestorEventos();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getNombreCompleto()));
        colDni.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDni()));
        colEmail.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCorreoElectronico()));
        colTelefono.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getTelefono()));

        comboEventos.setItems(
                FXCollections.observableArrayList(gestor.listarEventos()));
    }

    @FXML
    public void cargarParticipantes() {
        Evento seleccionado = comboEventos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            ObservableList<Participante> participantes =
                    FXCollections.observableArrayList(seleccionado.listarParticipantes());
            tablaParticipantes.setItems(participantes);
        }
    }

    @FXML
    public void inscribirParticipante() {
        Evento evento = comboEventos.getSelectionModel().getSelectedItem();
        if (evento != null) {
            System.out.println("Inscribir participante en: " + evento.getNombre());
        }
    }

    @FXML
    public void darDeBaja() {
        Participante seleccionado = tablaParticipantes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Dando de baja: " + seleccionado.getNombreCompleto());
        }
    }
}
