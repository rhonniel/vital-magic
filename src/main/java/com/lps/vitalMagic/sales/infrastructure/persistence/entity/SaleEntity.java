package com.lps.vitalMagic.sales.infrastructure.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
public class SaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "sale_id")
    private List<SaleItemEntity> items =new ArrayList<>();

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    protected SaleEntity() {
    }

    public SaleEntity(Long id, List<SaleItemEntity> items, BigDecimal totalAmount) {
        this.id = id;
        this.items = items;
        this.totalAmount = totalAmount;
    }
}
