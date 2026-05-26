package com.municipio.gestioneventos.controlador;

import com.municipio.gestioneventos.modelo.entidades.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventosControlador {

    @FXML private TableView<Evento> tablaEventos;
    @FXML private TableColumn<Evento, String> colNombre;
    @FXML private TableColumn<Evento, String> colTipo;
    @FXML private TableColumn<Evento, String> colFecha;
    @FXML private TableColumn<Evento, String> colEstado;
    @FXML private TableColumn<Evento, String> colParticipantes;
    @FXML private TextField campoBusqueda;

    private GestorEventos gestor = GestorEventos.getInstancia();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colTipo.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getClass().getSimpleName()));
        colFecha.setCellValueFactory(data -> {
            LocalDate fecha = data.getValue().getFechaInicio();
            return new javafx.beans.property.SimpleStringProperty(
                    fecha != null ? fecha.toString() : "");
        });
        colEstado.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));
        colParticipantes.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(data.getValue().getParticipantes().size())));
        cargarEventos();
    }

    private void cargarEventos() {
        ObservableList<Evento> eventos =
                FXCollections.observableArrayList(gestor.listarEventos());
        tablaEventos.setItems(eventos);
    }

    @FXML
    public void buscarEvento() {
        String texto = campoBusqueda.getText().trim();
        if (texto.isEmpty()) {
            cargarEventos();
            return;
        }
        tablaEventos.setItems(
                FXCollections.observableArrayList(gestor.buscarEventosPorNombre(texto)));
    }

    @FXML
    public void editarEvento() {
        Evento seleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Sin selección", "Seleccioná un evento para editar.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar evento");
        dialog.setHeaderText("Editando: " + seleccionado.getNombre());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField campoNombre = new TextField(seleccionado.getNombre());
        DatePicker campoFecha = new DatePicker(seleccionado.getFechaInicio());
        TextField campoDuracion = new TextField(String.valueOf(seleccionado.getDuracionEstimada()));

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(campoNombre, 1, 0);
        grid.add(new Label("Fecha inicio:"), 0, 1);
        grid.add(campoFecha, 1, 1);
        grid.add(new Label("Duración (días):"), 0, 2);
        grid.add(campoDuracion, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                String nuevoNombre = campoNombre.getText().trim();
                if (nuevoNombre.isEmpty()) {
                    mostrarAlerta("Nombre inválido", "El nombre no puede estar vacío.");
                    return;
                }
                if (nuevoNombre.matches("[0-9]+")) {
                    mostrarAlerta("Nombre inválido", "El nombre no puede ser solo números. Ejemplo válido: 'Festival 2026'.");
                    return;
                }
                if (nuevoNombre.trim().length() < 3) {
                    mostrarAlerta("Nombre inválido", "El nombre debe tener al menos 3 caracteres.");
                    return;
                }
                int duracion = Integer.parseInt(campoDuracion.getText().trim());
                if (duracion <= 0) {
                    mostrarAlerta("Error", "La duración debe ser mayor a 0.");
                    return;
                }
                seleccionado.setNombre(nuevoNombre);
                seleccionado.setFechaInicio(campoFecha.getValue());
                seleccionado.setDuracionEstimada(duracion);
                gestor.actualizar(seleccionado);
                cargarEventos();
                mostrarInfo("Éxito", "Evento actualizado correctamente.");
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "La duración debe ser un número entero.");
            } catch (IllegalArgumentException e) {
                mostrarAlerta("Error de validación", e.getMessage());
            }
        }
    }

    @FXML
    public void cambiarEstado() {
        Evento seleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Sin selección", "Seleccioná un evento para cambiar su estado.");
            return;
        }

        String estadoActual = seleccionado.getEstado();
        String[] estadosPosibles = {"En planificación", "Confirmado", "En ejecución", "Finalizado"};

        String siguienteEstado = null;
        switch (estadoActual) {
            case "En Planificación" -> siguienteEstado = "Confirmado";
            case "En planificación" -> siguienteEstado = "Confirmado";
            case "Confirmado" -> siguienteEstado = "En ejecución";
            case "En ejecución" -> siguienteEstado = "Finalizado";
            case "Finalizado" -> {
                mostrarAlerta("Sin cambios posibles", "El evento ya está finalizado.");
                return;
            }
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(siguienteEstado,
                java.util.Arrays.stream(estadosPosibles)
                        .filter(e -> !e.equals(estadoActual))
                        .collect(Collectors.toList()));
        dialog.setTitle("Cambiar estado");
        dialog.setHeaderText("Estado actual: " + estadoActual);
        dialog.setContentText("Nuevo estado:");

        Optional<String> resultado = dialog.showAndWait();
        resultado.ifPresent(nuevoEstado -> {
            try {
                seleccionado.cambiarEstado(nuevoEstado);
                gestor.actualizar(seleccionado);
                cargarEventos();
                mostrarInfo("Éxito", "Estado cambiado a: " + nuevoEstado);
            } catch (IllegalStateException e) {
                mostrarAlerta("Transición inválida", e.getMessage());
            }
        });
    }

    @FXML
    public void eliminarEvento() {
        Evento seleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Sin selección", "Seleccioná un evento para eliminar.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar el evento \"" + seleccionado.getNombre() + "\"?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");
        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            gestor.eliminarEvento(seleccionado.getId());
            cargarEventos();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}