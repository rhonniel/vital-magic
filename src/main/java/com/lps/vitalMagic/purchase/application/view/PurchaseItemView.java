package com.lps.vitalMagic.purchase.application.view;

import java.math.BigDecimal;

public record PurchaseItemView(
        Long itemId,
        String itemName,
        int quantity,
        BigDecimal unitCost,
        BigDecimal subtotal
)
{}
