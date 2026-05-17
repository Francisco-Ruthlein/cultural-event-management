package com.municipio.gestioneventos.controlador;

import com.municipio.gestioneventos.modelo.entidades.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class NuevoEventoControlador {

    @FXML private ComboBox<String> comboTipo;
    @FXML private ComboBox<String> comboEstado;
    @FXML private TextField campoNombre;
    @FXML private DatePicker campoFecha;
    @FXML private TextField campoDuracion;
    @FXML private VBox camposEspecificos;

    private GestorEventos gestor =  GestorEventos.getInstancia();

    @FXML
    public void initialize() {
        comboTipo.setItems(FXCollections.observableArrayList(
                "Feria", "Concierto", "Exposicion", "Taller", "Ciclo de Cine"
        ));
        comboEstado.setItems(FXCollections.observableArrayList(
                "En planificación", "Confirmado", "En ejecución", "Finalizado"
        ));
    }

    @FXML
    public void mostrarCamposEspecificos() {
        camposEspecificos.getChildren().clear();
        String tipo = comboTipo.getSelectionModel().getSelectedItem();
        if (tipo == null) return;

        switch (tipo) {
            case "Feria" -> {
                camposEspecificos.getChildren().addAll(
                        crearCampo("Cantidad de stands:", "campoStands"),
                        crearCheckbox("¿Es techada?", "checkTechada")
                );
            }
            case "Concierto" -> {
                camposEspecificos.getChildren().addAll(
                        crearCheckbox("¿Entrada gratuita?", "checkGratuita")
                );
            }
            case "Exposicion" -> {
                camposEspecificos.getChildren().addAll(
                        crearCampo("Tipo de arte:", "campoTipoArte")
                );
            }
            case "Taller" -> {
                camposEspecificos.getChildren().addAll(
                        crearCampo("Cupo máximo:", "campoCupo"),
                        crearCampo("Modalidad:", "campoModalidad")
                );
            }
            case "Ciclo de Cine" -> {
                camposEspecificos.getChildren().addAll(
                        crearCampo("Orden de proyección:", "campoOrden"),
                        crearCheckbox("¿Tiene charla posterior?", "checkCharla")
                );
            }
        }
    }

    private HBox crearCampo(String label, String id) {
        HBox hbox = new HBox(10);
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 13px;");
        lbl.setPrefWidth(150);
        TextField tf = new TextField();
        tf.setId(id);
        tf.setPrefWidth(200);
        hbox.getChildren().addAll(lbl, tf);
        return hbox;
    }

    private HBox crearCheckbox(String label, String id) {
        HBox hbox = new HBox(10);
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        CheckBox cb = new CheckBox(label);
        cb.setId(id);
        cb.setStyle("-fx-font-size: 13px;");
        hbox.getChildren().add(cb);
        return hbox;
    }

    @FXML
    public void guardarEvento() {
        String tipo = comboTipo.getSelectionModel().getSelectedItem();
        String nombre = campoNombre.getText();
        String estado = comboEstado.getSelectionModel().getSelectedItem();

        if (tipo == null || nombre.isEmpty() || estado == null || campoFecha.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos incompletos");
            alert.setContentText("Por favor completá todos los campos obligatorios.");
            alert.showAndWait();
            return;
        }

        Evento evento = switch (tipo) {
            case "Feria" -> new Feria();
            case "Concierto" -> new Concierto();
            case "Exposicion" -> new Exposicion();
            case "Taller" -> new Taller();
            case "Ciclo de Cine" -> new CicloDeCine();
            default -> null;
        };

        if (evento != null) {
            evento.setNombre(nombre);
            evento.setFechaInicio(campoFecha.getValue());
            evento.setEstado(estado);
            evento.setDuracionEstimada(
                    campoDuracion.getText().isEmpty() ? 1 :
                            Integer.parseInt(campoDuracion.getText())
            );
            gestor.guardar(evento);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setContentText("Evento guardado correctamente.");
            alert.showAndWait();
        }
    }

    @FXML
    public void cancelar() {
        campoNombre.clear();
        campoFecha.setValue(null);
        campoDuracion.clear();
        comboTipo.getSelectionModel().clearSelection();
        comboEstado.getSelectionModel().clearSelection();
        camposEspecificos.getChildren().clear();
    }
}