package com.lps.vitalMagic.inventory.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class ItemAttributeId implements Serializable {


    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "attribute_id")
    private Long attributeId;



}