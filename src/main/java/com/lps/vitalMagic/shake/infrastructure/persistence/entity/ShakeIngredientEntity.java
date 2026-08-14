package com.lps.vitalMagic.shake.infrastructure.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "shake_ingredient")
@Getter
public class ShakeIngredientEntity {

    @EmbeddedId
    private ShakeIngredientId id;


    @MapsId("shakeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shake_id", nullable = false)
    private ShakeEntity shake;


    @Column
    private int quantity;

    protected ShakeIngredientEntity() {
    }

    public ShakeIngredientEntity(Long itemId, int quantity) {
        this.id = new ShakeIngredientId(null, itemId);
        this.quantity = quantity;
    }

    public void assignTo(ShakeEntity shakeEntity) {
        this.shake = shakeEntity;
    }
}
