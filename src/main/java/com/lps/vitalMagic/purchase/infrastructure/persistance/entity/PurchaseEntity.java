package com.lps.vitalMagic.purchase.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
public class PurchaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "purchase_id")
    private List<PurchaseItemEntity> items= new ArrayList<>();

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    protected PurchaseEntity() {
    }

    public PurchaseEntity(Long id, List<PurchaseItemEntity> items, BigDecimal totalAmount) {
        this.id = id;
        this.items = items;
        this.totalAmount = totalAmount;
    }

}
