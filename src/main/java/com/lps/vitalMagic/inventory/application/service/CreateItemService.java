package com.lps.vitalMagic.inventory.application.service;

import com.lps.vitalMagic.inventory.application.command.CreateItemCommand;
import com.lps.vitalMagic.inventory.application.usecase.CreateItemUseCase;

public class CreateItemService implements CreateItemUseCase {
    @Override
    public Long execute(CreateItemCommand command) {
        // Se incluye la creacion del ItemIventory, para este MVP un item no puede existir sin  inventario
        return 0L;
    }
}
