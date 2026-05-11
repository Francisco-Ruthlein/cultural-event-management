package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ciclos_de_cine")
public class CicloDeCine extends Evento {

    @Column(name = "orden_proyeccion")
    private String ordenProyeccion;

    @Column(name = "tiene_charla_posterior")
    private boolean tieneCharlaPosterior;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ciclo_id")
    private List<Pelicula> peliculas = new ArrayList<>();

    public CicloDeCine() {}

    public String getOrdenProyeccion() { return ordenProyeccion; }
    public void setOrdenProyeccion(String ordenProyeccion) { this.ordenProyeccion = ordenProyeccion; }
    public boolean isTieneCharlaPosterior() { return tieneCharlaPosterior; }
    public void setTieneCharlaPosterior(boolean tieneCharlaPosterior) { this.tieneCharlaPosterior = tieneCharlaPosterior; }
    public List<Pelicula> getPeliculas() { return peliculas; }
}