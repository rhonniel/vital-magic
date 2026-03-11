package com.lps.vitalMagic.shake.application.service;

import com.lps.vitalMagic.shake.application.command.CreateCustomShakeCommand;
import com.lps.vitalMagic.shake.application.usecase.CreateCustomShakeUseCase;

public class CreateCustomShakeService implements CreateCustomShakeUseCase {
    @Override
    public Long execute(CreateCustomShakeCommand command) {
        return 0L;
    }
}
