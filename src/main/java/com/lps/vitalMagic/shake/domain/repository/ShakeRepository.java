package com.lps.vitalMagic.shake.domain.repository;

import com.lps.vitalMagic.common.presentation.pagination.PageResult;
import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.application.view.ShakeView;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;

import java.util.List;
import java.util.Optional;

public interface ShakeRepository {
    Optional<Shake> findById(Long id);
    Shake save(Shake shake);

    PageResult<ShakeView> searchAvailableShakes(SearchShakeQuery query);
}
