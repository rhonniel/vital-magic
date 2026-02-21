package com.lps.vitalMagic.shake.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class ShakeIngredientId {

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "shake_id")
    private Long shakeId;


}
