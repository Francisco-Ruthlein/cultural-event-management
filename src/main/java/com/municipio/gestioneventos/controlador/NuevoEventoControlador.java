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
        comboEstado.setVisible(false);
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

        if(tipo == null) {
            mostrarAlerta("Falta el tipo de evento", "Por favor seleccioná el tipo de evento.");
            return;

        }
        if (nombre.isEmpty()) {
            mostrarAlerta("Falta el nombre", "Por favor ingresá el nombre del evento.");
            return;
        }
        if (nombre.matches("[0-9]+")) {
            mostrarAlerta("Nombre inválido", "El nombre no puede ser solo números. Ejemplo válido: 'Festival 2026'.");
            return;
        }
        if (nombre.trim().length() < 3) {
            mostrarAlerta("Nombre inválido", "El nombre debe tener al menos 3 caracteres.");
            return;

        }

        if (!campoDuracion.getText().isEmpty()) {
            try {
                int duracion = Integer.parseInt(campoDuracion.getText());
                if (duracion <= 0) {
                    mostrarAlerta("Duración inválida", "La duración debe ser un número mayor a 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Duración inválida", "La duración debe ser un número entero.");
                return;
            }
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
            try {

                evento.setNombre(nombre);
                evento.setFechaInicio(campoFecha.getValue());
                evento.setDuracionEstimada(
                        campoDuracion.getText().isEmpty() ? 1 :
                                Integer.parseInt(campoDuracion.getText())
                );


                if (evento instanceof Feria) {
                    Feria miFeria = (Feria) evento;


                    TextField campoStands = (TextField) camposEspecificos.lookup("#campoStands");
                    CheckBox checkTechada = (CheckBox) camposEspecificos.lookup("#checkTechada");


                    if (campoStands != null && !campoStands.getText().isEmpty()) {
                        miFeria.setCantidadStands(Integer.parseInt(campoStands.getText()));
                    } else {
                        throw new IllegalArgumentException("La cantidad de stands es obligatoria para una Feria.");
                    }


                    if (checkTechada != null) {
                        miFeria.setEsTechada(checkTechada.isSelected());
                    }
                }



                gestor.guardar(evento);


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setContentText("Evento '" + nombre + "' guardado correctamente.");
                alert.showAndWait();

                cancelar();

            } catch (NumberFormatException e) {

                mostrarAlerta("Error de Formato", "Por favor, ingresá números válidos en los campos numéricos.");
            } catch (IllegalArgumentException e) {

                mostrarAlerta("Error de Validación", e.getMessage());
            } catch (Exception e) {
                mostrarAlerta("Error del Sistema", "Ocurrió un error al guardar: " + e.getMessage());
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
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