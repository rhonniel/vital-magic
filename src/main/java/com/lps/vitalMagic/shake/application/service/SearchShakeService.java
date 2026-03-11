package com.lps.vitalMagic.shake.application.service;

import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.application.usecase.SearchShakeUseCase;
import com.lps.vitalMagic.shake.application.view.ShakeView;

import java.util.List;

public class SearchShakeService implements SearchShakeUseCase {
    @Override
    public List<ShakeView> execute(SearchShakeQuery query) {
        return List.of();
    }
}
