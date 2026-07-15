package com.lps.vitalMagic.purchase.domain.model.entity;

import com.lps.vitalMagic.purchase.domain.exception.InvalidPurchaseException;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;


@Getter
public class PurchaseItem {

    private Long id;

    private  ItemSnapshot item;


    private int quantity;



    private BigDecimal subtotal;

     PurchaseItem(Long purchaseId, ItemSnapshot item, int quantity) {

        if(quantity<=0){
            throw new InvalidPurchaseException("Purchase item quantity should be more than zero");
        }

        this.item= Objects.requireNonNull(item);
        this.quantity = quantity;
        calculateSubtotal();
    }

    private PurchaseItem(){}

    public static PurchaseItem from(Long id, Long productId, String productName, BigDecimal unitCost, int quantity, BigDecimal subtotal) {
      PurchaseItem purchaseItem = new PurchaseItem();
      purchaseItem.id=id;
      purchaseItem.item= new ItemSnapshot(productId,productName,unitCost);
      purchaseItem.quantity=quantity;
      purchaseItem.subtotal=subtotal;

      return purchaseItem;
     }

    private void calculateSubtotal(){
         this.subtotal=item.unitCost().multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

}
