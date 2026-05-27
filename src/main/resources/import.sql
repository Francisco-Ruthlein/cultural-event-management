/* =========================
   PERSONAS
========================= */

INSERT INTO personas (id, nombre_completo, dni, correo_electronico, telefono) VALUES
                                                                                  (1, 'Juan Perez', '40111222', 'juan@gmail.com', '3764123456'),
                                                                                  (2, 'Maria Lopez', '38999111', 'maria@gmail.com', '3764556677'),
                                                                                  (3, 'Carlos Gomez', '41222333', 'carlos@gmail.com', '3764778899'),
                                                                                  (4, 'Ana Rodriguez', '39888777', 'ana@gmail.com', '3764332211'),
                                                                                  (5, 'Lucia Fernandez', '42333444', 'lucia@gmail.com', '3764998877'),
                                                                                  (6, 'Pedro Silva', '37777111', 'pedro@gmail.com', '3764551122');


/* =========================
   PARTICIPANTES
========================= */

INSERT INTO participantes (id) VALUES
                                   (1),
                                   (2),
                                   (5);


/* =========================
   ORGANIZADORES
========================= */

INSERT INTO organizadores (id) VALUES
                                   (3),
                                   (4);


/* =========================
   ARTISTAS
========================= */

INSERT INTO artistas (id, genero_musical) VALUES
    (6, 'Rock Nacional');


/* =========================
   CURADORES
========================= */

INSERT INTO curadores (id, especialidad_arte) VALUES
    (4, 'Arte Contemporaneo');


/* =========================
   INSTRUCTORES
========================= */

INSERT INTO instructores (id, area_especializacion) VALUES
    (3, 'Programacion Java');


/* =========================
   EVENTOS
========================= */

INSERT INTO eventos (id, nombre, fecha_inicio, duracion_estimada, estado) VALUES
                                                                              (1, 'Festival Cultural', '2026-06-10', 5, 'ACTIVO'),
                                                                              (2, 'Concierto de Rock', '2026-07-01', 3, 'ACTIVO'),
                                                                              (3, 'Expo Arte Regional', '2026-07-15', 7, 'PENDIENTE'),
                                                                              (4, 'Taller Java', '2026-08-01', 2, 'ACTIVO'),
                                                                              (5, 'Ciclo Cine Clasico', '2026-09-10', 4, 'FINALIZADO');


/* =========================
   FERIAS
========================= */

INSERT INTO ferias (id, cantidad_stands, es_techada) VALUES
    (1, 25, true);


/* =========================
   CONCIERTOS
========================= */

INSERT INTO conciertos (id, es_entrada_gratuita) VALUES
    (2, false);


/* =========================
   EXPOSICIONES
========================= */

INSERT INTO exposiciones (id, tipo_arte, curador_id) VALUES
    (3, 'Pintura', 4);


/* =========================
   TALLERES
========================= */

INSERT INTO talleres (id, cupo_maximo, modalidad, instructor_id) VALUES
    (4, 30, 'Presencial', 3);


/* =========================
   CICLOS DE CINE
========================= */

INSERT INTO ciclos_de_cine (id, orden_proyeccion, tiene_charla_posterior) VALUES
    (5, 'Cronologico', true);


/* =========================
   PELICULAS
========================= */

INSERT INTO peliculas (id, titulo, director, duracion_minutos, ciclo_id) VALUES
                                                                             (1, 'Cinema Paradiso', 'Giuseppe Tornatore', 124, 5),
                                                                             (2, 'El Padrino', 'Francis Ford Coppola', 175, 5);


/* =========================
   RELACIONES EVENTO-ORGANIZADOR
========================= */

INSERT INTO evento_organizador (evento_id, organizador_id) VALUES
                                                               (1, 3),
                                                               (2, 4),
                                                               (3, 3),
                                                               (4, 4);


/* =========================
   RELACIONES EVENTO-PARTICIPANTE
========================= */

INSERT INTO evento_participante (evento_id, participante_id) VALUES
                                                                 (1, 1),
                                                                 (1, 2),
                                                                 (2, 5),
                                                                 (4, 1);


/* =========================
   RELACIONES CONCIERTO-ARTISTA
========================= */

INSERT INTO concierto_artista (concierto_id, artista_id) VALUES
    (2, 6);