package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import java.util.Optional;

public interface ContextoAprendizajeRepository {

    ContextoAprendizaje guardar(ContextoAprendizaje contexto);

    Optional<ContextoAprendizaje> findContextoById(Long idContextoAprendizaje);
}
