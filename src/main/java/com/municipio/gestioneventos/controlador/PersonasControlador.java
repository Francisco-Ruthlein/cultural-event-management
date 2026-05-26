package com.municipio.gestioneventos.controlador;

import com.municipio.gestioneventos.modelo.entidades.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PersonasControlador {

    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, String> colNombre;
    @FXML private TableColumn<Persona, String> colDni;
    @FXML private TableColumn<Persona, String> colTelefono;
    @FXML private TableColumn<Persona, String> colEmail;
    @FXML private TableColumn<Persona, String> colRol;
    @FXML private TextField campoBusqueda;

    private GestorEventos gestor = GestorEventos.getInstancia();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreCompleto()));
        colDni.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDni()));
        colTelefono.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        colEmail.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCorreoElectronico()));
        colRol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getClass().getSimpleName()));
        cargarPersonas();
    }

    private void cargarPersonas() {
        ObservableList<Persona> personas =
                FXCollections.observableArrayList(gestor.listarPersonas());
        tablaPersonas.setItems(personas);
    }

    @FXML
    public void buscarPersona() {
        String texto = campoBusqueda.getText().trim();
        if (texto.isEmpty()) {
            cargarPersonas();
            return;
        }
        tablaPersonas.setItems(
                FXCollections.observableArrayList(gestor.buscarPersonasPorNombre(texto)));
    }

    @FXML
    public void nuevaPersona() {
        mostrarFormularioPersona(null);
    }

    @FXML
    public void editarPersona() {
        Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Sin selección", "Seleccioná una persona para editar.");
            return;
        }
        mostrarFormularioPersona(seleccionada);
    }

    private void mostrarFormularioPersona(Persona personaExistente) {
        boolean esEdicion = personaExistente != null;

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(esEdicion ? "Editar persona" : "Nueva persona");
        dialog.setHeaderText(esEdicion
                ? "Editando: " + personaExistente.getNombreCompleto()
                : "Completá los datos de la nueva persona");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Campos comunes
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

        ComboBox<String> comboRol = new ComboBox<>();
        comboRol.setItems(FXCollections.observableArrayList(
                "Participante", "Organizador", "Artista", "Curador", "Instructor"));
        comboRol.setPromptText("Seleccioná un rol");
        comboRol.setPrefWidth(220);

        // Campo extra dinámico
        Label labelExtra = new Label();
        TextField campoExtra = new TextField();
        campoExtra.setPrefWidth(220);
        HBox filaExtra = new HBox(12, labelExtra, campoExtra);
        filaExtra.setVisible(false);
        filaExtra.setManaged(false);

        grid.add(new Label("Nombre completo:"), 0, 0);  grid.add(campoNombre, 1, 0);
        grid.add(new Label("DNI:"), 0, 1);               grid.add(campoDni, 1, 1);
        grid.add(new Label("Teléfono:"), 0, 2);          grid.add(campoTel, 1, 2);
        grid.add(new Label("Email:"), 0, 3);             grid.add(campoEmail, 1, 3);
        grid.add(new Label("Rol:"), 0, 4);               grid.add(comboRol, 1, 4);
        grid.add(filaExtra, 1, 5);

        // Si es edición, pre-cargar datos
        if (esEdicion) {
            campoNombre.setText(personaExistente.getNombreCompleto());
            campoDni.setText(personaExistente.getDni());
            campoTel.setText(personaExistente.getTelefono() != null ? personaExistente.getTelefono() : "");
            campoEmail.setText(personaExistente.getCorreoElectronico() != null ? personaExistente.getCorreoElectronico() : "");
            comboRol.setValue(personaExistente.getClass().getSimpleName());
            comboRol.setDisable(true); // No se puede cambiar el rol en edición

            // Mostrar campo extra si corresponde
            if (personaExistente instanceof Artista a) {
                mostrarCampoExtra(filaExtra, labelExtra, campoExtra, "Género musical:", a.getGeneroMusical());
            } else if (personaExistente instanceof Curador c) {
                mostrarCampoExtra(filaExtra, labelExtra, campoExtra, "Especialidad de arte:", c.getEspecialidadArte());
            } else if (personaExistente instanceof Instructor i) {
                mostrarCampoExtra(filaExtra, labelExtra, campoExtra, "Área de especialización:", i.getAreaEspecializacion());
            }
        }

        // Listener del ComboBox para mostrar campo dinámico
        comboRol.setOnAction(e -> {
            String rol = comboRol.getValue();
            if (rol == null) return;
            switch (rol) {
                case "Artista" ->
                        mostrarCampoExtra(filaExtra, labelExtra, campoExtra, "Género musical:", "");
                case "Curador" ->
                        mostrarCampoExtra(filaExtra, labelExtra, campoExtra, "Especialidad de arte:", "");
                case "Instructor" ->
                        mostrarCampoExtra(filaExtra, labelExtra, campoExtra, "Área de especialización:", "");
                default -> {
                    filaExtra.setVisible(false);
                    filaExtra.setManaged(false);
                }
            }
            dialog.getDialogPane().getScene().getWindow().sizeToScene();
        });

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isEmpty() || resultado.get() != ButtonType.OK) return;

        String nombre = campoNombre.getText().trim();
        String dni    = campoDni.getText().trim();
        String tel    = campoTel.getText().trim();
        String email  = campoEmail.getText().trim();
        String rol    = comboRol.getValue();
        String extra  = campoExtra.getText().trim();

        if (rol == null) {
            mostrarAlerta("Rol requerido", "Seleccioná un rol para la persona.");
            return;
        }

        try {
            // Validaciones del modelo
            Persona.validarNombre(nombre);
            Persona.validarDni(dni);
            Persona.validarTelefono(tel);
            Persona.validarEmail(email);

            if (esEdicion) {
                // Edición: actualizar campos
                personaExistente.setNombreCompleto(nombre);
                personaExistente.setDni(dni);
                personaExistente.setTelefono(tel);
                personaExistente.setCorreoElectronico(email);
                if (personaExistente instanceof Artista a) a.setGeneroMusical(extra);
                else if (personaExistente instanceof Curador c) c.setEspecialidadArte(extra);
                else if (personaExistente instanceof Instructor i) i.setAreaEspecializacion(extra);
                gestor.actualizar(personaExistente);
                mostrarInfo("Éxito", "Persona actualizada correctamente.");
            } else {
                // Alta: instanciar subclase correcta
                Persona persona = switch (rol) {
                    case "Organizador" -> new Organizador();
                    case "Artista" -> {
                        Artista a = new Artista();
                        a.setGeneroMusical(extra);
                        yield a;
                    }
                    case "Curador" -> {
                        Curador c = new Curador();
                        c.setEspecialidadArte(extra);
                        yield c;
                    }
                    case "Instructor" -> {
                        Instructor i = new Instructor();
                        i.setAreaEspecializacion(extra);
                        yield i;
                    }
                    default -> new Participante();
                };
                persona.setNombreCompleto(nombre);
                persona.setDni(dni);
                persona.setTelefono(tel);
                persona.setCorreoElectronico(email);
                gestor.guardar(persona);
                mostrarInfo("Éxito", "Persona guardada correctamente.");
            }
            cargarPersonas();

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error de validación", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Error del sistema", "No se pudo guardar: " + e.getMessage());
        }
    }

    private void mostrarCampoExtra(HBox fila, Label label, TextField campo, String texto, String valor) {
        label.setText(texto);
        campo.setText(valor != null ? valor : "");
        fila.setVisible(true);
        fila.setManaged(true);
    }

    @FXML
    public void eliminarPersona() {
        Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Sin selección", "Seleccioná una persona para eliminar.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar a \"" + seleccionada.getNombreCompleto() + "\"?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");
        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            gestor.eliminarPersona(seleccionada.getId());
            cargarPersonas();
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