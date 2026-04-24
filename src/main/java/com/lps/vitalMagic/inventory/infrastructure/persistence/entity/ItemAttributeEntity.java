package com.lps.vitalMagic.inventory.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Table(name = "item_attribute")
@Getter
public class ItemAttributeEntity {

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "attribute_id")
    private Long attributeId;

    @Getter
    @Column
    private int value;

    protected ItemAttributeEntity(){}

    public ItemAttributeEntity( Long itemId, Long attributeId,int value) {
        this.value = value;
        this.attributeId = attributeId;
        this.itemId = itemId;
    }
}