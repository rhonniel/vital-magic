package com.lps.vitalMagic.sales.application.usecase;

import com.lps.vitalMagic.sales.application.command.CreateSaleCommand;

//Todo   mejorar como obtener calculo de cantidad de ingredientes
public interface RegisterSaleUseCase {
    Long execute(CreateSaleCommand command);
}
