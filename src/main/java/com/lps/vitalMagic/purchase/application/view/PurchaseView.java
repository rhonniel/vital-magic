package com.lps.vitalMagic.purchase.application.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PurchaseView(
   Long id,
   LocalDate createAt,
   BigDecimal totalAmount,
   List<PurchaseItemView> items
) {}
