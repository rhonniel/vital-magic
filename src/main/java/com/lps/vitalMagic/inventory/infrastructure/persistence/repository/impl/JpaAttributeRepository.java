package com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl;

import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;
import com.lps.vitalMagic.inventory.domain.repository.AttributeRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.mapper.AttributeMapper;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.AttributeJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaAttributeRepository implements AttributeRepository {

    private final AttributeJpaRepository jpaRepository;

    public JpaAttributeRepository( AttributeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Attribute> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(AttributeMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Attribute> findById(Long id) {
        return jpaRepository.findById(id).map(AttributeMapper::toDomain);
    }
}
