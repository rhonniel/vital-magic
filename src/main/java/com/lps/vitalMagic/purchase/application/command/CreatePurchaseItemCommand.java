package com.lps.vitalMagic.purchase.application.command;

import java.math.BigDecimal;

public record CreatePurchaseItemCommand(
        Long itemId,
        int quantity,
        BigDecimal unitCost
) {
}
