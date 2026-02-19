package com.lps.vitalMagic.inventory.model.entity;

import com.lps.vitalMagic.inventory.model.enums.InventoryTransactionConcept;
import com.lps.vitalMagic.inventory.model.enums.InventoryTransactionOperation;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="inventory_transaction")
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_inventory_id", nullable = false)
    private ItemInventory itemInventory;

    @Column(name ="source_id")
    private Long sourceId;

    @Column(name="concept")
    @Enumerated(EnumType.STRING)
    private InventoryTransactionConcept concept;

    @Enumerated(EnumType.STRING)
    @Column
    private InventoryTransactionOperation operation;

    @Column
    private int quantity;

    @Column(name="unit_cost")
    private BigDecimal unitCost;

    @Column(name="process_at")
    private LocalDateTime processAt;



}
