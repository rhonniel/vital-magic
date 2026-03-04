package com.lps.vitalMagic.purchase.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PurchaseFilter {
    private LocalDate from;
    private LocalDate to;
    private Long itemId;
}
