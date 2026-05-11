package com.municipio.gestioneventos.modelo.entidades;

import java.util.ArrayList;
import java.util.List;

public class CicloDeCine extends Evento {

    private String ordenProyeccion;
    private boolean tieneCharlaPosterior;
    private List<Pelicula> peliculas = new ArrayList<>();

    public CicloDeCine() {}

    public String getOrdenProyeccion() { return ordenProyeccion; }
    public void setOrdenProyeccion(String ordenProyeccion) { this.ordenProyeccion = ordenProyeccion; }
    public boolean isTieneCharlaPosterior() { return tieneCharlaPosterior; }
    public void setTieneCharlaPosterior(boolean tieneCharlaPosterior) { this.tieneCharlaPosterior = tieneCharlaPosterior; }
    public List<Pelicula> getPeliculas() { return peliculas; }
}