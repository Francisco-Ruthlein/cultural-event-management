package com.municipio.gestioneventos.controlador;

import com.municipio.gestioneventos.modelo.entidades.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
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

    private GestorEventos gestor = GestorEventos.getInstancia();
    private List<Evento> todosLosEventos;

    @FXML
    public void initialize() {
        colFecha.setCellValueFactory(data -> {
            LocalDate fecha = data.getValue().getFechaInicio();
            return new javafx.beans.property.SimpleStringProperty(
                    fecha != null ? fecha.toString() : "Sin fecha");
        });
        colNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colTipo.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getClass().getSimpleName()));
        colEstado.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));
        colDuracion.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDuracionEstimada() + " día/s"));

        // Colorear filas según estado
        tablaCalendario.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Evento evento, boolean empty) {
                super.updateItem(evento, empty);
                if (empty || evento == null) {
                    setStyle("");
                    return;
                }
                String color;
                String estado = evento.getEstado();
                if (estado.equals("Confirmado")) {
                    color = "#e8f5e9";
                } else if (estado.equals("En ejecución")) {
                    color = "#fff9c4";
                } else if (estado.equals("Finalizado")) {
                    color = "#f5f5f5";
                } else {
                    color = "transparent";
                }
                if (isSelected()) {
                    setStyle("-fx-background-color: #185FA5; -fx-text-fill: white;");
                } else {
                    setStyle("-fx-background-color: " + color + "; -fx-text-fill: black;");
                }
            }
        });

        comboMes.setItems(FXCollections.observableArrayList(
                "Enero","Febrero","Marzo","Abril","Mayo","Junio",
                "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
        ));
        comboEstado.setItems(FXCollections.observableArrayList(
                "En planificación","Confirmado","En ejecución","Finalizado"
        ));

        cargarEventos();
    }

    private void cargarEventos() {
        todosLosEventos = gestor.listarEventos().stream()
                .sorted((a, b) -> {
                    if (a.getFechaInicio() == null) return 1;
                    if (b.getFechaInicio() == null) return -1;
                    return a.getFechaInicio().compareTo(b.getFechaInicio());
                })
                .collect(Collectors.toList());
        tablaCalendario.setItems(FXCollections.observableArrayList(todosLosEventos));
    }

    @FXML
    public void filtrarPorMes() {
        aplicarFiltros();
    }

    @FXML
    public void filtrarPorEstado() {
        aplicarFiltros();
    }

    // Aplica ambos filtros combinados
    private void aplicarFiltros() {
        String mes    = comboMes.getSelectionModel().getSelectedItem();
        String estado = comboEstado.getSelectionModel().getSelectedItem();
        int numMes    = comboMes.getSelectionModel().getSelectedIndex() + 1;

        List<Evento> filtrados = todosLosEventos.stream()
                .filter(e -> {
                    if (mes == null) return true;
                    return e.getFechaInicio() != null
                            && e.getFechaInicio().getMonthValue() == numMes;
                })
                .filter(e -> {
                    if (estado == null) return true;
                    return e.getEstado().equalsIgnoreCase(estado);
                })
                .collect(Collectors.toList());

        tablaCalendario.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML
    public void verTodos() {
        comboMes.getSelectionModel().clearSelection();
        comboEstado.getSelectionModel().clearSelection();
        tablaCalendario.setItems(FXCollections.observableArrayList(todosLosEventos));
    }
}