package com.lps.vitalMagic.inventory.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Table(name = "item_attribute")
@Getter
public class ItemAttributeEntity {

    @EmbeddedId
    private ItemAttributeId id;

    @MapsId("itemId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;



    @Getter
    @Column
    private int value;

    protected ItemAttributeEntity(){}

    public ItemAttributeEntity(Long attributeId, int value) {
        this.id = new ItemAttributeId(null, attributeId);
        this.value = value;
    }
    void assignTo(ItemEntity item) {
        this.item = item;
    }

}