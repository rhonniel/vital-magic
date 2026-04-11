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

    private Item(){
    }

    public static Item create(String name, String description, List<AttributeValue> attributes) {
        Objects.requireNonNull(attributes);
        if(attributes.isEmpty()){
            throw new  InvalidItemException ("Item should have a lest one attribute");
        }

        Item item= new Item();
        item.name=requireNonBlank(name,"name");
        item.description=requireNonBlank(description,"description");
        item.active=true;

        for(AttributeValue attribute:attributes){
            item.addAttribute(attribute);
        }


        return item;
    }

    public void addAttribute(AttributeValue attr) {
        Objects.requireNonNull(attr);
        this.attributes.add(new ItemAttribute(attr.attributeId(),this,attr.value()));
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
