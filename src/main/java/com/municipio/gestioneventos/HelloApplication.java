package com.municipio.gestioneventos;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    // Variable global estática por si necesitas acceder a la conexión desde tus controladores luego
    public static EntityManagerFactory emf;

    @Override
    public void start(Stage stage) throws IOException {

        // --- 1. INICIALIZAR LA BASE DE DATOS ---
        System.out.println("Iniciando conexión con la Base de Datos...");
        try {
            // "GestionEventosPU" debe coincidir con el nombre en tu persistence.xml
            emf = Persistence.createEntityManagerFactory("GestionEventosPU");
            System.out.println("=========================================");
            System.out.println("¡CONEXIÓN EXITOSA Y TABLAS VERIFICADAS!");
            System.out.println("=========================================");
        } catch (Exception e) {
            System.err.println("Error crítico al conectar con la base de datos:");
            e.printStackTrace();
        }

        // --- 2. CARGAR LA INTERFAZ GRÁFICA ---
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("main-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("Gestión de Eventos Municipales");
        stage.setScene(scene);
        stage.show();
    }

    // Este método se ejecuta cuando se cierra la aplicación
    @Override
    public void stop() throws Exception {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("Conexión a la base de datos cerrada correctamente.");
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}