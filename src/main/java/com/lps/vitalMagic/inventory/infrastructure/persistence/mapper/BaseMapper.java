package com.lps.vitalMagic.inventory.infrastructure.persistence.mapper;

public interface BaseMapper<D,E> {
    E toEntity(D domain) ;
    D toDomain(E entity);
}
