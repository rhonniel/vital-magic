package com.lps.vitalMagic.inventory.infrastructure.persistence.mapper;

import com.lps.vitalMagic.inventory.domain.model.entity.ItemInventory;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemInventoryEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemInventoryMapper implements BaseMapper<ItemInventory, ItemInventoryEntity>{

    @Override
    public ItemInventoryEntity toEntity(ItemInventory domain) {
        return new ItemInventoryEntity(domain.getId(),domain.getItemId(),domain.getUnitCost(),domain.getMinStock(), domain.getCurrentStock(), domain.isActive()) ;
    }

    @Override
    public ItemInventory toDomain(ItemInventoryEntity entity) {
        return ItemInventory.from(entity.getId(), entity.getItemId(),entity.getMinStock(),entity.getCurrentStock(),entity.getUnitCost());
    }
}
