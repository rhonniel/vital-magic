package com.lps.vitalMagic.sales.infrastructure.persistence.mapper;

import com.lps.vitalMagic.sales.application.pagination.PageResult;
import com.lps.vitalMagic.sales.application.view.SaleItemView;
import com.lps.vitalMagic.sales.application.view.SaleView;
import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleEntity;
import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleItemEntity;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class SaleMapper {


    public static SaleEntity toEntity(Sale domain){
        return new SaleEntity(domain.getId(), domain.getItems().stream().map(item -> SaleItemMapper.toEntity(domain.getId(),item)).toList(),domain.getTotalAmount(),domain.getCreateAt());
    }

    public static Sale toDomain(SaleEntity entity){
        return Sale.from(entity.getId(),entity.getItems().stream().map(SaleItemMapper::toDomain).toList(),entity.getTotalAmount(),entity.getCreateAt());
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

        return new SaleView(entity.getId(),entity.getCreateAt(),entity.getTotalAmount(),itemViewList);

    }
}
