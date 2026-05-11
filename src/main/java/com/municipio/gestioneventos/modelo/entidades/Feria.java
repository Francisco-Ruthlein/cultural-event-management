package com.municipio.gestioneventos.modelo.entidades;

public class Feria extends Evento {

    private int cantidadStands;
    private boolean esTechada;

    public Feria() {}

    public int getCantidadStands() { return cantidadStands; }
    public void setCantidadStands(int cantidadStands) { this.cantidadStands = cantidadStands; }
    public boolean isEsTechada() { return esTechada; }
    public void setEsTechada(boolean esTechada) { this.esTechada = esTechada; }
}