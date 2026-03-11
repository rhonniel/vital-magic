package com.lps.vitalMagic.shake.application.service;

import com.lps.vitalMagic.shake.application.command.CreateStandardShakeCommand;
import com.lps.vitalMagic.shake.application.usecase.CreateStandardShakeUseCase;

public class CreateStandardShakeService implements CreateStandardShakeUseCase {

    @Override
    public Long execute(CreateStandardShakeCommand command) {
        return 0L;
    }
}
