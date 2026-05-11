package com.municipio.gestioneventos.modelo.entidades;

public class Artista extends Persona {

    private String generoMusical;

    public Artista() {}

    public String obtenerRepertorio() {
        return "Artista: " + getNombreCompleto() + " | Género: " + generoMusical;
    }

    public String getGeneroMusical() { return generoMusical; }
    public void setGeneroMusical(String generoMusical) { this.generoMusical = generoMusical; }
}