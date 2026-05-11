package com.municipio.gestioneventos.modelo.entidades;

public class Instructor extends Persona {

    private String areaEspecializacion;

    public Instructor() {}

    public void dictarTaller(Taller t) {
        System.out.println("Dictando taller: " + t.getNombre());
    }

    public String getAreaEspecializacion() { return areaEspecializacion; }
    public void setAreaEspecializacion(String area) { this.areaEspecializacion = area; }
}