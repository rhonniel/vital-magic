package com.lps.vitalMagic.product.aplication.usecase;

import com.lps.vitalMagic.product.aplication.command.CreateShakeProductCommand;

public interface CreateShakeProductUseCase {
    Long execute(CreateShakeProductCommand command);
}
