package com.lps.vitalMagic.sales.infrastructure.persistence.mapper;

import com.lps.vitalMagic.common.pagination.PageResult;
import com.lps.vitalMagic.sales.application.view.SaleItemView;
import com.lps.vitalMagic.sales.application.view.SaleView;
import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleEntity;
import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleItemEntity;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
//TODO nada testea el mapeo ni la infra en general
public class SaleMapper {


    public static SaleEntity toEntity(Sale domain){
        SaleEntity entity = new SaleEntity(domain.getId(),domain.getTotalAmount(),domain.getCreateAt());
        domain.getItems().forEach(item -> {
            entity.addItem(SaleItemMapper.toEntity(item));
        });

        return entity;
    }

    public static Sale toDomain(SaleEntity entity){
        return Sale.from(entity.getId(),entity.getItems().stream().map(SaleItemMapper::toDomain).toList(),entity.getTotalAmount(),entity.getCreatedAt());
    }



    public static PageResult<SaleView> toPageResult(Page<SaleEntity> page) {
        Page<SaleView> viewPage =
                page.map(SaleMapper::toView);

        return new PageResult<>(
                viewPage.getContent(),
                viewPage.getNumber(),
                viewPage.getSize(),
                viewPage.getTotalElements(),
                viewPage.getTotalPages()
        );
    }

    private static SaleView toView(SaleEntity entity) {
        List<SaleItemView> itemViewList=  new ArrayList<>();
        for(SaleItemEntity item: entity.getItems()){
            itemViewList.add(new SaleItemView(item.getId(), item.getProductName(), item.getQuantity(), item.getUnitPrice(),item.getSubtotal()));
        }

        return new SaleView(entity.getId(),entity.getCreatedAt(),entity.getTotalAmount(),itemViewList);

    }
}
