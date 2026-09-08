package com.veltia.adaptivequiz.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ACA_ASIGNATURA")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AsignaturaEntity {

    @Id
    @Column(name = "id_asignatura", nullable = false)
    private Long idAsignatura;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private boolean activo;
}
