package com.municipio.gestioneventos.modelo.entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Evento {

    private String nombre;
    private LocalDate fechaInicio;
    private int duracionEstimada;
    private String estado;
    private List<Organizador> organizadores = new ArrayList<>();
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
