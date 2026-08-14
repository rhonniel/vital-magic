package com.lps.vitalMagic.product.domain.model.data;


import com.lps.vitalMagic.product.domain.exception.InvalidProductException;

import java.math.BigDecimal;
import java.util.Objects;

public record IngredientCost(
        Long itemId,
        int quantity,
        BigDecimal unitCost) {

    public IngredientCost {
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(unitCost);

        if (quantity <= 0) {
            throw new InvalidProductException(
                    "Ingredient quantity should be greater than zero"
            );
        }

        if (unitCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductException(
                    "Ingredient unit cost should be greater than zero"
            );
        }
    }

    public BigDecimal totalCost() {
        return unitCost.multiply(BigDecimal.valueOf(quantity));
    }
}

