package com.lps.vitalMagic.product.aplication.exception;

import com.lps.vitalMagic.product.domain.model.enums.ProductType;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String message) {
        super(message);
    }

    public ProductAlreadyExistsException(Long referenceNo, ProductType productType) {
        super("Product referenceNo " + referenceNo + " already exists: " + productType);
    }
}
