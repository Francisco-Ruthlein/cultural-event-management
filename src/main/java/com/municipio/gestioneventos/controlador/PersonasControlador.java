package com.municipio.gestioneventos.controlador;

import com.municipio.gestioneventos.modelo.entidades.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PersonasControlador {

    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, String> colNombre;
    @FXML private TableColumn<Persona, String> colDni;
    @FXML private TableColumn<Persona, String> colTelefono;
    @FXML private TableColumn<Persona, String> colEmail;
    @FXML private TableColumn<Persona, String> colRol;
    @FXML private TextField campoBusqueda;

    private GestorEventos gestor =  GestorEventos.getInstancia();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getNombreCompleto()));
        colDni.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDni()));
        colTelefono.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getTelefono()));
        colEmail.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCorreoElectronico()));
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
    public void nuevaPersona() {
        System.out.println("Nueva persona");
    }

    @FXML
    public void buscarPersona() {
        System.out.println("Buscando: " + campoBusqueda.getText());
    }

    @FXML
    public void editarPersona() {
        Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            System.out.println("Editando: " + seleccionada.getNombreCompleto());
        }
    }

    @FXML
    public void eliminarPersona() {
        Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            gestor.eliminarPersona(seleccionada.getId());
            cargarPersonas();
        }
    }
}