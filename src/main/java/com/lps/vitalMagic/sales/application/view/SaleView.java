package com.lps.vitalMagic.sales.application.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SaleView(
        Long id,
        LocalDateTime createAt,
        BigDecimal totalAmount,
        List<SaleItemView> items
) {
}
