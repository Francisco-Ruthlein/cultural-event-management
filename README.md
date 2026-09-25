# 🎭 Sistema de Gestión de Eventos Municipales

Sistema de escritorio desarrollado en Java para gestionar los eventos culturales organizados por un municipio (ferias, exposiciones, conciertos, talleres y ciclos de cine).

## 🛠️ Tecnologías

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 17 |
| Interfaz gráfica | JavaFX 21.0.6 |
| Persistencia | JPA 3.1 + Hibernate 6.4 |
| Base de datos | PostgreSQL |
| Build | Maven |
| IDE | IntelliJ IDEA Ultimate |

## 📐 Arquitectura

El sistema aplica el patrón **MVC (Modelo-Vista-Controlador)**:

- **Modelo** → Entidades del dominio con lógica de negocio (modelo rico)
- **Vista** → Archivos `.fxml` con la interfaz gráfica en JavaFX
- **Controlador** → Clases Java con `@FXML` que conectan la vista con el modelo

## 🗂️ Estructura del proyecto

```
src/main/java/com/municipio/gestioneventos/
├── controlador/
│   ├── MainControlador.java
│   ├── EventosControlador.java
│   ├── NuevoEventoControlador.java
│   ├── PersonasControlador.java
│   ├── InscripcionesControlador.java
│   ├── CalendarioControlador.java
│   └── EventoControlador.java
├── modelo/
│   └── entidades/
│       ├── Evento.java          ← Clase abstracta
│       ├── Feria.java
│       ├── Concierto.java
│       ├── Exposicion.java
│       ├── Taller.java
│       ├── CicloDeCine.java
│       ├── Pelicula.java
│       ├── Persona.java         ← Clase abstracta
│       ├── Participante.java
│       ├── Organizador.java
│       ├── Artista.java
│       ├── Curador.java
│       ├── Instructor.java
│       └── GestorEventos.java   ← Patrón Singleton
├── HelloApplication.java
└── Launcher.java

src/main/resources/com/municipio/gestioneventos/
├── main-view.fxml
├── eventos-view.fxml
├── nuevo-evento-view.fxml
├── personas-view.fxml
├── inscripciones-view.fxml
└── calendario-view.fxml
```

## 🧩 Conceptos de POO aplicados

### Herencia y clases abstractas
`Evento` y `Persona` son clases abstractas. No se pueden instanciar directamente, solo sus subclases concretas.

```
Evento (abstracta)
├── Feria
├── Concierto
├── Exposicion
├── Taller
└── CicloDeCine

Persona (abstracta)
├── Participante
├── Organizador
├── Artista
├── Curador
└── Instructor
```

### Composición
`CicloDeCine` contiene una lista de `Pelicula` con relación de composición fuerte: si se elimina el ciclo, se eliminan sus películas.

### Polimorfismo
`Taller` sobreescribe `registrarParticipante()` para agregar validación de cupo máximo sobre el comportamiento base de `Evento`.

### Modelo Rico
Las clases tienen comportamiento propio. Por ejemplo, `cambiarEstado()` en `Evento` valida que las transiciones sean lógicas:

```
En planificación → Confirmado → En ejecución → Finalizado
```

No se puede saltear ni retroceder un estado.

### Patrón Singleton
`GestorEventos` mantiene una única instancia del `EntityManager` para toda la aplicación, evitando conexiones redundantes a la base de datos.

## 🚀 Cómo ejecutar el proyecto

### Requisitos previos
- JDK 17 para Windows
- PostgreSQL instalado y corriendo
- IntelliJ IDEA (recomendado)

### Configuración de la base de datos
1. Crear una base de datos llamada `gestion_eventos_db` en PostgreSQL
2. Crear el archivo `src/main/resources/META-INF/persistence.xml` con tus credenciales:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="3.0"
             xmlns="https://jakarta.ee/xml/ns/persistence">
    <persistence-unit name="gestion-eventos-pu" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        <properties>
            <property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/gestion_eventos_db"/>
            <property name="jakarta.persistence.jdbc.user" value="postgres"/>
            <property name="jakarta.persistence.jdbc.password" value="TU_CONTRASEÑA"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```

> ⚠️ El archivo `persistence.xml` está en `.gitignore` por seguridad. Cada desarrollador debe crearlo localmente con sus propias credenciales.

### Ejecución
1. Clonar el repositorio
2. Abrir el proyecto en IntelliJ IDEA desde Windows (no desde WSL)
3. Maven descarga las dependencias automáticamente
4. Configurar el JDK 17 de Windows en Project Structure
5. Ejecutar `HelloApplication`

## 📋 Funcionalidades

- ✅ Alta, modificación y baja de eventos culturales
- ✅ Gestión del ciclo de vida del evento (estados)
- ✅ Registro de participantes por evento
- ✅ Listado y búsqueda de eventos
- ✅ Vista de inscripciones por evento
- ✅ Calendario de eventos con filtros por mes y estado
- ✅ Validaciones de negocio (fechas, cupo, transiciones de estado)


## 👥 Autores

- Francisco Martín Ruthlein
- Gonzalo Aquino
