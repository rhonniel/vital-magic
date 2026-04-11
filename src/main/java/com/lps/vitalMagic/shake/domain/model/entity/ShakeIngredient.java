package com.lps.vitalMagic.shake.domain.model.entity;

import com.lps.vitalMagic.shake.domain.model.embeddable.ShakeIngredientId;
import com.lps.vitalMagic.shake.domain.model.exception.InvalidShakeException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Entity
@Table(name = "shake_ingredient")
@Getter
public class ShakeIngredient {
    @EmbeddedId
    private ShakeIngredientId shakeIngredientId;

    @ManyToOne
    @MapsId("shakeId")
    //TODO  Design flaw caused by tight coupling with Hibernate
    private Shake shake;


    @Column
    private int quantity;

     ShakeIngredient(Shake shake, Long itemId, int quantity) {

         if(quantity<=0){
             throw  new InvalidShakeException("A Shake Ingredient must contain at least one unit");
         }

        this.shake = Objects.requireNonNull(shake);
        this.shakeIngredientId = new ShakeIngredientId(null, itemId); //TODO  Design flaw caused by tight coupling with Hibernate
        this.quantity = quantity;
    }





}
