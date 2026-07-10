package com.lps.vitalMagic.sales.application.query;

import com.lps.vitalMagic.sales.application.pagination.Pagination;

import java.time.LocalDate;

public record SearchSaleQuery(
        LocalDate from,
        LocalDate to,
        Long productId,
        Pagination pagination
) {
}
