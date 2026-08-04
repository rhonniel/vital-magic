package com.lps.vitalMagic.purchase.application.query;

import com.lps.vitalMagic.common.presentation.pagination.Pagination;

import java.time.LocalDate;

public record SearchPurchasesQuery(
         LocalDate from,
         LocalDate to,
         Long itemId,
         Pagination pagination
) {
}
