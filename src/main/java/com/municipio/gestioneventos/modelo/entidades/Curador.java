package com.municipio.gestioneventos.modelo.entidades;

public class Curador extends Persona {

    private String especialidadArte;

    public Curador() {}

    public void evaluarExposicion(Exposicion e) {
        System.out.println("Evaluando exposición: " + e.getNombre());
    }

    public String getEspecialidadArte() { return especialidadArte; }
    public void setEspecialidadArte(String especialidadArte) { this.especialidadArte = especialidadArte; }
}