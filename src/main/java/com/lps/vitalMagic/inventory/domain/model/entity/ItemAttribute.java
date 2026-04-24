package com.lps.vitalMagic.inventory.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.exception.InvalidItemException;
import lombok.Getter;


@Getter
public class ItemAttribute {

    private Long attributeId;
    private int value;


     ItemAttribute(Long attributeId,int value) {

        if (value <= 0) {
            throw new InvalidItemException("Attribute Value must be more than zero");
        }

        this.value = value;
        this.attributeId= attributeId;
    }


    public static ItemAttribute from(Long attributeId,int value){
       return new ItemAttribute(attributeId, value);
    }



}