package com.lps.vitalMagic.purchase.infrastructure.persistance.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;



@Getter
@Entity
@Table(name = "purchase_item")
public class PurchaseItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "purchase_id")
    private Long purchaseId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "unit_cost")
    private BigDecimal unitCost;

    @Column
    private int quantity;


    @Column
    private BigDecimal subtotal;

    protected PurchaseItemEntity(){}


    public PurchaseItemEntity(Long id, Long purchaseId, Long itemId, String itemName, BigDecimal unitCost, int quantity, BigDecimal subtotal) {
        this.id = id;
        this.purchaseId = purchaseId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }
}
