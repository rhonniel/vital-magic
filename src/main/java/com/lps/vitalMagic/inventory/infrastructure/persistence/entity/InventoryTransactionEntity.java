package com.lps.vitalMagic.inventory.infrastructure.persistence.entity;


import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="inventory_transaction")
@Getter
public class InventoryTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_inventory_id")
    private Long itemInventoryId;

    @Column(name ="source_id")
    private Long sourceId;

    @Column
    @Enumerated(EnumType.STRING)
    private InventoryTransactionType type;

    @Column
    private int quantity;

    @Column(name="unit_cost")
    private BigDecimal unitCost;

    @Column(name="process_at")
    private LocalDateTime processAt;

    protected InventoryTransactionEntity() {}

    public InventoryTransactionEntity(Long id, Long itemInventoryId, Long sourceId, InventoryTransactionType type,
                                      int quantity, BigDecimal unitCost, LocalDateTime processAt) {
        this.id = id;
        this.itemInventoryId = itemInventoryId;
        this.sourceId = sourceId;
        this.type = type;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.processAt = processAt;
    }
}
