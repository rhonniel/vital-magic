package com.lps.vitalMagic.sales.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_item")
@Getter
public class SaleItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private SaleEntity sale;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column
    private int quantity;

    @Column
    private BigDecimal subtotal;


    protected SaleItemEntity(){}


    public SaleItemEntity(Long id, Long productId, String productName, BigDecimal unitPrice, int quantity, BigDecimal subtotal) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public void assignTo(SaleEntity saleEntity) {
        this.sale = saleEntity;
    }
}
