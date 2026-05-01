package com.lps.vitalMagic.product.domain.model.entity;

import com.lps.vitalMagic.product.domain.model.enums.ProductType;

import java.math.BigDecimal;

//TODO Falta unir este modulo al sistema, actualmente al crearse un batido debe crearse automaticamente un producto, validar si es lo mejor para el MVP
public class Product {

    private Long id;


    private Long referenceNo;


    private ProductType productType;


    private String name;


    private BigDecimal price;



    private boolean active;

}
