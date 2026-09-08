package com.veltia.adaptivequiz.infrastructure.persistence.adapters;

import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import com.veltia.adaptivequiz.domain.model.ParametroConfiguracion;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import com.veltia.adaptivequiz.domain.model.ReglaAdaptacion;
import com.veltia.adaptivequiz.domain.repository.CatalogoRepository;
import com.veltia.adaptivequiz.domain.repository.ParametroRepository;
import com.veltia.adaptivequiz.domain.repository.PoliticaAdaptacionRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.jdbc.CatalogoJdbcResolver;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** Adaptador PostgreSQL de configuración: catálogos, parámetros y política versionada. */
@Repository
public class CatalogoConfiguracionJdbcAdapter implements
        CatalogoRepository, ParametroRepository, PoliticaAdaptacionRepository {

    private final NamedParameterJdbcTemplate jdbc;
    private final CatalogoJdbcResolver catalogos;

    public CatalogoConfiguracionJdbcAdapter(
            NamedParameterJdbcTemplate jdbc,
            CatalogoJdbcResolver catalogos) {
        this.jdbc = jdbc;
        this.catalogos = catalogos;
    }

    @Override
    public Optional<ItemCatalogo> findItemActivo(String codigoCatalogo, String codigoItem) {
        return catalogos.buscarActivo(codigoCatalogo, codigoItem);
    }

    @Override
    public List<ItemCatalogo> findItemsActivos(String codigoCatalogo) {
        return catalogos.listarActivos(codigoCatalogo);
    }

    @Override
    public Optional<ParametroConfiguracion> findActivoByCodigo(String codigo) {
        List<ParametroConfiguracion> parametros = jdbc.query("""
                SELECT codigo, valor_entero, valor_decimal, valor_booleano, valor_texto
                FROM CFG_PARAMETRO
                WHERE codigo = :codigo
                  AND activo = TRUE
                  AND (vigente_desde IS NULL OR vigente_desde <= CURRENT_TIMESTAMP)
                  AND (vigente_hasta IS NULL OR vigente_hasta >= CURRENT_TIMESTAMP)
                """, Map.of("codigo", codigo), (rs, rowNum) -> new ParametroConfiguracion(
                rs.getString("codigo"),
                rs.getObject("valor_entero", Long.class),
                rs.getBigDecimal("valor_decimal"),
                rs.getObject("valor_booleano", Boolean.class),
                rs.getString("valor_texto")));
        return parametros.stream().findFirst();
    }

    @Override
    public Optional<PoliticaAdaptacion> findActivaVigente() {
        return buscarPolitica("""
                SELECT *
                FROM CFG_POLITICA_ADAPTACION
                WHERE activa = TRUE
                  AND vigente_desde <= CURRENT_TIMESTAMP
                  AND (vigente_hasta IS NULL OR vigente_hasta >= CURRENT_TIMESTAMP)
                ORDER BY version_politica DESC, id_politica_adaptacion DESC
                LIMIT 1
                """, Map.of());
    }

    @Override
    public Optional<PoliticaAdaptacion> findById(Long idPoliticaAdaptacion) {
        return buscarPolitica("""
                SELECT *
                FROM CFG_POLITICA_ADAPTACION
                WHERE id_politica_adaptacion = :idPoliticaAdaptacion
                """, Map.of("idPoliticaAdaptacion", idPoliticaAdaptacion));
    }

    private Optional<PoliticaAdaptacion> buscarPolitica(String sql, Map<String, ?> parametros) {
        List<PoliticaAdaptacion> politicas = jdbc.query(sql, parametros, (rs, rowNum) -> {
            Long id = rs.getLong("id_politica_adaptacion");
            return new PoliticaAdaptacion(
                    id,
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getInt("version_politica"),
                    catalogos.requeridoPorId(rs.getLong("id_item_modo_adaptacion")),
                    rs.getShort("tamano_ventana_intentos"),
                    rs.getObject("vigente_desde", OffsetDateTime.class),
                    rs.getObject("vigente_hasta", OffsetDateTime.class),
                    rs.getBoolean("activa"),
                    reglas(id));
        });
        return politicas.stream().findFirst();
    }

    private List<ReglaAdaptacion> reglas(Long idPoliticaAdaptacion) {
        return jdbc.query("""
                SELECT *
                FROM CFG_REGLA_ADAPTACION
                WHERE id_politica_adaptacion = :idPoliticaAdaptacion
                  AND activa = TRUE
                  AND vigente_desde <= CURRENT_TIMESTAMP
                  AND (vigente_hasta IS NULL OR vigente_hasta >= CURRENT_TIMESTAMP)
                ORDER BY prioridad ASC, id_regla_adaptacion ASC
                """, Map.of("idPoliticaAdaptacion", idPoliticaAdaptacion), (rs, rowNum) -> new ReglaAdaptacion(
                rs.getLong("id_regla_adaptacion"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getInt("prioridad"),
                rs.getBigDecimal("porcentaje_acierto_min"),
                rs.getBigDecimal("porcentaje_acierto_max"),
                rs.getObject("tiempo_promedio_min_ms", Integer.class),
                rs.getObject("tiempo_promedio_max_ms", Integer.class),
                rs.getObject("racha_aciertos_min", Short.class),
                rs.getObject("racha_errores_min", Short.class),
                catalogos.requeridoPorId(rs.getLong("id_item_nivel_rendimiento")),
                catalogos.requeridoPorId(rs.getLong("id_item_accion_principal")),
                itemOpcional(rs.getObject("id_item_tipo_ejercicio_destino", Long.class)),
                rs.getBoolean("habilitar_pista"),
                rs.getBoolean("activa")));
    }

    private ItemCatalogo itemOpcional(Long idItemCatalogo) {
        return idItemCatalogo == null ? null : catalogos.requeridoPorId(idItemCatalogo);
    }
}
