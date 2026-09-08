package com.veltia.adaptivequiz.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.veltia.adaptivequiz.application.exception.RecursoNoEncontradoException;
import com.veltia.adaptivequiz.domain.model.Asignatura;
import com.veltia.adaptivequiz.domain.model.Tema;
import com.veltia.adaptivequiz.domain.repository.AsignaturaRepository;
import com.veltia.adaptivequiz.domain.repository.TemaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CatalogoAcademicoQueryServiceTest {

    @Test
    void consulta_asignaturas_y_temas_mediante_puertos_de_dominio() {
        Asignatura matematica = new Asignatura(1L, "MAT", "Matemática", "Demo", true);
        Tema algebra = new Tema(2L, 1L, null, "ALG", "Álgebra", "Demo", 10, true);
        CatalogoAcademicoQueryService service = new CatalogoAcademicoQueryService(
                new AsignaturaRepositoryFake(List.of(matematica)),
                idAsignatura -> idAsignatura.equals(1L) ? List.of(algebra) : List.of());

        assertEquals(List.of("MAT"), service.consultarAsignaturas().stream()
                .map(Asignatura::codigo)
                .toList());
        assertEquals(List.of("ALG"), service.consultarTemasDeAsignatura(1L).stream().map(Tema::codigo).toList());
    }

    @Test
    void informa_cuando_la_asignatura_no_existe_o_no_esta_activa() {
        CatalogoAcademicoQueryService service = new CatalogoAcademicoQueryService(
                new AsignaturaRepositoryFake(List.of()),
                idAsignatura -> List.of());

        assertThrows(RecursoNoEncontradoException.class, () -> service.consultarTemasDeAsignatura(99L));
    }

    private static final class AsignaturaRepositoryFake implements AsignaturaRepository {
        private final List<Asignatura> asignaturas;

        private AsignaturaRepositoryFake(List<Asignatura> asignaturas) {
            this.asignaturas = asignaturas;
        }

        @Override
        public List<Asignatura> findAllActivas() {
            return asignaturas.stream().filter(Asignatura::activo).toList();
        }

        @Override
        public Optional<Asignatura> findActivaById(Long idAsignatura) {
            return asignaturas.stream()
                    .filter(Asignatura::activo)
                    .filter(asignatura -> asignatura.idAsignatura().equals(idAsignatura))
                    .findFirst();
        }
    }
}
