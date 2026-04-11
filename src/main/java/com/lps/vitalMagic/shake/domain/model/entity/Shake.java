package com.lps.vitalMagic.shake.domain.model.entity;

import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;
import com.lps.vitalMagic.shake.domain.model.exception.InvalidShakeException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Shake {
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

    @Enumerated(EnumType.STRING)
    @Column(name = "shake_type")
    @Getter
    private ShakeType shakeType;

    @Enumerated(EnumType.STRING)
    @Column(name="shake_category")
    @Getter
    private ShakeCategory shakeCategory;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shake_id")
    private List<ShakeIngredient> ingredients;

    @Column
    @Getter
    private boolean active;
    /*
    TODO 1-Pensar primero las cosas VO y luego como ENTITY si aplicara el caso.
     2-La cracion de objetos dependientes como los ingredientes de una receta deben estar manejado dentro del iten padre no fuera
     */


    private Shake(){
        this.ingredients = new ArrayList<>();
    }

    public void addIngredient(IngredientQuantity ingredientQuantity) {
        ShakeIngredient ingredient =
                new ShakeIngredient(this, ingredientQuantity.itemId(), ingredientQuantity.quantity());

        this.ingredients.add(ingredient);
    }

    public static Shake createStandardShake(String name, String description, ShakeCategory shakeCategory, List<IngredientQuantity> ingredients) {
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

        for(IngredientQuantity ingredientQuantity: ingredients){
            shake.addIngredient(ingredientQuantity);
        }

        return shake;
    }

    public List<ShakeIngredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }
}
