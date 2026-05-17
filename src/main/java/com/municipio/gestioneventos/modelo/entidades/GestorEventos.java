package com.municipio.gestioneventos.modelo.entidades;

import com.municipio.gestioneventos.modelo.entidades.Evento;
import com.municipio.gestioneventos.modelo.entidades.Persona;
import jakarta.persistence.*;
import java.util.List;

public class GestorEventos {

    private static GestorEventos instancia;
    private EntityManagerFactory emf;
    private EntityManager em;

    private GestorEventos() {
        emf = Persistence.createEntityManagerFactory("gestion-eventos-pu");
        em = emf.createEntityManager();
    }

    public static GestorEventos getInstancia() {
        if (instancia == null) {
            instancia = new GestorEventos();
        }
        return instancia;
    }

    public void guardar(Object entidad) {
        em.getTransaction().begin();
        em.persist(entidad);
        em.getTransaction().commit();
    }

    public List<Evento> listarEventos() {
        return em.createQuery("SELECT e FROM Evento e", Evento.class).getResultList();
    }

    public List<Persona> listarPersonas() {
        return em.createQuery("SELECT p FROM Persona p", Persona.class).getResultList();
    }

    public void actualizar(Object entidad) {
        em.getTransaction().begin();
        em.merge(entidad);
        em.getTransaction().commit();
    }

    public void eliminarEvento(Long id) {
        em.getTransaction().begin();
        Evento e = em.find(Evento.class, id);
        if (e != null) em.remove(e);
        em.getTransaction().commit();
    }

    public void eliminarPersona(Long id) {
        em.getTransaction().begin();
        Persona p = em.find(Persona.class, id);
        if (p != null) em.remove(p);
        em.getTransaction().commit();
    }

    public Evento buscarEvento(Long id) {
        return em.find(Evento.class, id);
    }

    public void cerrar() {
        em.close();
        emf.close();
    }
}