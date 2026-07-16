package com.lps.vitalMagic.purchase.domain.model.entity;

import com.lps.vitalMagic.purchase.domain.exception.InvalidPurchaseException;

import java.math.BigDecimal;
import java.util.Objects;


public record ItemSnapshot(
        Long itemId,
        String itemName,
        BigDecimal unitCost) {

    public ItemSnapshot(Long itemId, String itemName, BigDecimal unitCost) {

        Objects.requireNonNull(unitCost);

        if (itemName == null || itemName.isBlank()){
            throw new InvalidPurchaseException("itemName is required");
        }

        if (unitCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPurchaseException("Unit price must be positive");
        }
        this.itemId = Objects.requireNonNull(itemId);
        this.itemName = itemName;
        this.unitCost = unitCost;
    }
}
