package com.veltia.adaptivequiz.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BAN_PISTA_EJERCICIO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PistaEjercicioEntity {

    @Id
    @Column(name = "id_pista_ejercicio", nullable = false)
    private Long idPistaEjercicio;

    @Column(name = "id_ejercicio", nullable = false)
    private Long idEjercicio;

    @Column(name = "orden", nullable = false)
    private int orden;

    @Column(name = "texto", nullable = false)
    private String texto;

    @Column(name = "penalizacion_puntaje", nullable = false)
    private BigDecimal penalizacionPuntaje;
}
