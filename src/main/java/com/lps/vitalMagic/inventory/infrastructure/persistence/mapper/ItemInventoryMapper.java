package com.lps.vitalMagic.inventory.infrastructure.persistence.mapper;


import com.lps.vitalMagic.inventory.domain.model.entity.ItemInventory;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemInventoryEntity;


public class ItemInventoryMapper {

    public static ItemInventoryEntity toEntity(ItemInventory domain) {
        return new ItemInventoryEntity(domain.getId(),domain.getItemId(),domain.getUnitCost(),domain.getMinStock(), domain.getCurrentStock(), domain.isActive()) ;
    }


    public static ItemInventory toDomain(ItemInventoryEntity entity) {
        return ItemInventory.from(entity.getId(), entity.getItemId(),entity.getMinStock(),entity.getCurrentStock(),entity.getUnitCost());
    }
}
