package com.lps.vitalMagic.shake.application.command;

import java.util.List;

public record CreateCustomShakeCommand(
        List<CreateShakeIngredientCommand> ingredients
) {
}
