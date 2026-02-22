package com.lps.vitalMagic.sales.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sale_item")
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sale_id")
    private Long saleId;

    @Column(name = "item_inventory_Id")
    private Long itemInventoryId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column
    private int quantity;

    @Column
    private BigDecimal subtotal;


}
