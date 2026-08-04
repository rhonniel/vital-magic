package com.lps.vitalMagic.shake.infrastructure.persistence.projection;

public record ShakeIngredientProjection(
        Long shakeId,
        Long itemId,
        String itemName,
        Integer quantity
) {
}
