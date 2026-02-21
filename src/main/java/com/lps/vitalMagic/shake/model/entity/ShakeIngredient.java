package com.lps.vitalMagic.shake.model.entity;

import com.lps.vitalMagic.shake.model.embeddable.ShakeIngredientId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "shake_ingredient")
public class ShakeIngredient {
    @EmbeddedId
    private ShakeIngredientId shakeIngredientId;

    @Column
    private int quantity;



}
