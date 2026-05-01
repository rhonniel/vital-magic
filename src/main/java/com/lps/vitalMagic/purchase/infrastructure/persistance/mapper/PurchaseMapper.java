package com.lps.vitalMagic.purchase.infrastructure.persistance.mapper;

import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;
import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseEntity;


public class PurchaseMapper {

    public static PurchaseEntity toEntity(Purchase domain){
        return new PurchaseEntity(domain.getId(),
                domain.getItems().stream().map(PurchaseItemMapper::toEntity).toList(),
                domain.getTotalAmount());
    }

    public static Purchase toDomain(PurchaseEntity entity){
        return Purchase.from(entity.getId(),entity.getItems().stream().map(PurchaseItemMapper::toDomain).toList(),entity.getTotalAmount());
    }
}
