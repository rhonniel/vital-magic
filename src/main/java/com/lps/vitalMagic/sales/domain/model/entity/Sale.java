package com.lps.vitalMagic.sales.domain.model.entity;

import com.lps.vitalMagic.sales.domain.exception.InvalidSaleException;
import com.lps.vitalMagic.sales.domain.model.input.SaleItemInput;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "sale_id")
    private List<SaleItem> items =new ArrayList<>();

    @Column(name = "total_amount")
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
