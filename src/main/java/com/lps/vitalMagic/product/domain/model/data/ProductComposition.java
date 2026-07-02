package com.lps.vitalMagic.product.domain.model.data;

import com.lps.vitalMagic.product.domain.model.entity.Product;

public record ProductComposition(
   Product product,
   Composition composition
) {}
