package com.lps.vitalMagic.shake.infrastructure.persistence.projection;

import java.math.BigDecimal;

public record ShakeAttributeProjection(
        Long shakeId,
        Long attributeId,
        String attributeName,
        BigDecimal total
) {
}
