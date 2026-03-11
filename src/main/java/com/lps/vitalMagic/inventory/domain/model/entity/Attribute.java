package com.lps.vitalMagic.inventory.domain.model.entity;

import jakarta.persistence.*;


@Entity
@Table
public class Attribute {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String abbr;



}
