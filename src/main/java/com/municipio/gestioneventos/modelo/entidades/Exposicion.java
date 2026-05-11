package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "exposiciones")
public class Exposicion extends Evento {

    @Column(name = "tipo_arte")
    private String tipoArte;

    @ManyToOne
    @JoinColumn(name = "curador_id")
    private Curador curador;

    public Exposicion() {}

    public String getTipoArte() { return tipoArte; }
    public void setTipoArte(String tipoArte) { this.tipoArte = tipoArte; }
    public Curador getCurador() { return curador; }
    public void setCurador(Curador curador) { this.curador = curador; }
}