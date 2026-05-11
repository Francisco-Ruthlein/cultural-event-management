package com.municipio.gestioneventos.modelo.entidades;

public abstract class Persona {

    private String nombreCompleto;
    private String dni;
    private String telefono;
    private String correoElectronico;

    public Persona() {}

    public String obtenerDatosContacto() {
        return "Nombre: " + nombreCompleto +
                " | DNI: " + dni +
                " | Tel: " + telefono +
                " | Email: " + correoElectronico;
    }

    // Getters y Setters
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
}