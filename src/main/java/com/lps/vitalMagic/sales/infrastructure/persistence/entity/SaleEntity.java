package com.lps.vitalMagic.sales.infrastructure.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sale")
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

    @Column(name="created_at")
    private LocalDateTime createAt;

    protected SaleEntity() {
    }

    public SaleEntity(Long id, List<SaleItemEntity> items, BigDecimal totalAmount,LocalDateTime createAt) {
        this.id = id;
        this.items = items;
        this.totalAmount = totalAmount;
        this.createAt = createAt;
    }
}
