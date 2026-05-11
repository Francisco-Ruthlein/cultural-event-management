package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "participantes")
public class Participante extends Persona {

    @ManyToMany(mappedBy = "participantes")
    private List<Evento> historialEventos = new ArrayList<>();

    public Participante() {}

    public boolean solicitarInscripcion(Evento e) {
        return e.registrarParticipante(this);
    }

    public List<Evento> getHistorialEventos() { return historialEventos; }
}