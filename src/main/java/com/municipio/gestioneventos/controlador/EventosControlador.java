package com.municipio.gestioneventos.controlador;

import com.municipio.gestioneventos.modelo.entidades.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EventosControlador {

    @FXML private TableView<Evento> tablaEventos;
    @FXML private TableColumn<Evento, String> colNombre;
    @FXML private TableColumn<Evento, String> colTipo;
    @FXML private TableColumn<Evento, String> colFecha;
    @FXML private TableColumn<Evento, String> colEstado;
    @FXML private TableColumn<Evento, String> colParticipantes;
    @FXML private TextField campoBusqueda;

    private GestorEventos gestor = new GestorEventos();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colEstado.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));
        cargarEventos();
    }

    private void cargarEventos() {
        ObservableList<Evento> eventos =
                FXCollections.observableArrayList(gestor.listarEventos());
        tablaEventos.setItems(eventos);
    }

    @FXML
    public void nuevoEvento() {
        System.out.println("Nuevo evento");
    }

    @FXML
    public void buscarEvento() {
        System.out.println("Buscando: " + campoBusqueda.getText());
    }

    @FXML
    public void editarEvento() {
        Evento seleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Editando: " + seleccionado.getNombre());
        }
    }

    @FXML
    public void cambiarEstado() {
        Evento seleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            seleccionado.cambiarEstado("confirmado");
            gestor.actualizar(seleccionado);
            cargarEventos();
        }
    }

    @FXML
    public void eliminarEvento() {
        Evento seleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            gestor.eliminarEvento(seleccionado.getId());
            cargarEventos();
        }
    }
}