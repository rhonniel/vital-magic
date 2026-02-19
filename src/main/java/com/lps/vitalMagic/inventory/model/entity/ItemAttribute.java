package com.lps.vitalMagic.inventory.model.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "item_attribute")
public class ItemAttribute {

    @EmbeddedId
    private ItemAttributeId id;

    @Column
    private Byte value;
}