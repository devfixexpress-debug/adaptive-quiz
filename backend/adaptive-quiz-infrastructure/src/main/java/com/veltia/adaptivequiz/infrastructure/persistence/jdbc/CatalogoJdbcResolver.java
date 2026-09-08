package com.veltia.adaptivequiz.infrastructure.persistence.jdbc;

import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/** Resuelve referencias paramétricas sin filtrar por identificadores físicos. */
@Component
public class CatalogoJdbcResolver {

    private static final RowMapper<ItemCatalogo> ITEM_MAPPER = (rs, rowNum) -> new ItemCatalogo(
            rs.getLong("id_item_catalogo"), rs.getString("codigo"), rs.getString("nombre"));

    private final NamedParameterJdbcTemplate jdbc;

    public CatalogoJdbcResolver(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<ItemCatalogo> buscarActivo(String codigoCatalogo, String codigoItem) {
        List<ItemCatalogo> items = jdbc.query("""
                SELECT i.id_item_catalogo, i.codigo, i.nombre
                FROM CAT_ITEM_CATALOGO i
                JOIN CAT_CATALOGO c ON c.id_catalogo = i.id_catalogo
                WHERE c.codigo = :codigoCatalogo
                  AND c.activo = TRUE
                  AND i.codigo = :codigoItem
                  AND i.activo = TRUE
                """, Map.of("codigoCatalogo", codigoCatalogo, "codigoItem", codigoItem), ITEM_MAPPER);
        return items.stream().findFirst();
    }

    public List<ItemCatalogo> listarActivos(String codigoCatalogo) {
        return jdbc.query("""
                SELECT i.id_item_catalogo, i.codigo, i.nombre
                FROM CAT_ITEM_CATALOGO i
                JOIN CAT_CATALOGO c ON c.id_catalogo = i.id_catalogo
                WHERE c.codigo = :codigoCatalogo
                  AND c.activo = TRUE
                  AND i.activo = TRUE
                ORDER BY i.orden ASC, i.id_item_catalogo ASC
                """, Map.of("codigoCatalogo", codigoCatalogo), ITEM_MAPPER);
    }

    public ItemCatalogo requeridoPorId(Long idItemCatalogo) {
        List<ItemCatalogo> items = jdbc.query("""
                SELECT id_item_catalogo, codigo, nombre
                FROM CAT_ITEM_CATALOGO
                WHERE id_item_catalogo = :idItemCatalogo
                """, Map.of("idItemCatalogo", idItemCatalogo), ITEM_MAPPER);
        if (items.isEmpty()) {
            throw new IllegalStateException("No existe el ítem de catálogo " + idItemCatalogo);
        }
        return items.get(0);
    }
}
