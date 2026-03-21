package com.lps.vitalMagic.inventory.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.exception.InvalidItemException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table
@Getter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<ItemAttribute> attributes = new ArrayList<>();

    @Column
    private boolean active;

    private Item(String name, String description, List<ItemAttribute> attributes){
        if(attributes.isEmpty()){
            throw new  InvalidItemException ("Item should have a lest one attribute");
        }
        this.name=name;
        this.description=description;

        for(ItemAttribute attribute:attributes){
            addAttribute(attribute);
        }

    }

    public static Item create(String name, String description, List<ItemAttribute> attributes) {
        Item item= new Item(name,description,attributes);
        item.active=true;

        return item;
    }

    public void addAttribute(ItemAttribute attr) {
        if (attr == null) {
            throw new InvalidItemException("Item cannot have ItemAttribute with null value");
        }
        attr.assignToItem(this);
        this.attributes.add(attr);
    }

}
