-- AdaptiveQuiz
-- PostgreSQL 17
-- V2 - Parámetros operativos del MVP integrado.
-- V1 y V1.1 permanecen inmutables.

INSERT INTO CFG_PARAMETRO (
    codigo,
    nombre,
    descripcion,
    tipo_dato,
    valor_texto
) VALUES
    (
        'DIFICULTAD_INICIAL_CODIGO',
        'Dificultad inicial',
        'Código paramétrico de la dificultad con la que inicia un progreso de tema nuevo.',
        'TEXTO',
        'INTERMEDIO'
    ),
    (
        'TIPO_EJERCICIO_INICIAL_CODIGO',
        'Tipo de ejercicio inicial',
        'Código paramétrico del tipo de ejercicio para una sesión sin intentos previos.',
        'TEXTO',
        'OPCION_UNICA'
    ),
    (
        'VERSION_MOTOR_REGLAS',
        'Versión del motor de reglas',
        'Versión funcional registrada en cada evento adaptativo generado por reglas.',
        'TEXTO',
        '1.0.0'
    );
