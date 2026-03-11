package com.lps.vitalMagic.inventory.application.command;

import java.util.List;

public record CreateItemCommand (
    String name,
    String description,
    List<CreateItemAttributeCommand> attributes,
    int minStock


){}
