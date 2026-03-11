package com.lps.vitalMagic.shake.domain.model.entity;

import com.lps.vitalMagic.shake.domain.model.embeddable.ShakeIngredientId;
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
