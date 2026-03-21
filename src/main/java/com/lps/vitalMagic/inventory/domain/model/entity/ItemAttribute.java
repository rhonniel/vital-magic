package com.lps.vitalMagic.inventory.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.exception.DomainException;
import com.lps.vitalMagic.inventory.domain.exception.InvalidAttributeException;
import com.lps.vitalMagic.inventory.domain.model.entity.embeddable.ItemAttributeId;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Table(name = "item_attribute")
public class ItemAttribute {

    @EmbeddedId
    private ItemAttributeId id;

    @ManyToOne
    @MapsId("attributeId")
    private Attribute attribute;

    @ManyToOne
    @MapsId("itemId")
    private Item item;

    @Getter
    @Column
    private int value;


    private ItemAttribute(Attribute attribute, int value) {
        if (attribute == null) {
            throw new InvalidAttributeException("Attribute is required");
        }
        if (value <= 0) {
            throw new InvalidAttributeException("Value must be positive");
        }

        this.attribute = attribute;
        this.value = value;
    }

    public static ItemAttribute create(Attribute attribute, int value) {
        return new ItemAttribute(attribute, value);
    }

    void assignToItem(Item item) {
        if (item == null) {
            throw new InvalidAttributeException("Item is required");
        }
        this.item=item;
    }


}