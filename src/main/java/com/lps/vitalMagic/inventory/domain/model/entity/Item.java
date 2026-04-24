package com.lps.vitalMagic.inventory.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.exception.InvalidItemException;
import com.lps.vitalMagic.inventory.domain.model.input.AttributeValue;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Item {

    @Getter
    private Long id;

    @Getter
    private String name;

    @Getter
    private String description;


    private List<ItemAttribute> attributes = new ArrayList<>();


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

    public static Item from(Long id, String name, String description, List<ItemAttribute> attributes, Boolean active){
        Item item = new Item();
        item.id=id;
        item.name=name;
        item.description=description;
        item.attributes = new ArrayList<>(attributes);
        item.active=active;

        return  item;
    }

    public void addAttribute(AttributeValue attr) {
        Objects.requireNonNull(attr);
        this.attributes.add(new ItemAttribute(attr.attributeId(),attr.value()));
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
