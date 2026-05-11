package com.municipio.gestioneventos.modelo.entidades;

import java.util.ArrayList;
import java.util.List;

public class Organizador extends Persona {

    private List<Evento> eventosAsignados = new ArrayList<>();

    public Organizador() {}

    public void confirmarEvento(Evento e) {
        e.cambiarEstado("confirmado");
        eventosAsignados.add(e);
    }

    public List<Evento> getEventosAsignados() { return eventosAsignados; }
}