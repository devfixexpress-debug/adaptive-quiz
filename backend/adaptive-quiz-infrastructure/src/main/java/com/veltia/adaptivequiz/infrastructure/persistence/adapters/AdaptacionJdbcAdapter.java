package com.veltia.adaptivequiz.infrastructure.persistence.adapters;

import com.veltia.adaptivequiz.domain.model.AccionEvento;
import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.EventoAdaptacion;
import com.veltia.adaptivequiz.domain.repository.ContextoAprendizajeRepository;
import com.veltia.adaptivequiz.domain.repository.EventoAdaptacionRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.jdbc.CatalogoJdbcResolver;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** Adaptador auditable de snapshots, decisiones y acciones del motor adaptativo. */
@Repository
public class AdaptacionJdbcAdapter implements ContextoAprendizajeRepository, EventoAdaptacionRepository {

    private final NamedParameterJdbcTemplate jdbc;
    private final CatalogoJdbcResolver catalogos;

    public AdaptacionJdbcAdapter(NamedParameterJdbcTemplate jdbc, CatalogoJdbcResolver catalogos) {
        this.jdbc = jdbc;
        this.catalogos = catalogos;
    }

    @Override
    public ContextoAprendizaje guardar(ContextoAprendizaje contexto) {
        Long id = jdbc.queryForObject("""
                INSERT INTO ADP_CONTEXTO_APRENDIZAJE (
                    id_intento_disparador, id_estudiante, id_tema, id_politica_adaptacion,
                    numero_intentos_ventana, total_aciertos_ventana, porcentaje_acierto,
                    tiempo_promedio_ms, racha_aciertos, racha_errores,
                    id_item_dificultad_actual, id_item_tipo_ejercicio_actual, puntaje_rendimiento
                ) VALUES (
                    :idIntento, :idEstudiante, :idTema, :idPolitica,
                    :numeroIntentos, :totalAciertos, :porcentajeAcierto,
                    :tiempoPromedio, :rachaAciertos, :rachaErrores,
                    :idDificultad, :idTipo, :puntajeRendimiento
                ) RETURNING id_contexto_aprendizaje
                """, new MapSqlParameterSource()
                .addValue("idIntento", contexto.idIntentoDisparador())
                .addValue("idEstudiante", contexto.idEstudiante())
                .addValue("idTema", contexto.idTema())
                .addValue("idPolitica", contexto.idPoliticaAdaptacion())
                .addValue("numeroIntentos", contexto.numeroIntentosVentana())
                .addValue("totalAciertos", contexto.totalAciertosVentana())
                .addValue("porcentajeAcierto", contexto.porcentajeAcierto())
                .addValue("tiempoPromedio", contexto.tiempoPromedioMs())
                .addValue("rachaAciertos", contexto.rachaAciertos())
                .addValue("rachaErrores", contexto.rachaErrores())
                .addValue("idDificultad", contexto.dificultadActual().idItemCatalogo())
                .addValue("idTipo", contexto.tipoEjercicioActual().idItemCatalogo())
                .addValue("puntajeRendimiento", contexto.puntajeRendimiento()), Long.class);
        return new ContextoAprendizaje(
                id,
                contexto.idIntentoDisparador(),
                contexto.idEstudiante(),
                contexto.idTema(),
                contexto.idPoliticaAdaptacion(),
                contexto.numeroIntentosVentana(),
                contexto.totalAciertosVentana(),
                contexto.porcentajeAcierto(),
                contexto.tiempoPromedioMs(),
                contexto.rachaAciertos(),
                contexto.rachaErrores(),
                contexto.dificultadActual(),
                contexto.tipoEjercicioActual(),
                contexto.puntajeRendimiento());
    }

    @Override
    public Optional<ContextoAprendizaje> findContextoById(Long idContextoAprendizaje) {
        List<ContextoAprendizaje> contextos = jdbc.query("""
                SELECT *
                FROM ADP_CONTEXTO_APRENDIZAJE
                WHERE id_contexto_aprendizaje = :idContextoAprendizaje
                """, Map.of("idContextoAprendizaje", idContextoAprendizaje), this::mapContexto);
        return contextos.stream().findFirst();
    }

    @Override
    public EventoAdaptacion guardar(EventoAdaptacion evento) {
        MapSqlParameterSource parametros = new MapSqlParameterSource()
                .addValue("idContexto", evento.idContextoAprendizaje())
                .addValue("idRegla", evento.idReglaAdaptacion())
                .addValue("idOrigen", evento.origenDecision().idItemCatalogo())
                .addValue("idNivel", evento.nivelRendimiento().idItemCatalogo())
                .addValue("idAccion", evento.accionPrincipal().idItemCatalogo())
                .addValue("idDificultadAnterior", evento.dificultadAnterior().idItemCatalogo())
                .addValue("idDificultadNueva", evento.dificultadNueva().idItemCatalogo())
                .addValue("idTipoAnterior", idItem(evento.tipoEjercicioAnterior()))
                .addValue("idTipoNuevo", idItem(evento.tipoEjercicioNuevo()))
                .addValue("motivo", evento.motivo())
                .addValue("versionMotor", evento.versionMotor())
                .addValue("fechaDecision", evento.fechaDecision())
                .addValue("aplicacionExitosa", evento.aplicacionExitosa());
        Long idEvento = jdbc.queryForObject("""
                INSERT INTO ADP_EVENTO_ADAPTACION (
                    id_contexto_aprendizaje, id_regla_adaptacion, id_item_origen_decision,
                    id_item_nivel_rendimiento, id_item_accion_principal,
                    id_item_dificultad_anterior, id_item_dificultad_nueva,
                    id_item_tipo_ejercicio_anterior, id_item_tipo_ejercicio_nuevo,
                    motivo, version_motor, fecha_decision, aplicacion_exitosa
                ) VALUES (
                    :idContexto, :idRegla, :idOrigen,
                    :idNivel, :idAccion,
                    :idDificultadAnterior, :idDificultadNueva,
                    :idTipoAnterior, :idTipoNuevo,
                    :motivo, :versionMotor, :fechaDecision, :aplicacionExitosa
                ) RETURNING id_evento_adaptacion
                """, parametros, Long.class);
        for (AccionEvento accion : evento.acciones()) {
            jdbc.update("""
                    INSERT INTO ADP_ACCION_EVENTO (
                        id_evento_adaptacion, secuencia, id_item_accion_adaptacion,
                        detalle, valor_anterior, valor_nuevo, ejecutada
                    ) VALUES (
                        :idEvento, :secuencia, :idAccion,
                        :detalle, :valorAnterior, :valorNuevo, :ejecutada
                    )
                    """, new MapSqlParameterSource()
                    .addValue("idEvento", idEvento)
                    .addValue("secuencia", accion.secuencia())
                    .addValue("idAccion", accion.accionAdaptacion().idItemCatalogo())
                    .addValue("detalle", accion.detalle())
                    .addValue("valorAnterior", accion.valorAnterior())
                    .addValue("valorNuevo", accion.valorNuevo())
                    .addValue("ejecutada", accion.ejecutada()));
        }
        return new EventoAdaptacion(
                idEvento,
                evento.idContextoAprendizaje(),
                evento.idReglaAdaptacion(),
                evento.codigoRegla(),
                evento.origenDecision(),
                evento.nivelRendimiento(),
                evento.accionPrincipal(),
                evento.dificultadAnterior(),
                evento.dificultadNueva(),
                evento.tipoEjercicioAnterior(),
                evento.tipoEjercicioNuevo(),
                evento.motivo(),
                evento.versionMotor(),
                evento.fechaDecision(),
                evento.aplicacionExitosa(),
                evento.acciones());
    }

    @Override
    public List<EventoAdaptacion> findPorEstudiante(Long idEstudiante) {
        return jdbc.query("""
                SELECT e.*, r.codigo AS codigo_regla
                FROM ADP_EVENTO_ADAPTACION e
                LEFT JOIN CFG_REGLA_ADAPTACION r ON r.id_regla_adaptacion = e.id_regla_adaptacion
                JOIN ADP_CONTEXTO_APRENDIZAJE c ON c.id_contexto_aprendizaje = e.id_contexto_aprendizaje
                WHERE c.id_estudiante = :idEstudiante
                ORDER BY e.fecha_decision DESC, e.id_evento_adaptacion DESC
                """, Map.of("idEstudiante", idEstudiante), this::mapEvento);
    }

    @Override
    public Optional<EventoAdaptacion> findById(Long idEventoAdaptacion) {
        List<EventoAdaptacion> eventos = jdbc.query("""
                SELECT e.*, r.codigo AS codigo_regla
                FROM ADP_EVENTO_ADAPTACION e
                LEFT JOIN CFG_REGLA_ADAPTACION r ON r.id_regla_adaptacion = e.id_regla_adaptacion
                WHERE e.id_evento_adaptacion = :idEventoAdaptacion
                """, Map.of("idEventoAdaptacion", idEventoAdaptacion), this::mapEvento);
        return eventos.stream().findFirst();
    }

    @Override
    public Optional<EventoAdaptacion> findUltimoPorEstudianteAndTema(Long idEstudiante, Long idTema) {
        List<EventoAdaptacion> eventos = jdbc.query("""
                SELECT e.*, r.codigo AS codigo_regla
                FROM ADP_EVENTO_ADAPTACION e
                LEFT JOIN CFG_REGLA_ADAPTACION r ON r.id_regla_adaptacion = e.id_regla_adaptacion
                JOIN ADP_CONTEXTO_APRENDIZAJE c ON c.id_contexto_aprendizaje = e.id_contexto_aprendizaje
                WHERE c.id_estudiante = :idEstudiante AND c.id_tema = :idTema
                ORDER BY e.fecha_decision DESC, e.id_evento_adaptacion DESC
                LIMIT 1
                """, Map.of("idEstudiante", idEstudiante, "idTema", idTema), this::mapEvento);
        return eventos.stream().findFirst();
    }

    private ContextoAprendizaje mapContexto(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new ContextoAprendizaje(
                rs.getLong("id_contexto_aprendizaje"),
                rs.getLong("id_intento_disparador"),
                rs.getLong("id_estudiante"),
                rs.getLong("id_tema"),
                rs.getLong("id_politica_adaptacion"),
                rs.getShort("numero_intentos_ventana"),
                rs.getShort("total_aciertos_ventana"),
                rs.getBigDecimal("porcentaje_acierto"),
                rs.getInt("tiempo_promedio_ms"),
                rs.getShort("racha_aciertos"),
                rs.getShort("racha_errores"),
                catalogos.requeridoPorId(rs.getLong("id_item_dificultad_actual")),
                catalogos.requeridoPorId(rs.getLong("id_item_tipo_ejercicio_actual")),
                rs.getBigDecimal("puntaje_rendimiento"));
    }

    private EventoAdaptacion mapEvento(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Long idEvento = rs.getLong("id_evento_adaptacion");
        return new EventoAdaptacion(
                idEvento,
                rs.getLong("id_contexto_aprendizaje"),
                rs.getObject("id_regla_adaptacion", Long.class),
                rs.getString("codigo_regla"),
                catalogos.requeridoPorId(rs.getLong("id_item_origen_decision")),
                catalogos.requeridoPorId(rs.getLong("id_item_nivel_rendimiento")),
                catalogos.requeridoPorId(rs.getLong("id_item_accion_principal")),
                catalogos.requeridoPorId(rs.getLong("id_item_dificultad_anterior")),
                catalogos.requeridoPorId(rs.getLong("id_item_dificultad_nueva")),
                itemOpcional(rs.getObject("id_item_tipo_ejercicio_anterior", Long.class)),
                itemOpcional(rs.getObject("id_item_tipo_ejercicio_nuevo", Long.class)),
                rs.getString("motivo"),
                rs.getString("version_motor"),
                rs.getObject("fecha_decision", OffsetDateTime.class),
                rs.getBoolean("aplicacion_exitosa"),
                acciones(idEvento));
    }

    private List<AccionEvento> acciones(Long idEvento) {
        return jdbc.query("""
                SELECT *
                FROM ADP_ACCION_EVENTO
                WHERE id_evento_adaptacion = :idEvento
                ORDER BY secuencia ASC
                """, Map.of("idEvento", idEvento), (rs, rowNum) -> new AccionEvento(
                rs.getShort("secuencia"),
                catalogos.requeridoPorId(rs.getLong("id_item_accion_adaptacion")),
                rs.getString("detalle"),
                rs.getString("valor_anterior"),
                rs.getString("valor_nuevo"),
                rs.getBoolean("ejecutada")));
    }

    private Long idItem(com.veltia.adaptivequiz.domain.model.ItemCatalogo item) {
        return item == null ? null : item.idItemCatalogo();
    }

    private com.veltia.adaptivequiz.domain.model.ItemCatalogo itemOpcional(Long idItem) {
        return idItem == null ? null : catalogos.requeridoPorId(idItem);
    }
}
