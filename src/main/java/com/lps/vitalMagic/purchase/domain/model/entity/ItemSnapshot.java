package com.lps.vitalMagic.purchase.domain.model.entity;

import com.lps.vitalMagic.purchase.domain.exception.InvalidPurchaseException;

import java.math.BigDecimal;
import java.util.Objects;


public record ItemSnapshot(
        Long productId,
        String productName,
        BigDecimal unitCost) {

    public ItemSnapshot(Long productId, String productName, BigDecimal unitCost) {

        Objects.requireNonNull(unitCost);

        if (productName == null || productName.isBlank()){
            throw new InvalidPurchaseException("productName is required");
        }

        if (unitCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPurchaseException("Unit price must be positive");
        }
        this.productId = Objects.requireNonNull(productId);
        this.productName = productName;
        this.unitCost = unitCost;
    }
}
