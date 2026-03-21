package com.lps.vitalMagic.inventory.domain.model.enums;

public enum InventoryTransactionType {
    SALE(InventoryTransactionOperation.OUTBOUND),
    PURCHASE(InventoryTransactionOperation.INBOUND);

    private final InventoryTransactionOperation operation;

    InventoryTransactionType(InventoryTransactionOperation operation) {
        this.operation = operation;
    }

    public InventoryTransactionOperation getOperation() {
        return operation;
    }

    public boolean isInbound() {
        return operation == InventoryTransactionOperation.INBOUND;
    }

    public boolean isOutbound() {
        return operation == InventoryTransactionOperation.OUTBOUND;
    }
}
