package com.lps.vitalMagic.shake.application.query;

import java.util.List;
import java.util.Objects;

public record ShakeProductSource(
        Long shakeId,
        String name,
        List<ShakeIngredientSource> ingredients
) {

    public ShakeProductSource {
        Objects.requireNonNull(shakeId);
        Objects.requireNonNull(name);
        ingredients = List.copyOf(
                Objects.requireNonNull(ingredients)
        );
    }
}