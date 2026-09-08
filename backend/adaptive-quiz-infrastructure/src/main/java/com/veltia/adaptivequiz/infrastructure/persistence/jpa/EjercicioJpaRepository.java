package com.veltia.adaptivequiz.infrastructure.persistence.jpa;

import com.veltia.adaptivequiz.infrastructure.persistence.entities.EjercicioEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EjercicioJpaRepository extends JpaRepository<EjercicioEntity, Long> {

    @Query(value = """
            SELECT e.*
            FROM BAN_EJERCICIO e
            WHERE e.id_tema = :idTema
              AND e.id_item_dificultad = :idItemDificultad
              AND e.id_item_tipo_ejercicio = :idItemTipoEjercicio
              AND EXISTS (
                    SELECT 1
                    FROM CAT_ITEM_CATALOGO i
                    JOIN CAT_CATALOGO c ON c.id_catalogo = i.id_catalogo
                    WHERE i.id_item_catalogo = e.id_item_estado_ejercicio
                      AND c.codigo = 'ESTADO_EJERCICIO'
                      AND i.codigo = 'PUBLICADO'
                      AND i.activo = TRUE
              )
            ORDER BY e.id_ejercicio ASC
            """, nativeQuery = true)
    List<EjercicioEntity> findPublicadosPorTemaDificultadYTipo(
            @Param("idTema") Long idTema,
            @Param("idItemDificultad") Long idItemDificultad,
            @Param("idItemTipoEjercicio") Long idItemTipoEjercicio);

    @Query(value = """
            SELECT e.*
            FROM BAN_EJERCICIO e
            WHERE e.id_tema = :idTema
              AND e.id_item_tipo_ejercicio = :idItemTipoEjercicio
              AND EXISTS (
                    SELECT 1
                    FROM CAT_ITEM_CATALOGO i
                    JOIN CAT_CATALOGO c ON c.id_catalogo = i.id_catalogo
                    WHERE i.id_item_catalogo = e.id_item_estado_ejercicio
                      AND c.codigo = 'ESTADO_EJERCICIO'
                      AND i.codigo = 'PUBLICADO'
                      AND i.activo = TRUE
              )
            ORDER BY e.id_ejercicio ASC
            """, nativeQuery = true)
    List<EjercicioEntity> findPublicadosPorTemaYTipo(
            @Param("idTema") Long idTema,
            @Param("idItemTipoEjercicio") Long idItemTipoEjercicio);
}
