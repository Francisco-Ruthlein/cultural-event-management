package com.municipio.gestioneventos.modelo.entidades;

import java.util.ArrayList;
import java.util.List;

public class Concierto extends Evento {

    private List<Artista> artistas = new ArrayList<>();
    private boolean esEntradaGratuita;

    public Concierto() {}

    public List<Artista> getArtistas() { return artistas; }
    public boolean isEsEntradaGratuita() { return esEntradaGratuita; }
    public void setEsEntradaGratuita(boolean esEntradaGratuita) { this.esEntradaGratuita = esEntradaGratuita; }
}