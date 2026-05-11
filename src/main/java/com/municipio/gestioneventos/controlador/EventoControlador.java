package com.municipio.gestioneventos.controlador;

import com.municipio.gestioneventos.modelo.entidades.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class EventoControlador {

    private GestorEventos gestor = new GestorEventos();

    // CREAR evento
    @FXML
    public void crearEvento(Evento evento) {
        gestor.guardar(evento);
    }

    // LISTAR eventos
    @FXML
    public List<Evento> listarEventos() {
        return gestor.listarEventos();
    }

    // CAMBIAR estado
    @FXML
    public void cambiarEstadoEvento(Long id, String nuevoEstado) {
        Evento e = gestor.buscarEvento(id);
        if (e != null) {
            e.cambiarEstado(nuevoEstado);
            gestor.actualizar(e);
        }
    }

    // ELIMINAR evento
    @FXML
    public void eliminarEvento(Long id) {
        gestor.eliminarEvento(id);
    }

    // REGISTRAR participante
    @FXML
    public void registrarParticipante(Long eventoId, Participante p) {
        Evento e = gestor.buscarEvento(eventoId);
        if (e != null) {
            e.registrarParticipante(p);
            gestor.actualizar(e);
        }
    }

    // LISTAR participantes de un evento
    @FXML
    public List<Participante> listarParticipantes(Long eventoId) {
        Evento e = gestor.buscarEvento(eventoId);
        if (e != null) {
            return e.listarParticipantes();
        }
        return null;
    }
}