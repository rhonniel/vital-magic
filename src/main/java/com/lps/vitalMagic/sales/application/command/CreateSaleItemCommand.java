package com.lps.vitalMagic.sales.application.command;

import java.math.BigDecimal;

public record CreateSaleItemCommand(
        Long productId,
        int quantity
) {
}
