package com.lps.vitalMagic.purchase.application.query;

import java.time.LocalDate;

public record SearchPurchasesQuery(
         LocalDate from,
         LocalDate to,
         Long itemId
) {
}
