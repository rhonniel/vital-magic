package com.lps.vitalMagic.sales.domain.model.entity;

import com.lps.vitalMagic.sales.domain.exception.InvalidSaleException;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;


@Getter
public class SaleItem {

    private Long id;


    private Long saleId;


    private  ProductSnapshot productSnapshot;


    private int quantity;


    private BigDecimal subtotal;

    SaleItem(ProductSnapshot productSnapshot, int quantity) {

        if(quantity<=0){
            throw new InvalidSaleException("Sale item quantity should be more than zero");
        }

        this.productSnapshot= Objects.requireNonNull(productSnapshot);
        this.quantity = quantity;
        calculateSubtotal();
    }

    private SaleItem (){}

    public static SaleItem from(Long id, Long saleId, Long productId, String productName, BigDecimal unitPrice, int quantity, BigDecimal subtotal) {
        SaleItem item = new SaleItem();
        item.id=id;
        item.saleId=saleId;
        item.productSnapshot =  new ProductSnapshot(productId,productName,unitPrice);
        item.quantity=quantity;
        item.subtotal=subtotal;
        return item;

    }

    private void calculateSubtotal(){
        this.subtotal=productSnapshot.getUnitPrice().multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }



}
