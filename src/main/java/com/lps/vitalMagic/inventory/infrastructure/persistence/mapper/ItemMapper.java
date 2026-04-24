package com.lps.vitalMagic.inventory.infrastructure.persistence.mapper;

import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemAttribute;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemAttributeEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper implements BaseMapper<Item, ItemEntity>{

    @Override
    public ItemEntity toEntity(Item domain) {
        return new ItemEntity(domain.getName(), domain.getDescription(),
                domain.getAttributes().stream().map(attribute -> new ItemAttributeEntity(domain.getId(),attribute.getAttributeId(),attribute.getValue())).toList(), domain.isActive());
    }

    @Override
    public Item toDomain(ItemEntity entity) {
        List<ItemAttribute> attributes = entity.getAttributes()
                .stream()
                .map(a -> ItemAttribute.from(a.getAttributeId(), a.getValue()))
                .toList();
         return Item.from(entity.getId(), entity.getName(), entity.getDescription(), attributes,entity.isActive());
    }

}
