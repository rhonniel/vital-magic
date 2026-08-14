package com.lps.vitalMagic.product.domain.model.data;


import com.lps.vitalMagic.product.domain.exception.InvalidProductException;

import java.util.List;
import java.util.Objects;

public record ShakeProductData(
        Long referenceNo,
        String name,
        List<IngredientCost> ingredients
) {

    public ShakeProductData {
        Objects.requireNonNull(referenceNo);
        Objects.requireNonNull(name);
        ingredients = List.copyOf(
                Objects.requireNonNull(ingredients)
        );

        if (name.isBlank()) {
            throw new InvalidProductException(
                    "Product name should not be blank"
            );
        }

        if (ingredients.isEmpty()) {
            throw new InvalidProductException(
                    "Shake product should have ingredients"
            );
        }
    }
}