package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "instructores")
public class Instructor extends Persona {

    @Column(name = "area_especializacion")
    private String areaEspecializacion;

    public Instructor() {}

    public void dictarTaller(Taller t) {
        System.out.println("Dictando taller: " + t.getNombre());
    }

    public String getAreaEspecializacion() { return areaEspecializacion; }
    public void setAreaEspecializacion(String area) { this.areaEspecializacion = area; }
}