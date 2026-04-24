package com.lps.vitalMagic.inventory.domain.repository;

import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;

import java.util.List;
import java.util.Optional;

public interface AttributeRepository {
    List<Attribute> findAll();
    Optional<Attribute> findById(Long id);
}
