package com.municipio.gestioneventos.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import com.municipio.gestioneventos.modelo.entidades.GestorEventos;
import com.municipio.gestioneventos.modelo.entidades.Evento;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.scene.control.ScrollPane;

public class MainControlador {

    @FXML private StackPane contenidoCentral;
    @FXML private Label headerTitulo;
    @FXML private Label headerSubtitulo;
    @FXML private Button btnInicio;
    @FXML private Button btnEventos;
    @FXML private Button btnPersonas;
    @FXML private Button btnInscripciones;
    @FXML private Button btnCalendario;

    private static final String ESTILO_ACTIVO =
            "-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: white; " +
                    "-fx-font-size: 13px; -fx-padding: 10 12; -fx-background-radius: 6; " +
                    "-fx-cursor: hand; -fx-alignment: CENTER_LEFT;";
    private static final String ESTILO_INACTIVO =
            "-fx-background-color: transparent; -fx-text-fill: rgba(255,255,255,0.7); " +
                    "-fx-font-size: 13px; -fx-padding: 10 12; -fx-background-radius: 6; " +
                    "-fx-cursor: hand; -fx-alignment: CENTER_LEFT;";

    @FXML
    private GestorEventos gestor = GestorEventos.getInstancia();
    public void mostrarInicio() {
        marcarActivo(btnInicio);
        headerTitulo.setText("Panel principal");
        headerSubtitulo.setText("Resumen general del sistema");
        contenidoCentral.getChildren().clear();

        // Obtener datos reales
        List<Evento> eventos = gestor.listarEventos();
        long confirmados  = eventos.stream().filter(e -> e.getEstado().equals("Confirmado")).count();
        long planificados = eventos.stream().filter(e -> e.getEstado().equals("En planificación")).count();
        long finalizados  = eventos.stream().filter(e -> e.getEstado().equals("Finalizado")).count();
        long personas     = gestor.listarPersonas().size();

        VBox contenido = new VBox(20);
        contenido.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        contenido.setStyle("-fx-padding: 30 40;");

        // Título de bienvenida
        Label titulo = new Label("Bienvenido al sistema de Gestión de Eventos");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #185FA5;");

        Label subtitulo = new Label("Municipalidad — resumen del estado actual");
        subtitulo.setStyle("-fx-font-size: 13px; -fx-text-fill: gray;");

        // Tarjetas de resumen
        HBox tarjetas = new HBox(16);
        tarjetas.setAlignment(javafx.geometry.Pos.CENTER);
        tarjetas.getChildren().addAll(
                crearTarjeta("Total eventos",    String.valueOf(eventos.size()), "#185FA5", "📅"),
                crearTarjeta("Confirmados",       String.valueOf(confirmados),    "#2e7d32", "✅"),
                crearTarjeta("En planificación",  String.valueOf(planificados),   "#e65100", "📝"),
                crearTarjeta("Finalizados",       String.valueOf(finalizados),    "#555555", "🏁"),
                crearTarjeta("Personas",          String.valueOf(personas),       "#6a1b9a", "👥")
        );

        // Próximos eventos
        Label lblProximos = new Label("Próximos eventos confirmados");
        lblProximos.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        VBox listaProximos = new VBox(6);
        java.time.LocalDate hoy = java.time.LocalDate.now();
        eventos.stream()
                .filter(e -> e.getEstado().equals("Confirmado")
                        && e.getFechaInicio() != null
                        && !e.getFechaInicio().isBefore(hoy))
                .sorted((a, b) -> a.getFechaInicio().compareTo(b.getFechaInicio()))
                .limit(5)
                .forEach(e -> {
                    HBox fila = new HBox(12);
                    fila.setStyle("-fx-background-color: white; -fx-padding: 10 16; " +
                            "-fx-background-radius: 8; -fx-border-color: #e0e0e0; " +
                            "-fx-border-radius: 8;");
                    fila.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    Label fecha = new Label(e.getFechaInicio().toString());
                    fecha.setStyle("-fx-text-fill: #185FA5; -fx-font-weight: bold; -fx-min-width: 100;");
                    Label nombre = new Label(e.getNombre());
                    nombre.setStyle("-fx-font-size: 13px; -fx-min-width: 180;");
                    Label tipo = new Label(e.getClass().getSimpleName());
                    tipo.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");
                    fila.getChildren().addAll(fecha, nombre, tipo);
                    listaProximos.getChildren().add(fila);
                });

        if (listaProximos.getChildren().isEmpty()) {
            Label sinEventos = new Label("No hay eventos confirmados próximos.");
            sinEventos.setStyle("-fx-text-fill: gray; -fx-font-size: 13px;");
            listaProximos.getChildren().add(sinEventos);
        }

        ScrollPane scroll = new ScrollPane(listaProximos);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scroll, javafx.scene.layout.Priority.ALWAYS);

        contenido.getChildren().addAll(titulo, subtitulo, tarjetas, lblProximos, scroll);
        contenidoCentral.getChildren().add(contenido);
    }

    private javafx.scene.layout.VBox crearTarjeta(String titulo, String valor, String color, String icono) {
        VBox tarjeta = new VBox(6);
        tarjeta.setAlignment(javafx.geometry.Pos.CENTER);
        tarjeta.setStyle("-fx-background-color: white; -fx-padding: 20 24; " +
                "-fx-background-radius: 10; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 10; -fx-min-width: 130;");

        Label ico = new Label(icono);
        ico.setStyle("-fx-font-size: 22px;");

        Label num = new Label(valor);
        num.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label lbl = new Label(titulo);
        lbl.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");
        lbl.setWrapText(true);
        lbl.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        tarjeta.getChildren().addAll(ico, num, lbl);
        return tarjeta;
    }

    @FXML
    public void mostrarEventos() {
        marcarActivo(btnEventos);
        headerTitulo.setText("Eventos");
        headerSubtitulo.setText("Gestión de eventos culturales");
        cargarVista("eventos-view.fxml");
    }

    @FXML
    public void mostrarPersonas() {
        marcarActivo(btnPersonas);
        headerTitulo.setText("Personas");
        headerSubtitulo.setText("Gestión de personas del sistema");
        cargarVista("personas-view.fxml");
    }

    @FXML
    public void mostrarInscripciones() {
        marcarActivo(btnInscripciones);
        headerTitulo.setText("Inscripciones");
        headerSubtitulo.setText("Registro de participantes en eventos");
        cargarVista("inscripciones-view.fxml");
    }

    @FXML
    public void mostrarCalendario() {
        marcarActivo(btnCalendario);
        headerTitulo.setText("Calendario");
        headerSubtitulo.setText("Visualización de eventos por fecha");
        cargarVista("calendario-view.fxml");
    }

    @FXML
    public void nuevoEvento() {
        marcarActivo(null); // ninguno activo cuando se abre desde el botón header
        headerTitulo.setText("Nuevo evento");
        headerSubtitulo.setText("Completá los datos del evento");
        cargarVista("nuevo-evento-view.fxml");
    }

    private void marcarActivo(Button botonActivo) {
        for (Button btn : new Button[]{btnInicio, btnEventos, btnPersonas, btnInscripciones, btnCalendario}) {
            btn.setStyle(btn == botonActivo ? ESTILO_ACTIVO : ESTILO_INACTIVO);
        }
    }

    private void cargarVista(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/municipio/gestioneventos/" + fxml)
            );
            contenidoCentral.getChildren().clear();
            contenidoCentral.getChildren().add(loader.load());
        } catch (IOException e) {
            Label error = new Label("Vista en construcción...");
            error.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
            contenidoCentral.getChildren().clear();
            contenidoCentral.getChildren().add(error);
        }
    }
}