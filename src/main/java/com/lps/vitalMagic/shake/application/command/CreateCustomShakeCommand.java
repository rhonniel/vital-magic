package com.lps.vitalMagic.shake.application.command;

import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;

import java.util.List;

public record CreateCustomShakeCommand(
        List<CreateShakeIngredient> ingredients
) {
}
