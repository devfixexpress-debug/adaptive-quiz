package com.veltia.adaptivequiz.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CAT_ITEM_CATALOGO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatalogoItemEntity {

    @Id
    @Column(name = "id_item_catalogo", nullable = false)
    private Long idItemCatalogo;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "nombre", nullable = false)
    private String nombre;
}
