package com.lps.vitalMagic.shake.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.shake.domain.model.embeddable.ShakeIngredientId;
import com.lps.vitalMagic.shake.domain.model.exception.InvalidShakeException;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "shake_ingredient")
public class ShakeIngredient {
    @EmbeddedId
    private ShakeIngredientId shakeIngredientId;

    @ManyToOne
    @MapsId("shakeId")
    private Shake shake;


    @Column
    private int quantity;

     ShakeIngredient(Shake shake, Long itemId, int quantity) {

         if(quantity<=0){
             throw  new InvalidShakeException("A Shake Ingredient must contain at least one unit");
         }

        this.shake = Objects.requireNonNull(shake);
        this.shakeIngredientId = new ShakeIngredientId(null, itemId);
        this.quantity = quantity;
    }





}
