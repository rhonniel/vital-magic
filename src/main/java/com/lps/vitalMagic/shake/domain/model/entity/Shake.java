package com.lps.vitalMagic.shake.domain.model.entity;

import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;
import com.lps.vitalMagic.shake.domain.model.exception.InvalidShakeException;
import com.lps.vitalMagic.shake.domain.model.input.IngredientQuantityInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Shake {
    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
    private String description;

    @Getter
    private ShakeType shakeType;

    @Getter
    private ShakeCategory shakeCategory;


    private List<ShakeIngredient> ingredients;

    @Getter
    private boolean active;

    private Shake(){
        this.ingredients = new ArrayList<>();
    }

    public void addIngredient(IngredientQuantityInput ingredientQuantity) {
        ShakeIngredient ingredient =
                new ShakeIngredient(this.id, ingredientQuantity.itemId(), ingredientQuantity.quantity());
        this.ingredients.add(ingredient);
    }

    public static Shake createStandardShake(String name, String description, ShakeCategory shakeCategory, List<IngredientQuantityInput> ingredients) {
        if(Objects.requireNonNull(ingredients).isEmpty()){
            throw  new InvalidShakeException("A shake must contain at least three ingredients");
        }

        if(ingredients.size()<3){
            throw  new InvalidShakeException("A shake must contain at least three ingredients");
        }


        Shake shake = new Shake();

        shake.name =  Objects.requireNonNull(name);
        shake.description = Objects.requireNonNull(description);
        shake.shakeType = Objects.requireNonNull(ShakeType.STANDARD);
        shake.shakeCategory =  Objects.requireNonNull(shakeCategory);
        shake.active=true;

        for(IngredientQuantityInput ingredientQuantity: ingredients){
            shake.addIngredient(ingredientQuantity);
        }

        return shake;
    }

    public static Shake from(Long id,String name, String description, ShakeType shakeType,ShakeCategory shakeCategory, List<ShakeIngredient> ingredients,boolean active){
        Shake  shake=new Shake();
        shake.id=id;
        shake.name=name;
        shake.description=description;
        shake.shakeType=shakeType;
        shake.shakeCategory=shakeCategory;
        shake.ingredients=ingredients;
        shake.active=active;

        return shake;
    }



    public List<ShakeIngredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }
}
