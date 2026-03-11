package com.lps.vitalMagic.shake.application.usecase;

import com.lps.vitalMagic.shake.application.command.CreateStandardShakeCommand;

public interface CreateStandardShakeUseCase {
    Long execute(CreateStandardShakeCommand command);
}
