package com.lps.vitalMagic.sales.domain.model.input;

import com.lps.vitalMagic.sales.domain.model.entity.ProductSnapshot;


public record SaleItemInput(
        ProductSnapshot productSnapshot,
        int quantity
) {
}
