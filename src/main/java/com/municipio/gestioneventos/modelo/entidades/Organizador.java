package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizadores")
public class Organizador extends Persona {

    @ManyToMany(mappedBy = "organizadores")
    private List<Evento> eventosAsignados = new ArrayList<>();

    public Organizador() {}

    public void confirmarEvento(Evento e) {
        e.cambiarEstado("confirmado");
        eventosAsignados.add(e);
    }

    public List<Evento> getEventosAsignados() { return eventosAsignados; }
}