package com.municipio.gestioneventos.modelo.entidades;

public class Exposicion extends Evento {

    private String tipoArte;
    private Curador curador;

    public Exposicion() {}

    public String getTipoArte() { return tipoArte; }
    public void setTipoArte(String tipoArte) { this.tipoArte = tipoArte; }
    public Curador getCurador() { return curador; }
    public void setCurador(Curador curador) { this.curador = curador; }
}