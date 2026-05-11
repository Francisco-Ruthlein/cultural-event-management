package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conciertos")
public class Concierto extends Evento {

    @ManyToMany
    @JoinTable(
            name = "concierto_artista",
            joinColumns = @JoinColumn(name = "concierto_id"),
            inverseJoinColumns = @JoinColumn(name = "artista_id")
    )
    private List<Artista> artistas = new ArrayList<>();

    @Column(name = "es_entrada_gratuita")
    private boolean esEntradaGratuita;

    public Concierto() {}

    public List<Artista> getArtistas() { return artistas; }
    public boolean isEsEntradaGratuita() { return esEntradaGratuita; }
    public void setEsEntradaGratuita(boolean esEntradaGratuita) { this.esEntradaGratuita = esEntradaGratuita; }
}