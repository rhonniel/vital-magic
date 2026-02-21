package com.lps.vitalMagic.shake.model.entity;

import com.lps.vitalMagic.core.entity.Auditory;
import com.lps.vitalMagic.shake.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.model.enums.ShakeType;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Shake {
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
    private List<ShakeIngredient> ingredients;

    @Embedded
    private Auditory auditory;

    @Column
    private boolean active;



}
