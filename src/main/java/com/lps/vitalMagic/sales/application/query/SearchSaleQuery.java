package com.lps.vitalMagic.sales.application.query;

import java.time.LocalDate;

public record SearchSaleQuery(
        LocalDate from,
        LocalDate to,
        Long productId
) {
}
