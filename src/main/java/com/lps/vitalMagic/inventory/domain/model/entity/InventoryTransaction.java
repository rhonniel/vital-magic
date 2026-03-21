package com.lps.vitalMagic.inventory.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.exception.InventoryTransactionException;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionOperation;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionType;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="inventory_transaction")
@Getter
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_inventory_id", nullable = false)
    private ItemInventory itemInventory;

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


    private InventoryTransaction(ItemInventory itemInventory, Long sourceId, int quantity, InventoryTransactionType type,BigDecimal unitCost){
        if(quantity<=0){
            throw new InventoryTransactionException("InventoryTransaction quantity should be positive");
        }

        this.itemInventory=Objects.requireNonNull(itemInventory);
        this.sourceId=Objects.requireNonNull(sourceId);
        this.quantity=quantity;
        this.type=type;
        this.unitCost=unitCost;
    }

    public static InventoryTransaction createSale(ItemInventory itemInventory, Long saleId, int quantity) {
        return new InventoryTransaction(itemInventory,saleId,quantity,InventoryTransactionType.SALE, null);
    }

    public static InventoryTransaction createPurchase(ItemInventory itemInventory, Long purchaseId, int quantity, BigDecimal unitCost) {

        Objects.requireNonNull(unitCost);
        if(unitCost.compareTo(BigDecimal.ZERO)<=0){
            throw new InventoryTransactionException("InventoryTransaction unitCost should be positive");
        }

        return new InventoryTransaction(itemInventory,purchaseId,quantity,InventoryTransactionType.PURCHASE,unitCost);

    }



}
