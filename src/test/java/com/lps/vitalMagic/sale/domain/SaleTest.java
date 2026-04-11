package com.lps.vitalMagic.sale.domain;


import com.lps.vitalMagic.sales.domain.exception.InvalidSaleException;
import com.lps.vitalMagic.sales.domain.model.entity.ProductSnapshot;
import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.sales.domain.model.input.SaleItemInput;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SaleTest {


    @Test
    public void createValidSale(){

        List<SaleItemInput> items =new ArrayList<>();

        ProductSnapshot productSnapshot= new ProductSnapshot(7L,"shake test", new BigDecimal("777.77"));
        SaleItemInput saleItem= new SaleItemInput(productSnapshot,2);
        items.add(saleItem);
        Sale sale= Sale.create(items);


        assertNotNull(sale);
        assertEquals(items.size(),sale.getItems().size());

    }

    @Test
    public void shouldSaleHaveALeastOneSaleItem(){

        List<SaleItemInput> items= new ArrayList<>();

        assertThrows(InvalidSaleException.class,()->Sale.create(items));

    }





    @Test
    public void shouldSaleItemQuantityBeMoreThanZero(){
        List<SaleItemInput> items =new ArrayList<>();

        ProductSnapshot productSnapshot= new ProductSnapshot(7L,"shake test", new BigDecimal("777.77"));
        SaleItemInput saleItem= new SaleItemInput(productSnapshot,0);
        items.add(saleItem);

        assertThrows(InvalidSaleException.class,()->Sale.create(items));

    }


    @Test
    public void shouldSaleItemPriceBeMoreThanZero(){
        Long productId= 7L;
        String productName="shake test";
        BigDecimal price = BigDecimal.ZERO;

        assertThrows(InvalidSaleException.class,()-> new ProductSnapshot(productId,productName,price));

    }




}
