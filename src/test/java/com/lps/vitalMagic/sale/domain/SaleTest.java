package com.lps.vitalMagic.sale.domain;


import com.lps.vitalMagic.sales.domain.exception.InvalidSaleException;
import com.lps.vitalMagic.sales.domain.model.entity.ProductSnapshot;
import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.sales.domain.model.entity.SaleItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SaleTest {


    @Test
    public void createValidSale(){

        BigDecimal total= new BigDecimal("777.77");
        List<SaleItem> items=getValidSaleItem();
        Sale sale= Sale.create(items,total);


        assertNotNull(sale);
        assertEquals(items.size(),sale.getItems().size());
        assertEquals(total,sale.getTotalAmount());

    }

    @Test
    public void shouldSaleHaveALeastOneSaleItem(){

        BigDecimal total= new BigDecimal("777.77");
        List<SaleItem> items= new ArrayList<>();

        assertThrows(InvalidSaleException.class,()->Sale.create(items,total));

    }


    public List<SaleItem> getValidSaleItem(){
        List<SaleItem> items =new ArrayList<>();

        ProductSnapshot productSnapshot= new ProductSnapshot(7L,"shake test", new BigDecimal("777.77"));
        BigDecimal subTotal = new BigDecimal("777.77");
        SaleItem saleItem= SaleItem.create(productSnapshot,2,subTotal);
        items.add(saleItem);

        return items;

    }


}
