package com.lps.vitalMagic.sales.application.service;

import com.lps.vitalMagic.sales.application.command.CreateSaleCommand;
import com.lps.vitalMagic.sales.application.usecase.RegisterSaleUseCase;

public class RegisterSaleService implements RegisterSaleUseCase {

    @Override
    public Long execute(CreateSaleCommand command) {
        return 0L;
    }
}
