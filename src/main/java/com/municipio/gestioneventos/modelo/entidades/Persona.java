package com.municipio.gestioneventos.modelo.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(nullable = false, unique = true)
    private String dni;

    private String telefono;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    public Persona() {}

    public Persona(String nombreCompleto, String dni, String telefono, String correoElectronico) {
        validarNombre(nombreCompleto);
        validarDni(dni);
        validarTelefono(telefono);
        validarEmail(correoElectronico);
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
    }

    // Validaciones de negocio en el modelo
    public static void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre completo es obligatorio.");
        if (nombre.trim().length() < 3)
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres.");
    }

    public static void validarDni(String dni) {
        if (dni == null || dni.trim().isEmpty())
            throw new IllegalArgumentException("El DNI es obligatorio.");
        if (!dni.matches("\\d{7,8}"))
            throw new IllegalArgumentException("El DNI debe contener solo números, sin puntos (7 u 8 dígitos). Ej: 12345678");
    }

    public static void validarEmail(String email) {
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        if (!email.contains("@") || !email.contains("."))
            throw new IllegalArgumentException("El formato del correo es incorrecto. Ej: nombre@dominio.com");
    }

    public static void validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty())
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        if (!telefono.matches("[+\\d]+"))
            throw new IllegalArgumentException("El teléfono debe contener solo números. Puede comenzar con +. Ej: +5493764123456");
    }

    public String obtenerDatosContacto() {
        return "Nombre: " + nombreCompleto +
                " | DNI: " + dni +
                " | Tel: " + telefono +
                " | Email: " + correoElectronico;
    }

    public Long getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) {
        validarNombre(nombreCompleto);
        this.nombreCompleto = nombreCompleto;
    }
    public String getDni() { return dni; }
    public void setDni(String dni) {
        validarDni(dni);
        this.dni = dni;
    }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) {
        validarTelefono(telefono);
        this.telefono = telefono;
    }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) {
        validarEmail(correoElectronico);
        this.correoElectronico = correoElectronico;
    }
}