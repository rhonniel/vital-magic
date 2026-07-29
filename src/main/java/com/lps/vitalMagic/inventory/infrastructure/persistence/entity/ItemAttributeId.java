package com.lps.vitalMagic.inventory.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
public class ItemAttributeId implements Serializable {

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "attribute_id")
    private Long attributeId;

    protected ItemAttributeId() {}

    public ItemAttributeId(Long itemId, Long attributeId) {
        this.itemId = itemId;
        this.attributeId = attributeId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemAttributeId that = (ItemAttributeId) o;
        return Objects.equals(itemId, that.itemId) && Objects.equals(attributeId, that.attributeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, attributeId);
    }
}