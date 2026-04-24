package com.lps.vitalMagic.inventory.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;


@Entity
@Table
@Getter
public class AttributeEntity {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String abbr;

    protected AttributeEntity(){}

     public AttributeEntity(Long id, String name, String description, String abbr) {
        this.name = name;
        this.description = description;
        this.abbr = abbr;
        this.id = id;
    }
}
