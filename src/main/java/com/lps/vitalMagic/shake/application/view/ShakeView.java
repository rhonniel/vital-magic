package com.lps.vitalMagic.shake.application.view;

import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;

import java.util.List;

public record ShakeView(
        Long id,
        String name,
        String description,
        ShakeType shakeType,
        ShakeCategory shakeCategory,
        List<ShakeAttributeView> attributes,
        List<ShakeIngredientView> ingredients
) {
}
