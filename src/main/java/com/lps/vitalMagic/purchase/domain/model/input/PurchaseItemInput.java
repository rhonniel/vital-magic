package com.lps.vitalMagic.purchase.domain.model.input;

import com.lps.vitalMagic.purchase.domain.model.entity.ItemSnapshot;

public record PurchaseItemInput(
        ItemSnapshot itemSnapshot,
        int quantity
) {}
