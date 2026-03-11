package com.lps.vitalMagic.purchase.application.service;

import com.lps.vitalMagic.purchase.application.command.CreatePurchaseCommand;
import com.lps.vitalMagic.purchase.application.usecase.RegisterPurchaseUseCase;

public class RegisterPurchaseService implements RegisterPurchaseUseCase {
    @Override
    public Long execute(CreatePurchaseCommand command) {
        return 0L;
    }
}
