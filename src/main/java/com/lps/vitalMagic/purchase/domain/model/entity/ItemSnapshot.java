package com.lps.vitalMagic.purchase.domain.model.entity;

import com.lps.vitalMagic.purchase.domain.exception.InvalidPurchaseItemException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ItemSnapshot {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "unit_cost")
    private BigDecimal unitCost;


    public ItemSnapshot(Long productId, String productName, BigDecimal unitCost) {

        Objects.requireNonNull(unitCost);

        if (productName == null || productName.isBlank()){
            throw new InvalidPurchaseItemException("productName is required");
        }

        if (unitCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPurchaseItemException("Unit price must be positive");
        }
        this.productId = Objects.requireNonNull(productId);
        this.productName = productName;
        this.unitCost = unitCost;
    }
}
