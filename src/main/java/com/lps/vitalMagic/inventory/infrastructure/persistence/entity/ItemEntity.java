package com.lps.vitalMagic.inventory.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table
@Getter
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<ItemAttributeEntity> attributes = new ArrayList<>();

    @Column
    private boolean active;

    public ItemEntity(){}

    public ItemEntity(String name, String description, List<ItemAttributeEntity> attributes, boolean active) {
        this.name = name;
        this.description = description;
        this.attributes = attributes;
        this.active = active;
    }
}
