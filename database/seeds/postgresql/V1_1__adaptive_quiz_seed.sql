-- AdaptiveQuiz - Seed demostrativo
-- PostgreSQL 17

INSERT INTO CAT_CATALOGO (codigo,nombre,descripcion) VALUES
('DIFICULTAD','Dificultad','Niveles de dificultad'),
('TIPO_EJERCICIO','Tipo de ejercicio','Modalidades de ejercicio'),
('ESTADO_EJERCICIO','Estado de ejercicio','Ciclo del ejercicio'),
('ESTADO_SESION','Estado de sesión','Ciclo de sesión'),
('RESULTADO_INTENTO','Resultado de intento','Resultado de resolución'),
('NIVEL_RENDIMIENTO','Nivel de rendimiento','Clasificación del contexto'),
('ACCION_ADAPTACION','Acción de adaptación','Acciones del motor'),
('ORIGEN_DECISION','Origen de decisión','Fuente de la decisión'),
('MODO_ADAPTACION','Modo de adaptación','Estrategia de política'),
('ESTADO_ESTUDIANTE','Estado de estudiante','Estado operativo');

INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'BASICO','Básico',10 FROM CAT_CATALOGO WHERE codigo='DIFICULTAD';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'INTERMEDIO','Intermedio',20 FROM CAT_CATALOGO WHERE codigo='DIFICULTAD';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'AVANZADO','Avanzado',30 FROM CAT_CATALOGO WHERE codigo='DIFICULTAD';

INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'OPCION_UNICA','Opción única',10 FROM CAT_CATALOGO WHERE codigo='TIPO_EJERCICIO';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'VERDADERO_FALSO','Verdadero/Falso',20 FROM CAT_CATALOGO WHERE codigo='TIPO_EJERCICIO';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'RESPUESTA_CORTA','Respuesta corta',30 FROM CAT_CATALOGO WHERE codigo='TIPO_EJERCICIO';

INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'BORRADOR','Borrador',10 FROM CAT_CATALOGO WHERE codigo='ESTADO_EJERCICIO';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'PUBLICADO','Publicado',20 FROM CAT_CATALOGO WHERE codigo='ESTADO_EJERCICIO';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'INACTIVO','Inactivo',30 FROM CAT_CATALOGO WHERE codigo='ESTADO_EJERCICIO';

INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'INICIADA','Iniciada',10 FROM CAT_CATALOGO WHERE codigo='ESTADO_SESION';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'FINALIZADA','Finalizada',20 FROM CAT_CATALOGO WHERE codigo='ESTADO_SESION';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'ABANDONADA','Abandonada',30 FROM CAT_CATALOGO WHERE codigo='ESTADO_SESION';

INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'CORRECTO','Correcto',10 FROM CAT_CATALOGO WHERE codigo='RESULTADO_INTENTO';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'INCORRECTO','Incorrecto',20 FROM CAT_CATALOGO WHERE codigo='RESULTADO_INTENTO';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'OMITIDO','Omitido',30 FROM CAT_CATALOGO WHERE codigo='RESULTADO_INTENTO';

INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'BAJO','Bajo',10 FROM CAT_CATALOGO WHERE codigo='NIVEL_RENDIMIENTO';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'MEDIO','Medio',20 FROM CAT_CATALOGO WHERE codigo='NIVEL_RENDIMIENTO';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'ALTO','Alto',30 FROM CAT_CATALOGO WHERE codigo='NIVEL_RENDIMIENTO';

INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'SUBIR_DIFICULTAD','Subir dificultad',10 FROM CAT_CATALOGO WHERE codigo='ACCION_ADAPTACION';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'MANTENER_DIFICULTAD','Mantener dificultad',20 FROM CAT_CATALOGO WHERE codigo='ACCION_ADAPTACION';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'BAJAR_DIFICULTAD','Bajar dificultad',30 FROM CAT_CATALOGO WHERE codigo='ACCION_ADAPTACION';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'ACTIVAR_PISTA','Activar pista',40 FROM CAT_CATALOGO WHERE codigo='ACCION_ADAPTACION';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'CAMBIAR_TIPO_EJERCICIO','Cambiar tipo de ejercicio',50 FROM CAT_CATALOGO WHERE codigo='ACCION_ADAPTACION';

INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'REGLAS','Reglas',10 FROM CAT_CATALOGO WHERE codigo='ORIGEN_DECISION';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'IA','Inteligencia artificial',20 FROM CAT_CATALOGO WHERE codigo='ORIGEN_DECISION';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'HIBRIDO','Híbrido',30 FROM CAT_CATALOGO WHERE codigo='ORIGEN_DECISION';

INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'REGLAS','Reglas',10 FROM CAT_CATALOGO WHERE codigo='MODO_ADAPTACION';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'IA','IA',20 FROM CAT_CATALOGO WHERE codigo='MODO_ADAPTACION';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'HIBRIDO','Híbrido',30 FROM CAT_CATALOGO WHERE codigo='MODO_ADAPTACION';

INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'ACTIVO','Activo',10 FROM CAT_CATALOGO WHERE codigo='ESTADO_ESTUDIANTE';
INSERT INTO CAT_ITEM_CATALOGO (id_catalogo,codigo,nombre,orden)
SELECT id_catalogo,'INACTIVO','Inactivo',20 FROM CAT_CATALOGO WHERE codigo='ESTADO_ESTUDIANTE';

INSERT INTO CFG_PARAMETRO (codigo,nombre,descripcion,tipo_dato,valor_decimal) VALUES
('PESO_PRECISION','Peso precisión','Peso auxiliar de precisión','DECIMAL',0.60),
('PESO_VELOCIDAD','Peso velocidad','Peso auxiliar de velocidad','DECIMAL',0.25),
('PESO_CONSISTENCIA','Peso consistencia','Peso auxiliar de consistencia','DECIMAL',0.15);

INSERT INTO CFG_PARAMETRO (codigo,nombre,descripcion,tipo_dato,valor_entero) VALUES
('TIEMPO_RAPIDO_MS','Tiempo rápido','Umbral inicial de respuesta rápida','ENTERO',20000),
('RACHA_ERRORES_REFUERZO','Racha de errores','Errores consecutivos para refuerzo','ENTERO',3);

INSERT INTO CFG_POLITICA_ADAPTACION (
    codigo,nombre,descripcion,version_politica,id_item_modo_adaptacion,tamano_ventana_intentos
)
SELECT 'POLITICA_BASE_TALLER','Política base Taller 001',
       'Reglas deterministas para demostración',1,i.id_item_catalogo,5
FROM CAT_ITEM_CATALOGO i JOIN CAT_CATALOGO c ON c.id_catalogo=i.id_catalogo
WHERE c.codigo='MODO_ADAPTACION' AND i.codigo='REGLAS';

-- Reglas
INSERT INTO CFG_REGLA_ADAPTACION (
 id_politica_adaptacion,codigo,nombre,prioridad,racha_errores_min,
 id_item_nivel_rendimiento,id_item_accion_principal,habilitar_pista
)
SELECT p.id_politica_adaptacion,'R_BAJO_RACHA','Bajo por racha de errores',10,3,
       r.id_item_catalogo,a.id_item_catalogo,TRUE
FROM CFG_POLITICA_ADAPTACION p
JOIN CAT_CATALOGO cr ON cr.codigo='NIVEL_RENDIMIENTO'
JOIN CAT_ITEM_CATALOGO r ON r.id_catalogo=cr.id_catalogo AND r.codigo='BAJO'
JOIN CAT_CATALOGO ca ON ca.codigo='ACCION_ADAPTACION'
JOIN CAT_ITEM_CATALOGO a ON a.id_catalogo=ca.id_catalogo AND a.codigo='BAJAR_DIFICULTAD'
WHERE p.codigo='POLITICA_BASE_TALLER' AND p.version_politica=1;

INSERT INTO CFG_REGLA_ADAPTACION (
 id_politica_adaptacion,codigo,nombre,prioridad,porcentaje_acierto_max,
 id_item_nivel_rendimiento,id_item_accion_principal,habilitar_pista
)
SELECT p.id_politica_adaptacion,'R_BAJO_PRECISION','Bajo por precisión',20,0.40,
       r.id_item_catalogo,a.id_item_catalogo,TRUE
FROM CFG_POLITICA_ADAPTACION p
JOIN CAT_CATALOGO cr ON cr.codigo='NIVEL_RENDIMIENTO'
JOIN CAT_ITEM_CATALOGO r ON r.id_catalogo=cr.id_catalogo AND r.codigo='BAJO'
JOIN CAT_CATALOGO ca ON ca.codigo='ACCION_ADAPTACION'
JOIN CAT_ITEM_CATALOGO a ON a.id_catalogo=ca.id_catalogo AND a.codigo='BAJAR_DIFICULTAD'
WHERE p.codigo='POLITICA_BASE_TALLER' AND p.version_politica=1;

INSERT INTO CFG_REGLA_ADAPTACION (
 id_politica_adaptacion,codigo,nombre,prioridad,porcentaje_acierto_min,tiempo_promedio_max_ms,
 id_item_nivel_rendimiento,id_item_accion_principal,habilitar_pista
)
SELECT p.id_politica_adaptacion,'R_ALTO','Alto rendimiento',30,0.80,20000,
       r.id_item_catalogo,a.id_item_catalogo,FALSE
FROM CFG_POLITICA_ADAPTACION p
JOIN CAT_CATALOGO cr ON cr.codigo='NIVEL_RENDIMIENTO'
JOIN CAT_ITEM_CATALOGO r ON r.id_catalogo=cr.id_catalogo AND r.codigo='ALTO'
JOIN CAT_CATALOGO ca ON ca.codigo='ACCION_ADAPTACION'
JOIN CAT_ITEM_CATALOGO a ON a.id_catalogo=ca.id_catalogo AND a.codigo='SUBIR_DIFICULTAD'
WHERE p.codigo='POLITICA_BASE_TALLER' AND p.version_politica=1;

INSERT INTO CFG_REGLA_ADAPTACION (
 id_politica_adaptacion,codigo,nombre,prioridad,
 id_item_nivel_rendimiento,id_item_accion_principal,habilitar_pista
)
SELECT p.id_politica_adaptacion,'R_MEDIO','Rendimiento medio/fallback',90,
       r.id_item_catalogo,a.id_item_catalogo,FALSE
FROM CFG_POLITICA_ADAPTACION p
JOIN CAT_CATALOGO cr ON cr.codigo='NIVEL_RENDIMIENTO'
JOIN CAT_ITEM_CATALOGO r ON r.id_catalogo=cr.id_catalogo AND r.codigo='MEDIO'
JOIN CAT_CATALOGO ca ON ca.codigo='ACCION_ADAPTACION'
JOIN CAT_ITEM_CATALOGO a ON a.id_catalogo=ca.id_catalogo AND a.codigo='MANTENER_DIFICULTAD'
WHERE p.codigo='POLITICA_BASE_TALLER' AND p.version_politica=1;

-- Académico
INSERT INTO ACA_ASIGNATURA (codigo,nombre,descripcion)
VALUES ('MAT','Matemática','Asignatura de demostración');

INSERT INTO ACA_TEMA (id_asignatura,codigo,nombre,descripcion,orden)
SELECT id_asignatura,'ALG','Álgebra','Ecuaciones y operaciones algebraicas',10
FROM ACA_ASIGNATURA WHERE codigo='MAT';

-- Estudiante demo
INSERT INTO APR_ESTUDIANTE (codigo,nombre_mostrado,id_item_estado_estudiante)
SELECT 'EST-DEMO-001','Estudiante Demo',i.id_item_catalogo
FROM CAT_ITEM_CATALOGO i JOIN CAT_CATALOGO c ON c.id_catalogo=i.id_catalogo
WHERE c.codigo='ESTADO_ESTUDIANTE' AND i.codigo='ACTIVO';

-- Ejercicios: 5 por dificultad
WITH ref AS (
 SELECT t.id_tema,
        (SELECT i.id_item_catalogo FROM CAT_ITEM_CATALOGO i JOIN CAT_CATALOGO c ON c.id_catalogo=i.id_catalogo
         WHERE c.codigo='TIPO_EJERCICIO' AND i.codigo='OPCION_UNICA') tipo,
        (SELECT i.id_item_catalogo FROM CAT_ITEM_CATALOGO i JOIN CAT_CATALOGO c ON c.id_catalogo=i.id_catalogo
         WHERE c.codigo='ESTADO_EJERCICIO' AND i.codigo='PUBLICADO') estado,
        (SELECT i.id_item_catalogo FROM CAT_ITEM_CATALOGO i JOIN CAT_CATALOGO c ON c.id_catalogo=i.id_catalogo
         WHERE c.codigo='DIFICULTAD' AND i.codigo='BASICO') basico,
        (SELECT i.id_item_catalogo FROM CAT_ITEM_CATALOGO i JOIN CAT_CATALOGO c ON c.id_catalogo=i.id_catalogo
         WHERE c.codigo='DIFICULTAD' AND i.codigo='INTERMEDIO') intermedio,
        (SELECT i.id_item_catalogo FROM CAT_ITEM_CATALOGO i JOIN CAT_CATALOGO c ON c.id_catalogo=i.id_catalogo
         WHERE c.codigo='DIFICULTAD' AND i.codigo='AVANZADO') avanzado
 FROM ACA_TEMA t WHERE t.codigo='ALG'
)
INSERT INTO BAN_EJERCICIO(id_tema,codigo,enunciado,id_item_tipo_ejercicio,id_item_dificultad,id_item_estado_ejercicio,explicacion,tiempo_objetivo_segundos)
SELECT id_tema,codigo,enunciado,tipo,dificultad,estado,explicacion,tiempo
FROM ref CROSS JOIN LATERAL (VALUES
 ('ALG-B-001','2x + 4 = 12. ¿Cuánto vale x?',ref.basico,'Restar 4 y dividir entre 2.',20),
 ('ALG-B-002','x + 7 = 15. ¿Cuánto vale x?',ref.basico,'Restar 7 en ambos lados.',20),
 ('ALG-B-003','3x = 21. ¿Cuánto vale x?',ref.basico,'Dividir entre 3.',20),
 ('ALG-B-004','x - 5 = 9. ¿Cuánto vale x?',ref.basico,'Sumar 5.',20),
 ('ALG-B-005','5x = 30. ¿Cuánto vale x?',ref.basico,'Dividir entre 5.',20),
 ('ALG-I-001','3x + 5 = 20. ¿Cuánto vale x?',ref.intermedio,'Aislar 3x y dividir.',25),
 ('ALG-I-002','4x - 8 = 16. ¿Cuánto vale x?',ref.intermedio,'Sumar 8 y dividir entre 4.',25),
 ('ALG-I-003','2(x + 3) = 18. ¿Cuánto vale x?',ref.intermedio,'Dividir entre 2 y restar 3.',25),
 ('ALG-I-004','5x + 2 = 3x + 14. ¿Cuánto vale x?',ref.intermedio,'Agrupar términos con x.',30),
 ('ALG-I-005','7 - 2x = -5. ¿Cuánto vale x?',ref.intermedio,'Restar 7 y dividir entre -2.',30),
 ('ALG-A-001','(x-2)/3 + (x+1)/2 = 6. ¿Cuánto vale x?',ref.avanzado,'Eliminar denominadores y resolver.',45),
 ('ALG-A-002','x² - 5x + 6 = 0. ¿Cuál es una raíz?',ref.avanzado,'Factorizar (x-2)(x-3).',45),
 ('ALG-A-003','2x² - 8 = 0. ¿Cuál es la raíz positiva?',ref.avanzado,'Despejar x²=4.',45),
 ('ALG-A-004','Si 2^(x+1)=16, ¿cuánto vale x?',ref.avanzado,'16=2^4.',40),
 ('ALG-A-005','|2x-3|=7. ¿Cuál es una solución positiva?',ref.avanzado,'Resolver 2x-3=7.',45)
) AS q(codigo,enunciado,dificultad,explicacion,tiempo);

-- Opciones de demostración
INSERT INTO BAN_OPCION_EJERCICIO(id_ejercicio,codigo,texto,es_correcta,orden)
SELECT e.id_ejercicio,v.codigo,v.texto,v.correcta,v.orden
FROM BAN_EJERCICIO e
JOIN (VALUES
 ('ALG-B-001','A','2',FALSE,1),('ALG-B-001','B','3',FALSE,2),('ALG-B-001','C','4',TRUE,3),('ALG-B-001','D','6',FALSE,4),
 ('ALG-B-002','A','6',FALSE,1),('ALG-B-002','B','7',FALSE,2),('ALG-B-002','C','8',TRUE,3),('ALG-B-002','D','9',FALSE,4),
 ('ALG-B-003','A','6',FALSE,1),('ALG-B-003','B','7',TRUE,2),('ALG-B-003','C','8',FALSE,3),('ALG-B-003','D','9',FALSE,4),
 ('ALG-B-004','A','4',FALSE,1),('ALG-B-004','B','14',TRUE,2),('ALG-B-004','C','9',FALSE,3),('ALG-B-004','D','15',FALSE,4),
 ('ALG-B-005','A','5',FALSE,1),('ALG-B-005','B','6',TRUE,2),('ALG-B-005','C','7',FALSE,3),('ALG-B-005','D','8',FALSE,4),
 ('ALG-I-001','A','3',FALSE,1),('ALG-I-001','B','4',FALSE,2),('ALG-I-001','C','5',TRUE,3),('ALG-I-001','D','6',FALSE,4),
 ('ALG-I-002','A','4',FALSE,1),('ALG-I-002','B','5',FALSE,2),('ALG-I-002','C','6',TRUE,3),('ALG-I-002','D','8',FALSE,4),
 ('ALG-I-003','A','5',FALSE,1),('ALG-I-003','B','6',TRUE,2),('ALG-I-003','C','7',FALSE,3),('ALG-I-003','D','9',FALSE,4),
 ('ALG-I-004','A','4',FALSE,1),('ALG-I-004','B','5',FALSE,2),('ALG-I-004','C','6',TRUE,3),('ALG-I-004','D','7',FALSE,4),
 ('ALG-I-005','A','5',FALSE,1),('ALG-I-005','B','6',TRUE,2),('ALG-I-005','C','-6',FALSE,3),('ALG-I-005','D','7',FALSE,4),
 ('ALG-A-001','A','5',FALSE,1),('ALG-A-001','B','7',TRUE,2),('ALG-A-001','C','8',FALSE,3),('ALG-A-001','D','9',FALSE,4),
 ('ALG-A-002','A','1',FALSE,1),('ALG-A-002','B','2',TRUE,2),('ALG-A-002','C','4',FALSE,3),('ALG-A-002','D','6',FALSE,4),
 ('ALG-A-003','A','1',FALSE,1),('ALG-A-003','B','2',TRUE,2),('ALG-A-003','C','4',FALSE,3),('ALG-A-003','D','8',FALSE,4),
 ('ALG-A-004','A','2',FALSE,1),('ALG-A-004','B','3',TRUE,2),('ALG-A-004','C','4',FALSE,3),('ALG-A-004','D','5',FALSE,4),
 ('ALG-A-005','A','2',FALSE,1),('ALG-A-005','B','5',TRUE,2),('ALG-A-005','C','7',FALSE,3),('ALG-A-005','D','10',FALSE,4)
) v(ejercicio,codigo,texto,correcta,orden) ON e.codigo=v.ejercicio;

INSERT INTO BAN_PISTA_EJERCICIO(id_ejercicio,orden,texto,penalizacion_puntaje)
SELECT id_ejercicio,1,'Aísla primero los términos que contienen la variable.',0.10
FROM BAN_EJERCICIO;
