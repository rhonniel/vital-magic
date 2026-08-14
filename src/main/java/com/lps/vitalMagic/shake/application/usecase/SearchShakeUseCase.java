package com.lps.vitalMagic.shake.application.usecase;

import com.lps.vitalMagic.common.pagination.PageResult;
import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.application.view.ShakeView;

public interface SearchShakeUseCase {
    PageResult<ShakeView> execute(SearchShakeQuery query);
}
