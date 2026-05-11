package com.municipio.gestioneventos.controlador;

import com.municipio.gestioneventos.modelo.entidades.*;
import javafx.fxml.FXML;
import java.util.List;

public class PersonaControlador {

    private GestorEventos gestor = new GestorEventos();

    // CREAR persona
    @FXML
    public void crearPersona(Persona persona) {
        gestor.guardar(persona);
    }

    // LISTAR personas
    @FXML
    public List<Persona> listarPersonas() {
        return gestor.listarPersonas();
    }

    // ELIMINAR persona
    @FXML
    public void eliminarPersona(Long id) {
        gestor.eliminarPersona(id);
    }

    // ACTUALIZAR persona
    @FXML
    public void actualizarPersona(Persona persona) {
        gestor.actualizar(persona);
    }
}