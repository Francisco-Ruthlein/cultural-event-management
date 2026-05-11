package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventos")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "duracion_estimada")
    private int duracionEstimada;

    @Column(nullable = false)
    private String estado;

    @ManyToMany
    @JoinTable(
            name = "evento_organizador",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "organizador_id")
    )
    private List<Organizador> organizadores = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "evento_participante",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "participante_id")
    )
    private List<Participante> participantes = new ArrayList<>();

    public Evento() {}

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public boolean registrarParticipante(Participante p) {
        if (!this.estado.equals("confirmado")) {
            return false;
        }
        participantes.add(p);
        return true;
    }

    public List<Participante> listarParticipantes() {
        return participantes;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public int getDuracionEstimada() { return duracionEstimada; }
    public void setDuracionEstimada(int duracionEstimada) { this.duracionEstimada = duracionEstimada; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<Organizador> getOrganizadores() { return organizadores; }
    public List<Participante> getParticipantes() { return participantes; }
}