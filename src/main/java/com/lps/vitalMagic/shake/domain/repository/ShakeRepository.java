package com.lps.vitalMagic.shake.domain.repository;

import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import java.util.List;
import java.util.Optional;

public interface ShakeRepository {
    Optional<Shake> findById(Long id);
    Shake save(Shake shake);

    List<Shake> searchAvailableShakes(SearchShakeQuery query);
}
