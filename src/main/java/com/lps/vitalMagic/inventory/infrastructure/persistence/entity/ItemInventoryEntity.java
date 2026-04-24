package com.lps.vitalMagic.inventory.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "item_inventory")
@Getter
public class ItemInventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="item_id")
    private  Long itemId;

    @Column(name="unit_cost")
    private BigDecimal unitCost;

    @Column(name = "min_stock")
    private int minStock;

    @Column(name="current_stock")
    private int currentStock;

    @Column
    private boolean active;

    protected ItemInventoryEntity(){}

    public ItemInventoryEntity(Long id, Long itemId, BigDecimal unitCost, int minStock, int currentStock, boolean active) {
        this.id = id;
        this.itemId = itemId;
        this.unitCost = unitCost;
        this.minStock = minStock;
        this.currentStock = currentStock;
        this.active = active;
    }
}
