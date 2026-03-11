package com.lps.vitalMagic.purchase.application.usecase;

import com.lps.vitalMagic.purchase.application.command.CreatePurchaseCommand;

public interface RegisterPurchaseUseCase {
    Long execute(CreatePurchaseCommand command);
}
