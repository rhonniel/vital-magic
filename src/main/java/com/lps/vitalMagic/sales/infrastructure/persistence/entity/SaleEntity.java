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

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItemEntity> items = new ArrayList<>();

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    protected SaleEntity() {
    }

    public SaleEntity(Long id, BigDecimal totalAmount,LocalDateTime createdAt) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    public void addItem(SaleItemEntity item) {
        items.add(item);
        item.assignTo(this);
    }
}
