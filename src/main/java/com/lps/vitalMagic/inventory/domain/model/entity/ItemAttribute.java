package com.lps.vitalMagic.inventory.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.model.entity.embeddable.ItemAttributeId;
import jakarta.persistence.*;


@Entity
@Table(name = "item_attribute")
public class ItemAttribute {

    @EmbeddedId
    private ItemAttributeId id;

    @Column
    private Byte value;
}