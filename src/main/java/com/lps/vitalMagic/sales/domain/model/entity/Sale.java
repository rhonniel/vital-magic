package com.lps.vitalMagic.sales.domain.model.entity;

import com.lps.vitalMagic.sales.domain.exception.InvalidSaleException;
import com.lps.vitalMagic.sales.domain.model.input.SaleItemInput;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Sale {

    @Getter
    private Long id;


    private List<SaleItem> items =new ArrayList<>();


    @Getter
    private BigDecimal totalAmount;

    private Sale() {
    }


    public static Sale create(List<SaleItemInput> items){
        Objects.requireNonNull(items);

        if(items.isEmpty()){
            throw new InvalidSaleException("Sale Items shouldn't be empty");
        }
        Sale sale = new Sale();
        for(SaleItemInput saleItem:items){
            sale.items.add(new SaleItem(saleItem.productSnapshot(), saleItem.quantity()));
        }
        sale.calculateTotal();
       return  sale;
    }

    public static Sale from(Long id, List<SaleItem> items,BigDecimal totalAmount){
        Sale sale= new Sale();
        sale.id=id;
        sale.items=items;
        sale.totalAmount=totalAmount;

        return sale;

    }

    public List<SaleItem> getItems(){
        return Collections.unmodifiableList(items);
    }

    private void calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (SaleItem item : items) {
            total = total.add(item.getSubtotal());
        }

        this.totalAmount = total;
    }




}
