package com.lps.vitalMagic.shake.application.usecase;

import com.lps.vitalMagic.shake.application.command.CreateCustomShakeCommand;


public interface CreateCustomShakeUseCase {
    Long execute(CreateCustomShakeCommand command);
}
