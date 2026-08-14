package com.lps.vitalMagic.product.aplication.command;

import java.util.Objects;

public record CreateShakeProductCommand(
        Long referenceNo) {

    public CreateShakeProductCommand {
        Objects.requireNonNull(referenceNo);

        if (referenceNo <= 0) {
            throw new IllegalArgumentException(
                    "Reference number should be greater than zero"
            );
        }
    }
}