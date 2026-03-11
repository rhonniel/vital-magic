package com.lps.vitalMagic.shake.application.query;

import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;

public record SearchShakeQuery(
        ShakeType type,
        ShakeCategory category
) {
}
