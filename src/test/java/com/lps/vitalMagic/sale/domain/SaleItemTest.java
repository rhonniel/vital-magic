package com.lps.vitalMagic.sale.domain;

import com.lps.vitalMagic.sales.domain.exception.InvalidSaleItemException;
import com.lps.vitalMagic.sales.domain.model.entity.ProductSnapshot;
import com.lps.vitalMagic.sales.domain.model.entity.SaleItem;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


public class SaleItemTest {

    @Test
    public void createValidSaleItem(){
        Long productId= 7L;
        String productName="shake test";
        BigDecimal price = new BigDecimal("777.77");
        ProductSnapshot productSnapshot= new ProductSnapshot(productId,productName,price);
        BigDecimal subTotal= new BigDecimal("777.77");
        int quantity=2;

        SaleItem saleItem= SaleItem.create(productSnapshot,quantity,subTotal);

        assertNotNull(saleItem);
        assertEquals(quantity,saleItem.getQuantity());
    }


    @Test
    public void shouldSaleItemQuantityBeMoreThanZero(){
        Long productId= 7L;
        String productName="shake test";
        BigDecimal price = new BigDecimal("777.77");
        ProductSnapshot productSnapshot= new ProductSnapshot(productId,productName,price);
        BigDecimal subTotal= new BigDecimal("777.77");
        int quantity=0;

        assertThrows(InvalidSaleItemException.class,()->SaleItem.create(productSnapshot,quantity,subTotal));

    }


    @Test
    public void shouldSaleItemPriceBeMoreThanZero(){
        Long productId= 7L;
        String productName="shake test";
        BigDecimal price = BigDecimal.ZERO;

        assertThrows(InvalidSaleItemException.class,()-> new ProductSnapshot(productId,productName,price));

    }



}
