package com.lps.vitalMagic.shake.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
public class ShakeIngredientId implements Serializable {

    @Column(name = "shake_id")
    private Long shakeId;

    @Column(name = "item_id")
    private Long itemId;

    public ShakeIngredientId() {
    }

    public ShakeIngredientId(Long shakeId, Long itemId) {
        this.shakeId = shakeId;
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShakeIngredientId that = (ShakeIngredientId) o;
        return Objects.equals(shakeId, that.shakeId) && Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shakeId, itemId);
    }
}
