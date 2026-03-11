package com.lps.vitalMagic.purchase.application.command;

import java.math.BigDecimal;
import java.util.List;

public record CreatePurchaseCommand(
    List<CreatePurchaseItemCommand> items
) {}
