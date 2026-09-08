package com.veltia.adaptivequiz.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BAN_OPCION_EJERCICIO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OpcionEjercicioEntity {

    @Id
    @Column(name = "id_opcion_ejercicio", nullable = false)
    private Long idOpcionEjercicio;

    @Column(name = "id_ejercicio", nullable = false)
    private Long idEjercicio;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "texto", nullable = false)
    private String texto;

    @Column(name = "es_correcta", nullable = false)
    private boolean esCorrecta;

    @Column(name = "orden", nullable = false)
    private int orden;

    @Column(name = "retroalimentacion")
    private String retroalimentacion;
}
