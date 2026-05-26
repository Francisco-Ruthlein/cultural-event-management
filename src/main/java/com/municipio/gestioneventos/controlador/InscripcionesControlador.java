package com.municipio.gestioneventos.controlador;

import com.municipio.gestioneventos.modelo.entidades.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InscripcionesControlador {

    @FXML private ComboBox<Evento> comboEventos;
    @FXML private TableView<Participante> tablaParticipantes;
    @FXML private TableColumn<Participante, String> colNombre;
    @FXML private TableColumn<Participante, String> colDni;
    @FXML private TableColumn<Participante, String> colEmail;
    @FXML private TableColumn<Participante, String> colTelefono;

    private GestorEventos gestor = GestorEventos.getInstancia();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreCompleto()));
        colDni.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDni()));
        colEmail.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCorreoElectronico()));
        colTelefono.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));

        comboEventos.setItems(FXCollections.observableArrayList(gestor.listarEventos()));

        comboEventos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Evento evento, boolean empty) {
                super.updateItem(evento, empty);
                setText(empty || evento == null ? null :
                        evento.getNombre() + " [" + evento.getEstado() + "]");
            }
        });
        comboEventos.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Evento evento, boolean empty) {
                super.updateItem(evento, empty);
                setText(empty || evento == null ? null :
                        evento.getNombre() + " [" + evento.getEstado() + "]");
            }
        });
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
        if (evento == null) {
            mostrarAlerta("Sin evento", "Seleccioná un evento primero.");
            return;
        }
        if (!evento.getEstado().equals("Confirmado")) {
            mostrarAlerta("Evento no disponible",
                    "Solo se pueden inscribir participantes en eventos Confirmados.\n" +
                            "Estado actual: " + evento.getEstado());
            return;
        }

        List<Participante> participantesExistentes = gestor.listarPersonas().stream()
                .filter(p -> p instanceof Participante)
                .map(p -> (Participante) p)
                .collect(Collectors.toList());

        Alert opcion = new Alert(Alert.AlertType.CONFIRMATION);
        opcion.setTitle("Inscribir participante");
        opcion.setHeaderText("¿Cómo querés inscribir al participante?");
        ButtonType btnExistente = new ButtonType("Seleccionar existente");
        ButtonType btnNuevo = new ButtonType("Crear nuevo");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        opcion.getButtonTypes().setAll(btnExistente, btnNuevo, btnCancelar);
        Optional<ButtonType> respuesta = opcion.showAndWait();
        if (respuesta.isEmpty() || respuesta.get() == btnCancelar) return;

        Participante participante = null;

        if (respuesta.get() == btnExistente) {
            if (participantesExistentes.isEmpty()) {
                mostrarAlerta("Sin participantes",
                        "No hay participantes registrados en el sistema.\nUsá 'Crear nuevo'.");
                return;
            }
            ChoiceDialog<Participante> elegir = new ChoiceDialog<>(
                    participantesExistentes.get(0), participantesExistentes);
            elegir.setTitle("Seleccionar participante");
            elegir.setHeaderText("Elegí el participante a inscribir");
            elegir.setContentText("Participante:");
            Optional<Participante> elegido = elegir.showAndWait();
            if (elegido.isEmpty()) return;
            participante = elegido.get();
        } else {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nuevo participante");
            dialog.setHeaderText("Datos del participante");
            GridPane grid = new GridPane();
            grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));
            TextField campoNombre = new TextField(); campoNombre.setPromptText("Nombre completo");
            TextField campoDni    = new TextField(); campoDni.setPromptText("DNI");
            TextField campoTel    = new TextField(); campoTel.setPromptText("Teléfono");
            TextField campoEmail  = new TextField(); campoEmail.setPromptText("Email");
            grid.add(new Label("Nombre:"), 0, 0);   grid.add(campoNombre, 1, 0);
            grid.add(new Label("DNI:"), 0, 1);       grid.add(campoDni, 1, 1);
            grid.add(new Label("Teléfono:"), 0, 2);  grid.add(campoTel, 1, 2);
            grid.add(new Label("Email:"), 0, 3);     grid.add(campoEmail, 1, 3);
            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> res = dialog.showAndWait();
            if (res.isEmpty() || res.get() != ButtonType.OK) return;
            String nombre = campoNombre.getText().trim();
            String dni    = campoDni.getText().trim();
            if (nombre.isEmpty() || dni.isEmpty()) {
                mostrarAlerta("Datos incompletos", "El nombre y el DNI son obligatorios.");
                return;
            }
            Participante nuevo = new Participante();
            nuevo.setNombreCompleto(nombre);
            nuevo.setDni(dni);
            nuevo.setTelefono(campoTel.getText().trim());
            nuevo.setCorreoElectronico(campoEmail.getText().trim());
            gestor.guardar(nuevo);
            participante = nuevo;
        }

        boolean exito = evento.registrarParticipante(participante);
        if (exito) {
            gestor.actualizar(evento);
            cargarParticipantes();
            mostrarInfo("Inscripción exitosa",
                    participante.getNombreCompleto() + " fue inscripto/a en " + evento.getNombre());
        } else {
            mostrarAlerta("No se pudo inscribir",
                    "El evento no acepta inscripciones en este momento o el cupo está lleno.");
        }
    }

    @FXML
    public void darDeBaja() {
        Evento evento = comboEventos.getSelectionModel().getSelectedItem();
        Participante seleccionado = tablaParticipantes.getSelectionModel().getSelectedItem();

        if (evento == null) {
            mostrarAlerta("Sin evento", "Seleccioná un evento primero.");
            return;
        }
        if (seleccionado == null) {
            mostrarAlerta("Sin selección", "Seleccioná un participante para dar de baja.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar baja");
        confirmacion.setHeaderText("¿Dar de baja a " + seleccionado.getNombreCompleto() + "?");
        confirmacion.setContentText("Se eliminará su inscripción al evento.");
        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            evento.getParticipantes().remove(seleccionado);
            gestor.actualizar(evento);
            cargarParticipantes();
            mostrarInfo("Baja realizada",
                    seleccionado.getNombreCompleto() + " fue dado/a de baja.");
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