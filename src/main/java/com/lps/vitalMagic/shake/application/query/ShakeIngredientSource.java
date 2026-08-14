package com.lps.vitalMagic.shake.application.query;

import java.util.Objects;

public record ShakeIngredientSource(
        Long itemId,
        int quantity
) {

    public ShakeIngredientSource {
        Objects.requireNonNull(itemId);

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Ingredient quantity should be greater than zero"
            );
        }
    }
}