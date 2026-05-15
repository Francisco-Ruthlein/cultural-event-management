package com.municipio.gestioneventos.controlador;

import com.municipio.gestioneventos.modelo.entidades.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarioControlador {

    @FXML private TableView<Evento> tablaCalendario;
    @FXML private TableColumn<Evento, String> colFecha;
    @FXML private TableColumn<Evento, String> colNombre;
    @FXML private TableColumn<Evento, String> colTipo;
    @FXML private TableColumn<Evento, String> colEstado;
    @FXML private TableColumn<Evento, String> colDuracion;
    @FXML private ComboBox<String> comboMes;
    @FXML private ComboBox<String> comboEstado;

    private GestorEventos gestor = new GestorEventos();
    private List<Evento> todosLosEventos;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getNombre()));
        colFecha.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getFechaInicio().toString()));
        colEstado.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getEstado()));
        colTipo.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getClass().getSimpleName()));
        colDuracion.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(data.getValue().getDuracionEstimada())));

        comboMes.setItems(FXCollections.observableArrayList(
                "Enero","Febrero","Marzo","Abril","Mayo","Junio",
                "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
        ));
        comboEstado.setItems(FXCollections.observableArrayList(
                "En planificación","Confirmado","En ejecución","Finalizado"
        ));

        todosLosEventos = gestor.listarEventos();
        tablaCalendario.setItems(FXCollections.observableArrayList(todosLosEventos));
    }

    @FXML
    public void filtrarPorMes() {
        String mes = comboMes.getSelectionModel().getSelectedItem();
        if (mes != null) {
            int numMes = comboMes.getSelectionModel().getSelectedIndex() + 1;
            List<Evento> filtrados = todosLosEventos.stream()
                    .filter(e -> e.getFechaInicio().getMonthValue() == numMes)
                    .collect(Collectors.toList());
            tablaCalendario.setItems(FXCollections.observableArrayList(filtrados));
        }
    }

    @FXML
    public void filtrarPorEstado() {
        String estado = comboEstado.getSelectionModel().getSelectedItem();
        if (estado != null) {
            List<Evento> filtrados = todosLosEventos.stream()
                    .filter(e -> e.getEstado().equalsIgnoreCase(estado))
                    .collect(Collectors.toList());
            tablaCalendario.setItems(FXCollections.observableArrayList(filtrados));
        }
    }

    @FXML
    public void verTodos() {
        tablaCalendario.setItems(FXCollections.observableArrayList(todosLosEventos));
        comboMes.getSelectionModel().clearSelection();
        comboEstado.getSelectionModel().clearSelection();
    }
}