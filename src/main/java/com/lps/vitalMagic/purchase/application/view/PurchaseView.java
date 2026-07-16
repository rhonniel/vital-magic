package com.lps.vitalMagic.purchase.application.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PurchaseView(
   Long id,
   LocalDateTime createdAt,
   BigDecimal totalAmount,
   List<PurchaseItemView> items
) {}
