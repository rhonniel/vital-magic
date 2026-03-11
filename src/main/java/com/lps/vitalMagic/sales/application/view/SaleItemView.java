package com.lps.vitalMagic.sales.application.view;

import java.math.BigDecimal;

public record SaleItemView(
        Long itemId,
        String itemName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
