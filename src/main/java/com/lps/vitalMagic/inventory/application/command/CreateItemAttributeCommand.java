package com.lps.vitalMagic.inventory.application.command;

public record CreateItemAttributeCommand(
        Long attributeId,
        Byte value

) {}
