package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "talleres")
public class Taller extends Evento {

    @Column(name = "cupo_maximo")
    private int cupoMaximo;

    private String modalidad;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    public Taller() {}

    @Override
    public boolean registrarParticipante(Participante p) {
        if (!getEstado().equals("Confirmado")) {
            return false;
        }
        List<Participante> lista = getParticipantes();
        if (cupoMaximo > 0 && lista.size() >= cupoMaximo) {
            return false;
        }
        if (lista.contains(p)) {
            return false; // evitar duplicados
        }
        lista.add(p);
        return true;
    }

    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }
}