package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;

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
        if (!getEstado().equals("confirmado")) {
            return false;
        }
        if (listarParticipantes().size() >= cupoMaximo) {
            return false;
        }
        listarParticipantes().add(p);
        return true;
    }

    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }
}