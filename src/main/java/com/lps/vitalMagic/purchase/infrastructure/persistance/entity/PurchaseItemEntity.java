package com.lps.vitalMagic.purchase.infrastructure.persistance.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;



@Getter
public class PurchaseItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "purchase_id")
    private Long purchaseId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "unit_cost")
    private BigDecimal unitCost;

    @Column
    private int quantity;


    @Column
    private BigDecimal subtotal;

    protected PurchaseItemEntity(){}


    public PurchaseItemEntity(Long id, Long purchaseId, Long productId, String productName, BigDecimal unitCost, int quantity, BigDecimal subtotal) {
        this.id = id;
        this.purchaseId = purchaseId;
        this.productId = productId;
        this.productName = productName;
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }
}
