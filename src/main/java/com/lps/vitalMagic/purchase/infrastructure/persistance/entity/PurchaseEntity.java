package com.lps.vitalMagic.purchase.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase")
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

    @Column(name= "created_at")
    private LocalDateTime createdAt;

    protected PurchaseEntity() {
    }

    public PurchaseEntity(Long id, List<PurchaseItemEntity> items, BigDecimal totalAmount,LocalDateTime createdAt) {
        this.id = id;
        this.items = items;
        this.totalAmount = totalAmount;
        this.createdAt=createdAt;
    }

}
