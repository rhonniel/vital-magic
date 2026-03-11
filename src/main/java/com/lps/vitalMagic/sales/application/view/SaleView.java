package com.lps.vitalMagic.sales.application.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SaleView(
        Long id,
        LocalDate createAt,
        BigDecimal totalAmount,
        List<SaleItemView> items
) {
}
