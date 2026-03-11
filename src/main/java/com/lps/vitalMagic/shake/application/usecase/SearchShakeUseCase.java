package com.lps.vitalMagic.shake.application.usecase;

import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.application.view.ShakeView;

import java.util.List;

public interface SearchShakeUseCase {
    List<ShakeView> execute(SearchShakeQuery query);
}
