package com.lps.vitalMagic.inventory.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.exception.InvalidItemException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Entity
@Table

public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Column
    @Getter
    private String name;
    @Column
    @Getter
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<ItemAttribute> attributes = new ArrayList<>();

    @Column
    @Getter
    private boolean active;

    private Item(String name, String description, List<ItemAttribute> attributes){
        Objects.requireNonNull(attributes);
        if(attributes.isEmpty()){
            throw new  InvalidItemException ("Item should have a lest one attribute");
        }
        this.name=requireNonBlank(name,"name");
        this.description=requireNonBlank(description,"description");

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

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidItemException(field + " cannot be null or blank");
        }
        return value.trim();
    }

    public List<ItemAttribute> getAttributes(){
        return Collections.unmodifiableList(attributes);
    }


}
