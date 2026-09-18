package com.lps.vitalMagic.jobs.inventory;

import com.lps.vitalMagic.inventory.application.usecase.ProcessPendingInventoryTransactionsUseCase;
import com.lps.vitalMagic.jobs.infrastructure.persistence.JobExecutionHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProcessPendingInventoryTransactionsJob {
    private static final String JOB_NAME = "ProcessPendingInventoryTransactionsJob";

    private final ProcessPendingInventoryTransactionsUseCase useCase;
    private final JobExecutionHistory history;

    public ProcessPendingInventoryTransactionsJob(ProcessPendingInventoryTransactionsUseCase useCase,
                                                 JobExecutionHistory history) {
        this.useCase = useCase;
        this.history = history;
    }

    @Scheduled(cron = "${jobs.inventory.process-pending.cron}")
    @EventListener(ApplicationReadyEvent.class)
    public void execute() {
        Long executionId = history.start(JOB_NAME);
        try {
            useCase.execute();
            history.complete(executionId);
        } catch (RuntimeException | Error failure) {
            try {
                history.fail(executionId, failure);
            } catch (RuntimeException | Error historyFailure) {
                if (historyFailure != failure) {
                    failure.addSuppressed(historyFailure);
                }
            }
            log.error("Inventory job execution {} failed", executionId, failure);
            throw failure;
        }
    }
}
