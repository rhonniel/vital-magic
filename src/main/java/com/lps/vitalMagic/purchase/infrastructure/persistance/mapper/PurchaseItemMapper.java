package com.lps.vitalMagic.purchase.infrastructure.persistance.mapper;

import com.lps.vitalMagic.purchase.domain.model.entity.PurchaseItem;
import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseItemEntity;

public class PurchaseItemMapper {
    public static PurchaseItemEntity toEntity(Long purchaseId, PurchaseItem domain) {
        return new PurchaseItemEntity(domain.getId(),purchaseId,domain.getItem().itemId(),
                domain.getItem().itemName(),domain.getItem().unitCost(),
                domain.getQuantity(),domain.getSubtotal());
    }

    public static PurchaseItem toDomain(PurchaseItemEntity purchaseItemEntity) {
        return PurchaseItem.from(purchaseItemEntity.getId(),
                purchaseItemEntity.getItemId(),purchaseItemEntity.getItemName(),
                purchaseItemEntity.getUnitCost(),purchaseItemEntity.getQuantity(),purchaseItemEntity.getSubtotal());
    }
}
