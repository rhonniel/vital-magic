package com.lps.vitalMagic.inventory.infrastructure.persistence.mapper;


import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.AttributeEntity;



public class AttributeMapper  {

    public static AttributeEntity toEntity(Attribute domain) {
        return new AttributeEntity(domain.getId(), domain.getName(), domain.getDescription(), domain.getAbbr());
    }


    public static Attribute toDomain(AttributeEntity entity) {
        return new Attribute(entity.getId(),entity.getName(), entity.getDescription(), entity.getAbbr());
    }
}
