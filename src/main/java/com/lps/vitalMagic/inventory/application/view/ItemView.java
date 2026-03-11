package com.lps.vitalMagic.inventory.application.view;


import java.util.List;

public record ItemView(
        Long id,
        String name,
        String description,
        List<ItemAttributeView> attributes
) {}
