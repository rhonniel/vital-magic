package com.lps.vitalMagic.shake.application.command;

import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;

import java.util.List;

public record CreateStandardShakeCommand(
        String name,
        String description,
        ShakeCategory shakeCategory,
        List<CreateShakeIngredient> ingredients
) {
}
