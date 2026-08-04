package com.lps.vitalMagic.sales.application.query;

import com.lps.vitalMagic.common.presentation.pagination.Pagination;

import java.time.LocalDate;

public record SearchSaleQuery(
        LocalDate from,
        LocalDate to,
        Long productId,
        Pagination pagination
) {
}
