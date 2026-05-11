package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "curadores")
public class Curador extends Persona {

    @Column(name = "especialidad_arte")
    private String especialidadArte;

    public Curador() {}

    public void evaluarExposicion(Exposicion e) {
        System.out.println("Evaluando exposición: " + e.getNombre());
    }

    public String getEspecialidadArte() { return especialidadArte; }
    public void setEspecialidadArte(String especialidadArte) { this.especialidadArte = especialidadArte; }
}