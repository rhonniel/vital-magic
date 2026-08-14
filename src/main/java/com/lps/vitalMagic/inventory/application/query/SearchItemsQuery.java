package com.lps.vitalMagic.inventory.application.query;

import com.lps.vitalMagic.common.pagination.Pagination;

public record SearchItemsQuery(
        String name,
        Pagination pagination
)
{}
