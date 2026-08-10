package com.lps.vitalMagic.shake.application.usecase;

import com.lps.vitalMagic.shake.application.query.ShakeProductSource;

public interface FindShakeProductSourceUseCase{

    ShakeProductSource execute(Long shakeId);

}
