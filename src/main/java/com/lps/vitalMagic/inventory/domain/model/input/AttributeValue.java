package com.lps.vitalMagic.inventory.domain.model.input;

import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;

public record AttributeValue(
        Long attributeId,
        int value
) {
}
