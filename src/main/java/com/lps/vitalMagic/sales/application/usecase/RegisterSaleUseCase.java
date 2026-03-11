package com.lps.vitalMagic.sales.application.usecase;

import com.lps.vitalMagic.sales.application.command.CreateSaleCommand;

public interface RegisterSaleUseCase {
    Long execute(CreateSaleCommand command);
}
