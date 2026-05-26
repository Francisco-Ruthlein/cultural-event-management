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


                if (evento instanceof Feria miFeria) {
                    TextField campoStands = (TextField) camposEspecificos.lookup("#campoStands");
                    CheckBox checkTechada = (CheckBox) camposEspecificos.lookup("#checkTechada");
                    if (campoStands == null || campoStands.getText().isEmpty()) {
                        throw new IllegalArgumentException("La cantidad de stands es obligatoria para una Feria.");
                    }
                    miFeria.setCantidadStands(Integer.parseInt(campoStands.getText()));
                    if (checkTechada != null) miFeria.setEsTechada(checkTechada.isSelected());

                } else if (evento instanceof Taller miTaller) {
                    TextField campoCupo      = (TextField) camposEspecificos.lookup("#campoCupo");
                    TextField campoModalidad = (TextField) camposEspecificos.lookup("#campoModalidad");
                    if (campoCupo == null || campoCupo.getText().isEmpty()) {
                        throw new IllegalArgumentException("El cupo máximo es obligatorio para un Taller.");
                    }
                    int cupo = Integer.parseInt(campoCupo.getText().trim());
                    if (cupo <= 0) throw new IllegalArgumentException("El cupo máximo debe ser mayor a 0.");
                    miTaller.setCupoMaximo(cupo);
                    if (campoModalidad != null && !campoModalidad.getText().isEmpty()) {
                        miTaller.setModalidad(campoModalidad.getText().trim());
                    }

                } else if (evento instanceof Concierto miConcierto) {
                    CheckBox checkGratuita = (CheckBox) camposEspecificos.lookup("#checkGratuita");
                    if (checkGratuita != null) miConcierto.setEsEntradaGratuita(checkGratuita.isSelected());

                } else if (evento instanceof Exposicion miExposicion) {
                    TextField campoTipoArte = (TextField) camposEspecificos.lookup("#campoTipoArte");
                    if (campoTipoArte != null && !campoTipoArte.getText().isEmpty()) {
                        miExposicion.setTipoArte(campoTipoArte.getText().trim());
                    }

                } else if (evento instanceof CicloDeCine miCiclo) {
                    TextField campoOrden = (TextField) camposEspecificos.lookup("#campoOrden");
                    CheckBox checkCharla = (CheckBox) camposEspecificos.lookup("#checkCharla");
                    if (campoOrden != null && !campoOrden.getText().isEmpty()) {
                        miCiclo.setOrdenProyeccion(campoOrden.getText().trim());
                    }
                    if (checkCharla != null) miCiclo.setTieneCharlaPosterior(checkCharla.isSelected());
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