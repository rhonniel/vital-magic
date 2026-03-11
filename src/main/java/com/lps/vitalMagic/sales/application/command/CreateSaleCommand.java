package com.lps.vitalMagic.sales.application.command;

import java.util.List;

public record CreateSaleCommand(
 List<CreateSaleItemCommand> items
)
{}
