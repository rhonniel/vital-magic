package com.lps.vitalMagic.product.infrastructure.persistance.mapper;

import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.infrastructure.persistance.entity.ProductEntity;

public class ProductMapper {
    public static ProductEntity toEntity(Product domain){
        return  new ProductEntity(domain.getId(), domain.getReferenceNo(), domain.getProductType(),
                domain.getName(),domain.getPrice(), domain.isActive());
    }

    public static Product toDomain(ProductEntity entity){
        return Product.from(entity.getId(), entity.getReferenceNo(), entity.getProductType(),
                entity.getName(),entity.getPrice(),entity.isActive());
    }
}
