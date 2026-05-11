package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "ferias")
public class Feria extends Evento {

    @Column(name = "cantidad_stands")
    private int cantidadStands;

    @Column(name = "es_techada")
    private boolean esTechada;

    public Feria() {}

    public int getCantidadStands() { return cantidadStands; }
    public void setCantidadStands(int cantidadStands) { this.cantidadStands = cantidadStands; }
    public boolean isEsTechada() { return esTechada; }
    public void setEsTechada(boolean esTechada) { this.esTechada = esTechada; }
}