package com.lps.vitalMagic.inventory.infrastructure.persistence.mapper;

import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.InventoryTransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class InventoryTransactionMapper implements  BaseMapper<InventoryTransaction, InventoryTransactionEntity>{
    @Override
    public InventoryTransactionEntity toEntity(InventoryTransaction domain) {
        return  new InventoryTransactionEntity(domain.getId(), domain.getItemInventoryId(), domain.getSourceId(),domain.getType(),domain.getQuantity(),domain.getUnitCost(),domain.getProcessAt());
    }

    @Override
    public InventoryTransaction toDomain(InventoryTransactionEntity entity) {
        return  InventoryTransaction.from(entity.getId(), entity.getItemInventoryId(), entity.getSourceId(),
                entity.getQuantity(),entity.getType(),entity.getUnitCost(),entity.getProcessAt());
    }
}
