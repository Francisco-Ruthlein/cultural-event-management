package com.municipio.gestioneventos.modelo.entidades;

import java.util.ArrayList;
import java.util.List;

public class Participante extends Persona {

    private List<Evento> historialEventos = new ArrayList<>();

    public Participante() {}

    public boolean solicitarInscripcion(Evento e) {
        return e.registrarParticipante(this);
    }

    public List<Evento> getHistorialEventos() { return historialEventos; }
}