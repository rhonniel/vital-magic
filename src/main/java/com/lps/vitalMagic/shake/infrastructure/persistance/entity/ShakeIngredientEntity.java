package com.lps.vitalMagic.shake.infrastructure.persistance.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "shake_ingredient")
@Getter
public class ShakeIngredientEntity {

    @Column
    private Long itemId;

    @Column
    private Long shakeId;


    @Column
    private int quantity;

    protected ShakeIngredientEntity() {
    }

    public ShakeIngredientEntity(Long itemId, Long shakeId, int quantity) {
        this.itemId = itemId;
        this.shakeId = shakeId;
        this.quantity = quantity;
    }
}
