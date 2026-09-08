package com.veltia.adaptivequiz.infrastructure.persistence.adapters;

import com.veltia.adaptivequiz.domain.model.Estudiante;
import com.veltia.adaptivequiz.domain.model.Intento;
import com.veltia.adaptivequiz.domain.model.IntentoContextual;
import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import com.veltia.adaptivequiz.domain.model.ProgresoTema;
import com.veltia.adaptivequiz.domain.model.Respuesta;
import com.veltia.adaptivequiz.domain.model.SesionPractica;
import com.veltia.adaptivequiz.domain.repository.EstudianteRepository;
import com.veltia.adaptivequiz.domain.repository.IntentoRepository;
import com.veltia.adaptivequiz.domain.repository.ProgresoTemaRepository;
import com.veltia.adaptivequiz.domain.repository.RespuestaRepository;
import com.veltia.adaptivequiz.domain.repository.SesionPracticaRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.jdbc.CatalogoJdbcResolver;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** Persistencia de hechos de práctica y de la proyección APR_PROGRESO_TEMA. */
@Repository
public class AprendizajeJdbcAdapter implements
        EstudianteRepository,
        ProgresoTemaRepository,
        SesionPracticaRepository,
        IntentoRepository,
        RespuestaRepository {

    private final NamedParameterJdbcTemplate jdbc;
    private final CatalogoJdbcResolver catalogos;

    public AprendizajeJdbcAdapter(NamedParameterJdbcTemplate jdbc, CatalogoJdbcResolver catalogos) {
        this.jdbc = jdbc;
        this.catalogos = catalogos;
    }

    @Override
    public Optional<Estudiante> findActivoById(Long idEstudiante) {
        List<Estudiante> estudiantes = jdbc.query("""
                SELECT e.id_estudiante, e.codigo, e.nombre_mostrado, e.id_item_estado_estudiante
                FROM APR_ESTUDIANTE e
                JOIN CAT_ITEM_CATALOGO i ON i.id_item_catalogo = e.id_item_estado_estudiante
                JOIN CAT_CATALOGO c ON c.id_catalogo = i.id_catalogo
                WHERE e.id_estudiante = :idEstudiante
                  AND c.codigo = 'ESTADO_ESTUDIANTE'
                  AND i.codigo = 'ACTIVO'
                  AND i.activo = TRUE
                """, Map.of("idEstudiante", idEstudiante), (rs, rowNum) -> new Estudiante(
                rs.getLong("id_estudiante"),
                rs.getString("codigo"),
                rs.getString("nombre_mostrado"),
                catalogos.requeridoPorId(rs.getLong("id_item_estado_estudiante"))));
        return estudiantes.stream().findFirst();
    }

    @Override
    public Optional<ProgresoTema> findByEstudianteAndTema(Long idEstudiante, Long idTema) {
        List<ProgresoTema> progresos = jdbc.query("""
                SELECT *
                FROM APR_PROGRESO_TEMA
                WHERE id_estudiante = :idEstudiante AND id_tema = :idTema
                """, Map.of("idEstudiante", idEstudiante, "idTema", idTema), this::mapProgreso);
        return progresos.stream().findFirst();
    }

    @Override
    public List<ProgresoTema> findByEstudiante(Long idEstudiante) {
        return jdbc.query("""
                SELECT *
                FROM APR_PROGRESO_TEMA
                WHERE id_estudiante = :idEstudiante
                ORDER BY fecha_ultimo_intento DESC NULLS LAST, id_progreso_tema ASC
                """, Map.of("idEstudiante", idEstudiante), this::mapProgreso);
    }

    @Override
    public ProgresoTema guardar(ProgresoTema progreso) {
        MapSqlParameterSource parametros = parametrosProgreso(progreso);
        Long id = progreso.idProgresoTema() == null
                ? jdbc.queryForObject("""
                        INSERT INTO APR_PROGRESO_TEMA (
                            id_estudiante, id_tema, id_item_dificultad_actual, total_intentos,
                            total_aciertos, porcentaje_acierto, tiempo_promedio_ms,
                            racha_aciertos_actual, racha_errores_actual, fecha_ultimo_intento
                        ) VALUES (
                            :idEstudiante, :idTema, :idDificultad, :totalIntentos,
                            :totalAciertos, :porcentajeAcierto, :tiempoPromedioMs,
                            :rachaAciertos, :rachaErrores, :fechaUltimoIntento
                        ) RETURNING id_progreso_tema
                        """, parametros, Long.class)
                : guardarProgresoExistente(progreso.idProgresoTema(), parametros);
        return new ProgresoTema(
                id,
                progreso.idEstudiante(),
                progreso.idTema(),
                progreso.dificultadActual(),
                progreso.totalIntentos(),
                progreso.totalAciertos(),
                progreso.porcentajeAcierto(),
                progreso.tiempoPromedioMs(),
                progreso.rachaAciertosActual(),
                progreso.rachaErroresActual(),
                progreso.fechaUltimoIntento());
    }

    private Long guardarProgresoExistente(Long idProgresoTema, MapSqlParameterSource parametros) {
        parametros.addValue("idProgresoTema", idProgresoTema);
        jdbc.update("""
                UPDATE APR_PROGRESO_TEMA
                SET id_item_dificultad_actual = :idDificultad,
                    total_intentos = :totalIntentos,
                    total_aciertos = :totalAciertos,
                    porcentaje_acierto = :porcentajeAcierto,
                    tiempo_promedio_ms = :tiempoPromedioMs,
                    racha_aciertos_actual = :rachaAciertos,
                    racha_errores_actual = :rachaErrores,
                    fecha_ultimo_intento = :fechaUltimoIntento,
                    fecha_modificacion = CURRENT_TIMESTAMP,
                    usuario_modificacion = 'API'
                WHERE id_progreso_tema = :idProgresoTema
                """, parametros);
        return idProgresoTema;
    }

    @Override
    public Optional<SesionPractica> findById(Long idSesionPractica) {
        List<SesionPractica> sesiones = jdbc.query("SELECT * FROM PRA_SESION_PRACTICA WHERE id_sesion_practica = :idSesionPractica",
                Map.of("idSesionPractica", idSesionPractica), this::mapSesion);
        return sesiones.stream().findFirst();
    }

    @Override
    public Optional<SesionPractica> findIniciadaByEstudianteAndTema(Long idEstudiante, Long idTema) {
        List<SesionPractica> sesiones = jdbc.query("""
                SELECT s.*
                FROM PRA_SESION_PRACTICA s
                JOIN CAT_ITEM_CATALOGO i ON i.id_item_catalogo = s.id_item_estado_sesion
                JOIN CAT_CATALOGO c ON c.id_catalogo = i.id_catalogo
                WHERE s.id_estudiante = :idEstudiante
                  AND s.id_tema = :idTema
                  AND c.codigo = 'ESTADO_SESION'
                  AND i.codigo = 'INICIADA'
                ORDER BY s.fecha_inicio DESC, s.id_sesion_practica DESC
                LIMIT 1
                """, Map.of("idEstudiante", idEstudiante, "idTema", idTema), this::mapSesion);
        return sesiones.stream().findFirst();
    }

    @Override
    public SesionPractica guardar(SesionPractica sesion) {
        MapSqlParameterSource parametros = new MapSqlParameterSource()
                .addValue("idEstudiante", sesion.idEstudiante())
                .addValue("idTema", sesion.idTema())
                .addValue("idPolitica", sesion.idPoliticaAdaptacion())
                .addValue("idEstado", sesion.estado().idItemCatalogo())
                .addValue("fechaInicio", sesion.fechaInicio())
                .addValue("fechaFin", sesion.fechaFin())
                .addValue("cantidadIntentos", sesion.cantidadIntentos());
        Long id = sesion.idSesionPractica() == null
                ? jdbc.queryForObject("""
                        INSERT INTO PRA_SESION_PRACTICA (
                            id_estudiante, id_tema, id_politica_adaptacion, id_item_estado_sesion,
                            fecha_inicio, fecha_fin, cantidad_intentos
                        ) VALUES (
                            :idEstudiante, :idTema, :idPolitica, :idEstado,
                            :fechaInicio, :fechaFin, :cantidadIntentos
                        ) RETURNING id_sesion_practica
                        """, parametros, Long.class)
                : guardarSesionExistente(sesion.idSesionPractica(), parametros);
        return new SesionPractica(
                id,
                sesion.idEstudiante(),
                sesion.idTema(),
                sesion.idPoliticaAdaptacion(),
                sesion.estado(),
                sesion.fechaInicio(),
                sesion.fechaFin(),
                sesion.cantidadIntentos());
    }

    private Long guardarSesionExistente(Long idSesionPractica, MapSqlParameterSource parametros) {
        parametros.addValue("idSesionPractica", idSesionPractica);
        jdbc.update("""
                UPDATE PRA_SESION_PRACTICA
                SET id_item_estado_sesion = :idEstado,
                    fecha_fin = :fechaFin,
                    cantidad_intentos = :cantidadIntentos,
                    fecha_modificacion = CURRENT_TIMESTAMP,
                    usuario_modificacion = 'API'
                WHERE id_sesion_practica = :idSesionPractica
                """, parametros);
        return idSesionPractica;
    }

    @Override
    public Intento guardar(Intento intento) {
        Long id = jdbc.queryForObject("""
                INSERT INTO PRA_INTENTO (
                    id_sesion_practica, id_ejercicio, numero_orden, fecha_inicio, fecha_fin,
                    tiempo_respuesta_ms, id_item_resultado_intento, puntaje_obtenido, uso_pista
                ) VALUES (
                    :idSesion, :idEjercicio, :numeroOrden, :fechaInicio, :fechaFin,
                    :tiempoRespuestaMs, :idResultado, :puntaje, :usoPista
                ) RETURNING id_intento
                """, new MapSqlParameterSource()
                .addValue("idSesion", intento.idSesionPractica())
                .addValue("idEjercicio", intento.idEjercicio())
                .addValue("numeroOrden", intento.numeroOrden())
                .addValue("fechaInicio", intento.fechaInicio())
                .addValue("fechaFin", intento.fechaFin())
                .addValue("tiempoRespuestaMs", intento.tiempoRespuestaMs())
                .addValue("idResultado", intento.resultado().idItemCatalogo())
                .addValue("puntaje", intento.puntajeObtenido())
                .addValue("usoPista", intento.usoPista()), Long.class);
        return new Intento(
                id,
                intento.idSesionPractica(),
                intento.idEjercicio(),
                intento.numeroOrden(),
                intento.fechaInicio(),
                intento.fechaFin(),
                intento.tiempoRespuestaMs(),
                intento.resultado(),
                intento.puntajeObtenido(),
                intento.usoPista());
    }

    @Override
    public List<IntentoContextual> findRecientesParaContexto(Long idEstudiante, Long idTema, int limite) {
        return jdbc.query("""
                SELECT i.id_intento, i.tiempo_respuesta_ms, i.fecha_fin,
                       resultado.codigo AS codigo_resultado,
                       dificultad.id_item_catalogo AS id_dificultad,
                       tipo.id_item_catalogo AS id_tipo
                FROM PRA_INTENTO i
                JOIN PRA_SESION_PRACTICA s ON s.id_sesion_practica = i.id_sesion_practica
                JOIN BAN_EJERCICIO e ON e.id_ejercicio = i.id_ejercicio
                JOIN CAT_ITEM_CATALOGO resultado ON resultado.id_item_catalogo = i.id_item_resultado_intento
                JOIN CAT_ITEM_CATALOGO dificultad ON dificultad.id_item_catalogo = e.id_item_dificultad
                JOIN CAT_ITEM_CATALOGO tipo ON tipo.id_item_catalogo = e.id_item_tipo_ejercicio
                WHERE s.id_estudiante = :idEstudiante AND s.id_tema = :idTema
                ORDER BY i.fecha_fin DESC, i.id_intento DESC
                LIMIT :limite
                """, Map.of("idEstudiante", idEstudiante, "idTema", idTema, "limite", limite),
                (rs, rowNum) -> new IntentoContextual(
                        rs.getLong("id_intento"),
                        "CORRECTO".equals(rs.getString("codigo_resultado")),
                        rs.getInt("tiempo_respuesta_ms"),
                        catalogos.requeridoPorId(rs.getLong("id_dificultad")),
                        catalogos.requeridoPorId(rs.getLong("id_tipo")),
                        rs.getObject("fecha_fin", OffsetDateTime.class)));
    }

    @Override
    public List<Long> findIdsEjerciciosResueltosPorSesion(Long idSesionPractica) {
        return jdbc.queryForList("""
                SELECT id_ejercicio
                FROM PRA_INTENTO
                WHERE id_sesion_practica = :idSesionPractica
                ORDER BY numero_orden ASC
                """, Map.of("idSesionPractica", idSesionPractica), Long.class);
    }

    @Override
    public Respuesta guardar(Respuesta respuesta) {
        Long idRespuesta = jdbc.queryForObject("""
                INSERT INTO PRA_RESPUESTA (id_intento, texto_respuesta, fecha_respuesta)
                VALUES (:idIntento, :textoRespuesta, :fechaRespuesta)
                RETURNING id_respuesta
                """, new MapSqlParameterSource()
                .addValue("idIntento", respuesta.idIntento())
                .addValue("textoRespuesta", respuesta.textoRespuesta())
                .addValue("fechaRespuesta", respuesta.fechaRespuesta()), Long.class);
        for (Long idOpcion : respuesta.idOpcionesSeleccionadas()) {
            jdbc.update("""
                    INSERT INTO PRA_RESPUESTA_OPCION (id_respuesta, id_opcion_ejercicio)
                    VALUES (:idRespuesta, :idOpcion)
                    """, Map.of("idRespuesta", idRespuesta, "idOpcion", idOpcion));
        }
        return new Respuesta(
                idRespuesta,
                respuesta.idIntento(),
                respuesta.textoRespuesta(),
                respuesta.idOpcionesSeleccionadas(),
                respuesta.fechaRespuesta());
    }

    private ProgresoTema mapProgreso(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new ProgresoTema(
                rs.getLong("id_progreso_tema"),
                rs.getLong("id_estudiante"),
                rs.getLong("id_tema"),
                catalogos.requeridoPorId(rs.getLong("id_item_dificultad_actual")),
                rs.getInt("total_intentos"),
                rs.getInt("total_aciertos"),
                rs.getBigDecimal("porcentaje_acierto"),
                rs.getInt("tiempo_promedio_ms"),
                rs.getInt("racha_aciertos_actual"),
                rs.getInt("racha_errores_actual"),
                rs.getObject("fecha_ultimo_intento", OffsetDateTime.class));
    }

    private SesionPractica mapSesion(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new SesionPractica(
                rs.getLong("id_sesion_practica"),
                rs.getLong("id_estudiante"),
                rs.getLong("id_tema"),
                rs.getLong("id_politica_adaptacion"),
                catalogos.requeridoPorId(rs.getLong("id_item_estado_sesion")),
                rs.getObject("fecha_inicio", OffsetDateTime.class),
                rs.getObject("fecha_fin", OffsetDateTime.class),
                rs.getInt("cantidad_intentos"));
    }

    private MapSqlParameterSource parametrosProgreso(ProgresoTema progreso) {
        return new MapSqlParameterSource()
                .addValue("idEstudiante", progreso.idEstudiante())
                .addValue("idTema", progreso.idTema())
                .addValue("idDificultad", progreso.dificultadActual().idItemCatalogo())
                .addValue("totalIntentos", progreso.totalIntentos())
                .addValue("totalAciertos", progreso.totalAciertos())
                .addValue("porcentajeAcierto", progreso.porcentajeAcierto())
                .addValue("tiempoPromedioMs", progreso.tiempoPromedioMs())
                .addValue("rachaAciertos", progreso.rachaAciertosActual())
                .addValue("rachaErrores", progreso.rachaErroresActual())
                .addValue("fechaUltimoIntento", progreso.fechaUltimoIntento());
    }
}
