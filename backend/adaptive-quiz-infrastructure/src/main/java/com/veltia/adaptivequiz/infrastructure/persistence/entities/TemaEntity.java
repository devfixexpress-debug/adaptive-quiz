package com.veltia.adaptivequiz.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ACA_TEMA")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemaEntity {

    @Id
    @Column(name = "id_tema", nullable = false)
    private Long idTema;

    @Column(name = "id_asignatura", nullable = false)
    private Long idAsignatura;

    @Column(name = "id_tema_padre")
    private Long idTemaPadre;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "orden", nullable = false)
    private int orden;

    @Column(name = "activo", nullable = false)
    private boolean activo;
}
