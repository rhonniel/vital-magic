package com.lps.vitalMagic.sales.application.pagination;

public record Pagination(int page, int size) {

    public Pagination {
        if (page < 0) {
            throw new IllegalArgumentException("Number of page should be more than 1");
        }

        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("Number of elements per page should between 0 to 99");
        }
    }
}