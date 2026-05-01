package com.lps.vitalMagic.sales.infrastructure.persistence.mapper;

import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleEntity;

public class SaleMapper {


    public static SaleEntity toEntity(Sale domain){
        return new SaleEntity(domain.getId(), domain.getItems().stream().map(SaleItemMapper::toEntity).toList(),domain.getTotalAmount());
    }

    public static Sale toDomain(SaleEntity entity){
        return Sale.from(entity.getId(),entity.getItems().stream().map(SaleItemMapper::toDomain).toList(),entity.getTotalAmount());
    }


}
