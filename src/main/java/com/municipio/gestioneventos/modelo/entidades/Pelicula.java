package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "peliculas")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String director;

    @Column(name = "duracion_minutos")
    private int duracionMinutos;

    public Pelicula() {}

    public String getInfoPelicula() {
        return "Título: " + titulo + " | Director: " + director + " | Duración: " + duracionMinutos + " min";
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }
}