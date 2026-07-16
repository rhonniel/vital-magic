package com.lps.vitalMagic.purchase.infrastructure.persistance.mapper;

import com.lps.vitalMagic.purchase.application.view.PurchaseItemView;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;
import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;
import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseEntity;
import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseItemEntity;
import com.lps.vitalMagic.sales.application.pagination.PageResult;
import com.lps.vitalMagic.sales.application.view.SaleItemView;
import com.lps.vitalMagic.sales.application.view.SaleView;
import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleEntity;
import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleItemEntity;
import com.lps.vitalMagic.sales.infrastructure.persistence.mapper.SaleMapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;


public class PurchaseMapper {

    public static PurchaseEntity toEntity(Purchase domain){
        return new PurchaseEntity(domain.getId(),
                domain.getItems().stream().map(purchaseItem -> PurchaseItemMapper.toEntity(domain.getId(), purchaseItem)).toList(),
                domain.getTotalAmount(),domain.getCreatedAt());
    }

    public static Purchase toDomain(PurchaseEntity entity){
        return Purchase.from(entity.getId(),entity.getItems().stream().map(PurchaseItemMapper::toDomain).toList(),entity.getTotalAmount(),entity.getCreatedAt());
    }

    public static PageResult<PurchaseView> toPageResult(Page<PurchaseEntity> page) {
        Page<PurchaseView> viewPage =
                page.map(PurchaseMapper::toView);

        return new PageResult<>(
                viewPage.getContent(),
                viewPage.getNumber(),
                viewPage.getSize(),
                viewPage.getTotalElements(),
                viewPage.getTotalPages()
        );
    }



    private static PurchaseView toView(PurchaseEntity entity) {
        List<PurchaseItemView> itemViewList=  new ArrayList<>();
        for(PurchaseItemEntity item: entity.getItems()){
            itemViewList.add(new PurchaseItemView(item.getId(), item.getItemName(), item.getQuantity(),item.getUnitCost(),item.getSubtotal()));
        }

        return new PurchaseView(entity.getId(),entity.getCreatedAt(),entity.getTotalAmount(),itemViewList);

    }
}
