package com.lps.vitalMagic.inventory.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.exception.InvalidItemInventoryException;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "item_inventory")
@Getter
public class ItemInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name="unit_cost")
    private BigDecimal unitCost;

    @Column(name = "min_stock")
    private int minStock;

    @Column(name="current_stock")
    private int currentStock;

    @Column
    private boolean active;

    private ItemInventory(Item item,int minStock){
        if(minStock<0){
            throw new InvalidItemInventoryException("Min stock should be more than zero");
        }
        this.item=item;
        this.minStock=minStock;
    }


    public static ItemInventory create(Item item, int minStock) {
        ItemInventory itemInventory = new ItemInventory(item,minStock);

        itemInventory.unitCost=BigDecimal.ZERO;
        itemInventory.currentStock=0;
        itemInventory.active=true;

        return itemInventory;

    }
}
