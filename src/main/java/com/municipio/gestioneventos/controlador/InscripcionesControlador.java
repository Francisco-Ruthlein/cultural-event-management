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
                if (empty || evento == null) {
                    setText(null);
                } else {
                    String cupo = "";
                    if (evento instanceof Taller t && t.getCupoMaximo() > 0) {
                        cupo = " | Cupo: " + t.listarParticipantes().size()
                                + "/" + t.getCupoMaximo();
                    }
                    setText(evento.getNombre() + " [" + evento.getEstado() + "]" + cupo);
                }
            }
        });
        comboEventos.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Evento evento, boolean empty) {
                super.updateItem(evento, empty);
                if (empty || evento == null) {
                    setText(null);
                } else {
                    String cupo = "";
                    if (evento instanceof Taller t && t.getCupoMaximo() > 0) {
                        cupo = " | Cupo: " + t.listarParticipantes().size()
                                + "/" + t.getCupoMaximo();
                    }
                    setText(evento.getNombre() + " [" + evento.getEstado() + "]" + cupo);
                }
            }
        });
    }

    @FXML
    public void cargarParticipantes() {
        Evento seleccionado = comboEventos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        // Mostrar info de cupo si es Taller
        if (seleccionado instanceof Taller t && t.getCupoMaximo() > 0) {
            int ocupados = t.listarParticipantes().size();
            int maximo   = t.getCupoMaximo();
            if (ocupados >= maximo) {
                mostrarAlerta("Cupo lleno",
                        "Este taller tiene el cupo completo (" + maximo + "/" + maximo + ").");
            }
        }
        tablaParticipantes.setItems(
                FXCollections.observableArrayList(seleccionado.listarParticipantes()));
    }

    @FXML
    public void inscribirParticipante() {
        Evento evento = comboEventos.getSelectionModel().getSelectedItem();
        if (evento == null) {
            mostrarAlerta("Sin evento", "Seleccioná un evento primero.");
            return;
        }

        // Validar estado según enunciado
        String estado = evento.getEstado();
        if (!estado.equals("Confirmado")) {
            if (estado.equals("Finalizado")) {
                mostrarAlerta("Evento finalizado",
                        "No se pueden inscribir participantes en un evento finalizado.");
            } else {
                mostrarAlerta("Evento no disponible",
                        "Solo se pueden inscribir participantes en eventos Confirmados.\n"
                                + "Estado actual: " + estado);
            }
            return;
        }

        // Validar cupo si es Taller — recargar desde BD para tener dato fresco
        if (evento instanceof Taller t) {
            Evento eventoFresco = gestor.buscarPorId(evento.getId());
            if (eventoFresco instanceof Taller tFresco && tFresco.getCupoMaximo() > 0) {
                if (tFresco.listarParticipantes().size() >= tFresco.getCupoMaximo()) {
                    mostrarAlerta("Cupo lleno",
                            "El taller ya alcanzó su cupo máximo de "
                                    + tFresco.getCupoMaximo() + " participantes.");
                    return;
                }
            }
        }

        // Elegir entre participante existente o nuevo
        List<Participante> participantesExistentes = gestor.listarPersonas().stream()
                .filter(p -> p instanceof Participante)
                .map(p -> (Participante) p)
                .filter(p -> !evento.getParticipantes().contains(p)) // excluir ya inscriptos
                .collect(Collectors.toList());

        Alert opcion = new Alert(Alert.AlertType.CONFIRMATION);
        opcion.setTitle("Inscribir participante");
        opcion.setHeaderText("¿Cómo querés inscribir al participante?");
        ButtonType btnExistente = new ButtonType("Seleccionar existente");
        ButtonType btnNuevo     = new ButtonType("Crear nuevo");
        ButtonType btnCancelar  = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        opcion.getButtonTypes().setAll(btnExistente, btnNuevo, btnCancelar);
        Optional<ButtonType> respuesta = opcion.showAndWait();
        if (respuesta.isEmpty() || respuesta.get() == btnCancelar) return;

        Participante participante = null;

        if (respuesta.get() == btnExistente) {
            if (participantesExistentes.isEmpty()) {
                mostrarAlerta("Sin participantes disponibles",
                        "No hay participantes sin inscribir en este evento.\nUsá 'Crear nuevo'.");
                return;
            }
            ChoiceDialog<Participante> elegir = new ChoiceDialog<>(
                    participantesExistentes.get(0), participantesExistentes);
            elegir.setTitle("Seleccionar participante");
            elegir.setHeaderText("Elegí el participante a inscribir en:\n" + evento.getNombre());
            elegir.setContentText("Participante:");
            Optional<Participante> elegido = elegir.showAndWait();
            if (elegido.isEmpty()) return;
            participante = elegido.get();

        } else {
            // Formulario nuevo participante con validaciones del modelo
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nuevo participante");
            dialog.setHeaderText("Completá los datos del participante");
            GridPane grid = new GridPane();
            grid.setHgap(12); grid.setVgap(10); grid.setPadding(new Insets(20));

            TextField campoNombre = new TextField();
            campoNombre.setPromptText("Ej: Juan Pérez");
            campoNombre.setPrefWidth(220);
            TextField campoDni = new TextField();
            campoDni.setPromptText("Solo números sin puntos. Ej: 12345678");
            campoDni.setPrefWidth(220);
            TextField campoTel = new TextField();
            campoTel.setPromptText("Solo números. Ej: +5493764123456");
            campoTel.setPrefWidth(220);
            TextField campoEmail = new TextField();
            campoEmail.setPromptText("Ej: nombre@dominio.com");
            campoEmail.setPrefWidth(220);

            grid.add(new Label("Nombre:"),   0, 0); grid.add(campoNombre, 1, 0);
            grid.add(new Label("DNI:"),      0, 1); grid.add(campoDni,    1, 1);
            grid.add(new Label("Teléfono:"), 0, 2); grid.add(campoTel,    1, 2);
            grid.add(new Label("Email:"),    0, 3); grid.add(campoEmail,  1, 3);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> res = dialog.showAndWait();
            if (res.isEmpty() || res.get() != ButtonType.OK) return;

            try {
                // Validaciones del modelo rico
                Persona.validarNombre(campoNombre.getText().trim());
                Persona.validarDni(campoDni.getText().trim());
                Persona.validarTelefono(campoTel.getText().trim());
                Persona.validarEmail(campoEmail.getText().trim());

                Participante nuevo = new Participante();
                nuevo.setNombreCompleto(campoNombre.getText().trim());
                nuevo.setDni(campoDni.getText().trim());
                nuevo.setTelefono(campoTel.getText().trim());
                nuevo.setCorreoElectronico(campoEmail.getText().trim());
                gestor.guardar(nuevo);
                participante = nuevo;

            } catch (IllegalArgumentException e) {
                mostrarAlerta("Error de validación", e.getMessage());
                return;
            }
        }

        // Registrar inscripción
        boolean exito = evento.registrarParticipante(participante);
        if (exito) {
            gestor.actualizar(evento);
            cargarParticipantes();
            mostrarInfo("Inscripción exitosa",
                    participante.getNombreCompleto() + " fue inscripto/a en \"" + evento.getNombre() + "\".");
        } else {
            mostrarAlerta("No se pudo inscribir",
                    "El evento no acepta inscripciones o el cupo está lleno.");
        }
    }

    @FXML
    public void darDeBaja() {
        Evento evento        = comboEventos.getSelectionModel().getSelectedItem();
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
        confirmacion.setContentText("Se eliminará su inscripción al evento \"" + evento.getNombre() + "\".");
        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            evento.getParticipantes().remove(seleccionado);
            gestor.actualizar(evento);
            cargarParticipantes();
            mostrarInfo("Baja realizada",
                    seleccionado.getNombreCompleto() + " fue dado/a de baja del evento.");
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