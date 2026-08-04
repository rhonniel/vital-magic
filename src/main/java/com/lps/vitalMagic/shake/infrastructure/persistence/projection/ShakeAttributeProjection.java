package com.lps.vitalMagic.shake.infrastructure.persistence.projection;

public record ShakeAttributeProjection(
        Long shakeId,
        Long attributeId,
        String attributeName,
        Integer total
) {
}
