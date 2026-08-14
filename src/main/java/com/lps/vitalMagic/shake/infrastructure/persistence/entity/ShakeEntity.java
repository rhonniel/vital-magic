package com.lps.vitalMagic.shake.infrastructure.persistence.entity;

import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemAttributeEntity;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shake")
@Getter
public class ShakeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "shake_type")
    private ShakeType shakeType;

    @Enumerated(EnumType.STRING)
    @Column(name="shake_category")
    private ShakeCategory shakeCategory;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shake_id")
    private List<ShakeIngredientEntity> ingredients;

    @Column
    private boolean active;

    protected ShakeEntity() {
    }
    public void addShakeIngredientEntity(ShakeIngredientEntity shakeIngredientEntity) {
        ingredients.add(shakeIngredientEntity);
        shakeIngredientEntity.assignTo(this);
    }

    public ShakeEntity(Long id, String name, String description, ShakeType shakeType, ShakeCategory shakeCategory, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shakeType = shakeType;
        this.shakeCategory = shakeCategory;
        this.ingredients = new ArrayList<>();
        this.active = active;
    }
}
