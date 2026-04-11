package com.lps.vitalMagic.shake.domain.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@EqualsAndHashCode
@Getter
public class ShakeIngredientId {

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "shake_id")
    private Long shakeId;

    public ShakeIngredientId(Long shakeId, Long itemId) {
        this.shakeId = shakeId;
        this.itemId = itemId;
    }



}
