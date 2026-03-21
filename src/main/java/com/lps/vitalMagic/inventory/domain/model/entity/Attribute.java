package com.lps.vitalMagic.inventory.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.exception.DomainException;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Table
@Getter
public class Attribute {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String abbr;

    public Attribute(Long id, String name, String description, String abbr) {
        if(abbr.length()!=3){
            throw  new DomainException("The Abbreviation  size should be 3");
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.abbr = abbr;
    }
}
