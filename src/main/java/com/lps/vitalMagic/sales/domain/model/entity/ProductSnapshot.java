package com.lps.vitalMagic.sales.domain.model.entity;

import com.lps.vitalMagic.sales.domain.exception.InvalidSaleItemException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductSnapshot {

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;


    public ProductSnapshot(Long productId, String productName, BigDecimal unitPrice) {

        Objects.requireNonNull(unitPrice);

        if (productName == null || productName.isBlank()){
            throw new InvalidSaleItemException("productName is required");
        }

        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidSaleItemException("Unit price must be positive");
        }
        this.productId = Objects.requireNonNull(productId);
        this.productName = productName;
        this.unitPrice = unitPrice;
    }
}
