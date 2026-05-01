package com.lps.vitalMagic.purchase.infrastructure.persistance.mapper;

import com.lps.vitalMagic.purchase.domain.model.entity.PurchaseItem;
import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseItemEntity;

public class PurchaseItemMapper {
    public static PurchaseItemEntity toEntity(PurchaseItem domain) {
        return new PurchaseItemEntity(domain.getId(), domain.getPurchaseId(),domain.getItem().productId(),
                domain.getItem().productName(),domain.getItem().unitCost(),
                domain.getQuantity(),domain.getSubtotal());
    }

    public static PurchaseItem toDomain(PurchaseItemEntity purchaseItemEntity) {
        return PurchaseItem.from(purchaseItemEntity.getId(),purchaseItemEntity.getPurchaseId(),
                purchaseItemEntity.getProductId(),purchaseItemEntity.getProductName(),
                purchaseItemEntity.getUnitCost(),purchaseItemEntity.getQuantity(),purchaseItemEntity.getSubtotal());
    }
}
