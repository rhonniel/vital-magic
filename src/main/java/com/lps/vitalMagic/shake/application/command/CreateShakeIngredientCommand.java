package com.lps.vitalMagic.shake.application.command;

public record CreateShakeIngredientCommand(
        Long ingredientId,
        int quantity
) {
}
