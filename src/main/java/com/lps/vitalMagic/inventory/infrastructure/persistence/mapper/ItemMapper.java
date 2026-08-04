package com.lps.vitalMagic.inventory.infrastructure.persistence.mapper;

import com.lps.vitalMagic.inventory.application.view.ItemAttributeView;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemAttribute;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemAttributeEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.common.presentation.pagination.PageResult;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ItemMapper {


    public static ItemEntity toEntity(Item domain) {
        ItemEntity entity = new ItemEntity(
                domain.getName(),
                domain.getDescription(),
                domain.isActive()
        );

        domain.getAttributes().forEach(attribute ->
                entity.addAttribute(
                        new ItemAttributeEntity(
                                attribute.getAttributeId(),
                                attribute.getValue()
                        )
                )
        );

        return entity;
    }

    public static Item toDomain(ItemEntity entity) {
        List<ItemAttribute> attributes = entity.getAttributes()
                .stream()
                .map(a -> ItemAttribute.from(a.getId().getAttributeId(), a.getValue()))
                .toList();
         return Item.from(entity.getId(), entity.getName(), entity.getDescription(), attributes,entity.isActive());
    }


    public static PageResult<ItemView> toPageResult(Page<ItemEntity> page, Map<Long,String> attributesNames) {
        Page<ItemView> viewPage =
                page.map(itemEntity -> toView(itemEntity,attributesNames));

        return new PageResult<>(
                viewPage.getContent(),
                viewPage.getNumber(),
                viewPage.getSize(),
                viewPage.getTotalElements(),
                viewPage.getTotalPages()
        );
    }

    private static ItemView toView(ItemEntity entity,Map<Long,String> attributesNames) {
        List<ItemAttributeView> itemViewList=  new ArrayList<>();
        for(ItemAttributeEntity itemAttribute: entity.getAttributes()){
            itemViewList.add(new ItemAttributeView(itemAttribute.getId().getAttributeId(), attributesNames.get(itemAttribute.getId().getAttributeId()), itemAttribute.getValue()));
        }

        return new ItemView(entity.getId(), entity.getName(), entity.getDescription(),itemViewList);

    }

}
