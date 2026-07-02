package com.lps.vitalMagic.product.domain.model.data;

import java.util.List;

public record Composition(
        List<IngredientComposition> items
) { }
