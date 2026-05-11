package com.municipio.gestioneventos.modelo.entidades;

public class Pelicula {

    private String titulo;
    private String director;
    private int duracionMinutos;

    public Pelicula() {}

    public String getInfoPelicula() {
        return "Título: " + titulo + " | Director: " + director + " | Duración: " + duracionMinutos + " min";
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }
}