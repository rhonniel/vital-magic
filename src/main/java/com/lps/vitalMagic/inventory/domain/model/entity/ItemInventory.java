package com.lps.vitalMagic.inventory.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.exception.InvalidItemInventoryException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class ItemInventory {

    private Long id;


    private Long itemId;

    private BigDecimal unitCost;


    private int minStock;


    private int currentStock;


    private boolean active;

    private ItemInventory(Long itemId,int minStock,int currentStock,BigDecimal unitCost){
        if(minStock<0){
            throw new InvalidItemInventoryException("Min stock should be more than zero");
        }

        if(currentStock<0){
            throw new InvalidItemInventoryException("Current stock should be more than zero");
        }

        Objects.requireNonNull(itemId);


        this.itemId=Objects.requireNonNull(itemId);
        this.minStock=minStock;
        this.currentStock=currentStock;
        this.unitCost=Objects.requireNonNull(unitCost);
    }

    private ItemInventory() {
    }

    public static ItemInventory create(Long itemId, int minStock) {
        ItemInventory itemInventory = new ItemInventory(itemId,minStock,0,BigDecimal.ZERO);
        itemInventory.active=true;

        return itemInventory;

    }

    public static ItemInventory from(Long id,Long itemId,int minStock,int currentStock,BigDecimal unitCost){
       ItemInventory itemInventory= new ItemInventory();

       itemInventory.id=id;
       itemInventory.itemId=itemId;
       itemInventory.minStock=minStock;
       itemInventory.currentStock=currentStock;
       itemInventory.unitCost=unitCost;

       return itemInventory;

    }
}
