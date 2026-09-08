package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.ParametroConfiguracion;
import java.util.Optional;

public interface ParametroRepository {

    Optional<ParametroConfiguracion> findActivoByCodigo(String codigo);
}
