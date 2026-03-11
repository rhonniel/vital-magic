package com.lps.vitalMagic.inventory.application.usecase;

import com.lps.vitalMagic.inventory.application.command.CreateItemCommand;

public interface CreateItemUseCase {
    Long execute(CreateItemCommand command);
}
