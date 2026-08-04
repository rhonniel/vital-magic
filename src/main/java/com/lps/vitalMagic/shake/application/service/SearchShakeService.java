package com.lps.vitalMagic.shake.application.service;

import com.lps.vitalMagic.common.presentation.pagination.PageResult;
import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.application.usecase.SearchShakeUseCase;
import com.lps.vitalMagic.shake.application.view.ShakeView;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import org.springframework.stereotype.Service;



@Service
public class SearchShakeService implements SearchShakeUseCase {

    private final ShakeRepository shakeRepository;

    public SearchShakeService(ShakeRepository shakeRepository) {
        this.shakeRepository = shakeRepository;
    }

    @Override
    public PageResult<ShakeView> execute(SearchShakeQuery query) {


        return shakeRepository.searchAvailableShakes(query);

    }


}
