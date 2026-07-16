package com.lps.vitalMagic.sales.infrastructure.persistence.mapper;

import com.lps.vitalMagic.sales.domain.model.entity.SaleItem;
import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleItemEntity;

public class SaleItemMapper {

    public static SaleItemEntity toEntity(Long saleId,SaleItem domain){
        return  new SaleItemEntity(domain.getId(),saleId,domain.getProductSnapshot().getProductId(),
                domain.getProductSnapshot().getProductName(),domain.getProductSnapshot().getUnitPrice(), domain.getQuantity(), domain.getSubtotal());
    }

    public static SaleItem toDomain(SaleItemEntity entity){
        return SaleItem.from(entity.getId(),entity.getProductId(),entity.getProductName(),entity.getUnitPrice(),entity.getQuantity(),entity.getSubtotal());
    }
}
