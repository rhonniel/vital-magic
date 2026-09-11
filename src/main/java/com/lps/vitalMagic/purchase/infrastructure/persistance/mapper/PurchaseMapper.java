package com.lps.vitalMagic.purchase.infrastructure.persistance.mapper;

import com.lps.vitalMagic.purchase.application.view.PurchaseItemView;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;
import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;
import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseEntity;
import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseItemEntity;
import com.lps.vitalMagic.common.pagination.PageResult;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;


public class PurchaseMapper {

    public static PurchaseEntity toEntity(Purchase domain){
        PurchaseEntity purchaseEntity= new PurchaseEntity(domain.getId(),
                domain.getTotalAmount(),domain.getCreatedAt());
        domain.getItems().forEach(item -> {
            purchaseEntity.addItem(PurchaseItemMapper.toEntity(item));
        });

        return purchaseEntity;
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
        for(PurchaseItemEntity purchaseItem: entity.getItems()){
            itemViewList.add(new PurchaseItemView(purchaseItem.getItemId(), purchaseItem.getItemName(),
                    purchaseItem.getQuantity(),purchaseItem.getUnitCost(),purchaseItem.getSubtotal()));
        }

        return new PurchaseView(entity.getId(),entity.getCreatedAt(),entity.getTotalAmount(),itemViewList);

    }
}
