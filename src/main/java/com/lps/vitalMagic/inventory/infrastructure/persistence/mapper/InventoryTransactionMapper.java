package com.lps.vitalMagic.inventory.infrastructure.persistence.mapper;


import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.InventoryTransactionEntity;



public class InventoryTransactionMapper  {

    public static InventoryTransactionEntity toEntity(InventoryTransaction domain) {
        return  new InventoryTransactionEntity(domain.getId(), domain.getItemId(), domain.getSourceId(),domain.getType(),domain.getQuantity(),domain.getUnitCost(),domain.getProcessAt());
    }


    public static InventoryTransaction toDomain(InventoryTransactionEntity entity) {
        return  InventoryTransaction.from(entity.getId(), entity.getItemId(), entity.getSourceId(),
                entity.getQuantity(),entity.getType(),entity.getUnitCost(),entity.getProcessAt());
    }
}
