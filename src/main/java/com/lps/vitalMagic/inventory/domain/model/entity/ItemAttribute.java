package com.lps.vitalMagic.inventory.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.exception.DomainException;
import com.lps.vitalMagic.inventory.domain.exception.InvalidAttributeException;
import com.lps.vitalMagic.inventory.domain.exception.InvalidItemException;
import com.lps.vitalMagic.inventory.domain.model.entity.embeddable.ItemAttributeId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;


@Entity
@Table(name = "item_attribute")
public class ItemAttribute {

    @EmbeddedId
    private ItemAttributeId id;

    @ManyToOne
    @MapsId("itemId")
    private Item item;

    @Getter
    @Column
    private int value;


     ItemAttribute(Long attributeId, Item item,int value) {

        if (value <= 0) {
            throw new InvalidItemException("Attribute Value must be more than zero");
        }

        this.value = value;
        this.id= new ItemAttributeId(null,attributeId);
        this.item= Objects.requireNonNull(item);
    }


    void assignToItem(Item item) {
        if (item == null) {
            throw new InvalidAttributeException("Item is required");
        }
        this.item=item;
    }


}