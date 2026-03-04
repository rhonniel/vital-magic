package com.lps.vitalMagic.inventory.domain.repository;

import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;

import java.util.List;

public interface AttributeRepository {
    List<Attribute> findAll();
    Attribute findById();
}
