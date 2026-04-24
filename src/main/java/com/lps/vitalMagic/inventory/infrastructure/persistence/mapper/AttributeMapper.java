package com.lps.vitalMagic.inventory.infrastructure.persistence.mapper;

import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.AttributeEntity;
import org.springframework.stereotype.Component;

@Component
public class AttributeMapper implements BaseMapper<Attribute, AttributeEntity>{

    @Override
    public AttributeEntity toEntity(Attribute domain) {
        return new AttributeEntity(domain.getId(), domain.getName(), domain.getDescription(), domain.getAbbr());
    }

    @Override
    public Attribute toDomain(AttributeEntity entity) {
        return new Attribute(entity.getId(),entity.getName(), entity.getDescription(), entity.getAbbr());
    }
}
