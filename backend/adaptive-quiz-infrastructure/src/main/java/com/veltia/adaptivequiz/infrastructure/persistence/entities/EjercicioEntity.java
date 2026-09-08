package com.veltia.adaptivequiz.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BAN_EJERCICIO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EjercicioEntity {

    @Id
    @Column(name = "id_ejercicio", nullable = false)
    private Long idEjercicio;

    @Column(name = "id_tema", nullable = false)
    private Long idTema;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "enunciado", nullable = false)
    private String enunciado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item_tipo_ejercicio", insertable = false, updatable = false)
    private CatalogoItemEntity tipoEjercicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item_dificultad", insertable = false, updatable = false)
    private CatalogoItemEntity dificultad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item_estado_ejercicio", insertable = false, updatable = false)
    private CatalogoItemEntity estado;

    @Column(name = "explicacion")
    private String explicacion;

    @Column(name = "tiempo_objetivo_segundos")
    private Integer tiempoObjetivoSegundos;

    @Column(name = "puntaje_base", nullable = false)
    private BigDecimal puntajeBase;
}
