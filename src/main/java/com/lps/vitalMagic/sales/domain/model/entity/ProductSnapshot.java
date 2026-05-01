package com.lps.vitalMagic.sales.domain.model.entity;

import com.lps.vitalMagic.sales.domain.exception.InvalidSaleException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;


@Getter
public class ProductSnapshot {


    private Long productId;


    private String productName;

    private BigDecimal unitPrice;


    public ProductSnapshot(Long productId, String productName, BigDecimal unitPrice) {

        Objects.requireNonNull(unitPrice);

        if (productName == null || productName.isBlank()){
            throw new InvalidSaleException("productName is required");
        }

        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidSaleException("Unit price must be positive");
        }
        this.productId = Objects.requireNonNull(productId);
        this.productName = productName;
        this.unitPrice = unitPrice;
    }
}
