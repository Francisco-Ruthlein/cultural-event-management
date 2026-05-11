package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "artistas")
public class Artista extends Persona {

    @Column(name = "genero_musical")
    private String generoMusical;

    public Artista() {}

    public String obtenerRepertorio() {
        return "Artista: " + getNombreCompleto() + " | Género: " + generoMusical;
    }

    public String getGeneroMusical() { return generoMusical; }
    public void setGeneroMusical(String generoMusical) { this.generoMusical = generoMusical; }
}