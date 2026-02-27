package com.lps.vitalMagic.purchase.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name ="purchase_item")
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "purchase_id")
    private Long purchaseId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column
    private int quantity;

    @Column(name = "unit_cost")
    private BigDecimal unitCost;

    @Column
    private BigDecimal subtotal;


}
