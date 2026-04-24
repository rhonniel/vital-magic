package com.lps.vitalMagic.inventory.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.exception.InventoryTransactionException;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Getter
public class InventoryTransaction {

    private Long id;

    private Long itemInventoryId;


    private Long sourceId;


    private InventoryTransactionType type;


    private int quantity;


    private BigDecimal unitCost;

    private LocalDateTime processAt;

    private InventoryTransaction() {
    }

    private InventoryTransaction(Long itemInventoryId, Long sourceId, int quantity, InventoryTransactionType type, BigDecimal unitCost){
        if(quantity<=0){
            throw new InventoryTransactionException("InventoryTransaction quantity should be positive");
        }

        this.itemInventoryId=Objects.requireNonNull(itemInventoryId);
        this.sourceId=Objects.requireNonNull(sourceId);
        this.quantity=quantity;
        this.type=type;
        this.unitCost=unitCost;
    }

    public static InventoryTransaction createSale(Long itemInventoryId, Long saleId, int quantity) {
        return new InventoryTransaction(itemInventoryId,saleId,quantity,InventoryTransactionType.SALE, null);
    }

    public static InventoryTransaction createPurchase(Long itemInventoryId, Long purchaseId, int quantity, BigDecimal unitCost) {

        Objects.requireNonNull(unitCost);
        if(unitCost.compareTo(BigDecimal.ZERO)<=0){
            throw new InventoryTransactionException("InventoryTransaction unitCost should be positive");
        }

        return new InventoryTransaction(itemInventoryId,purchaseId,quantity,InventoryTransactionType.PURCHASE,unitCost);

    }

    public static InventoryTransaction from(Long id,Long itemInventoryId, Long sourceId, int quantity, InventoryTransactionType type,BigDecimal unitCost, LocalDateTime processAt){
        InventoryTransaction inventoryTransaction= new InventoryTransaction();
        inventoryTransaction.id=id;
        inventoryTransaction.itemInventoryId=itemInventoryId;
        inventoryTransaction.type=type;
        inventoryTransaction.unitCost=unitCost;
        inventoryTransaction.quantity=quantity;
        inventoryTransaction.sourceId=sourceId;
        inventoryTransaction.processAt=processAt;

        return inventoryTransaction;
    }



}
