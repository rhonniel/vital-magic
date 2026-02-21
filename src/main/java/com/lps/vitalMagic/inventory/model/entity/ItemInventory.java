package com.lps.vitalMagic.inventory.model.entity;

import com.lps.vitalMagic.core.entity.Auditory;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "item_inventory")
public class ItemInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name="unit_cost")
    private BigDecimal unitCost;

    @Column(name = "min_stock")
    private int minStock;

    @Column(name="current_stock")
    private int currentStock;

    @Column
    private boolean active;

    @Embedded
    private Auditory auditory;


}
