package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import java.util.List;
import java.util.Optional;

/** Resolución semántica de catálogos; evita acoplar el dominio a IDs físicos. */
public interface CatalogoRepository {

    Optional<ItemCatalogo> findItemActivo(String codigoCatalogo, String codigoItem);

    List<ItemCatalogo> findItemsActivos(String codigoCatalogo);
}
