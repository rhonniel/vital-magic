package com.lps.vitalMagic.jobs.inventory;

import com.lps.vitalMagic.inventory.application.usecase.ProcessPendingInventoryTransactionsUseCase;
import com.lps.vitalMagic.jobs.infrastructure.persistence.JobExecutionHistory;
import com.lps.vitalMagic.jobs.SchedulingConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.CronTask;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessPendingInventoryTransactionsJobTest {
    @Mock private ProcessPendingInventoryTransactionsUseCase useCase;
    @Mock private JobExecutionHistory history;
    @InjectMocks private ProcessPendingInventoryTransactionsJob job;

    @Test
    void shouldRecordStartDelegateAndRecordCompletion() {
        when(history.start("ProcessPendingInventoryTransactionsJob")).thenReturn(7L);

        job.execute();

        var order = inOrder(history, useCase);
        order.verify(history).start("ProcessPendingInventoryTransactionsJob");
        order.verify(useCase).execute();
        order.verify(history).complete(7L);
        verify(history, never()).fail(anyLong(), any());
    }

    @Test
    void shouldRecordFailureAndRethrowTheOriginalException() {
        when(history.start("ProcessPendingInventoryTransactionsJob")).thenReturn(7L);
        var failure = new IllegalStateException("Inventory write failed");
        doThrow(failure).when(useCase).execute();

        assertSame(failure, assertThrows(IllegalStateException.class, job::execute));

        var order = inOrder(history, useCase);
        order.verify(history).start("ProcessPendingInventoryTransactionsJob");
        order.verify(useCase).execute();
        order.verify(history).fail(7L, failure);
        verify(history, never()).complete(anyLong());
    }

    @Test
    void shouldPreserveOriginalFailureWhenRecordingFailureAlsoFails() {
        when(history.start("ProcessPendingInventoryTransactionsJob")).thenReturn(7L);
        var failure = new IllegalStateException("Inventory write failed");
        var historyFailure = new IllegalArgumentException("History unavailable");
        doThrow(failure).when(useCase).execute();
        doThrow(historyFailure).when(history).fail(7L, failure);

        assertSame(failure, assertThrows(IllegalStateException.class, job::execute));
        assertArrayEquals(new Throwable[]{historyFailure}, failure.getSuppressed());
    }

    @Test
    void shouldNotProcessInventoryIfExecutionCannotBeRegistered() {
        var failure = new IllegalStateException("History unavailable");
        when(history.start("ProcessPendingInventoryTransactionsJob")).thenThrow(failure);

        assertSame(failure, assertThrows(IllegalStateException.class, job::execute));
        verifyNoInteractions(useCase);
    }

    @Test
    void shouldRegisterTheScheduleUsingTheConfiguredCron() {
        String cron = "0 0 0 1 1 *";
        when(history.start("ProcessPendingInventoryTransactionsJob")).thenReturn(7L);
        try (var context = createContext(cron)) {
            var tasks = context.getBean(ScheduledAnnotationBeanPostProcessor.class).getScheduledTasks();
            assertEquals(1, tasks.size());
            var task = assertInstanceOf(CronTask.class, tasks.iterator().next().getTask());
            assertEquals(cron, task.getExpression());

            task.getRunnable().run();

            var order = inOrder(history, useCase);
            order.verify(history).start("ProcessPendingInventoryTransactionsJob");
            order.verify(useCase).execute();
            order.verify(history).complete(7L);
        }
    }

    @Test
    void shouldExecuteTheJobFlowOnceWhenApplicationIsReadyEvenWithCronDisabled() {
        when(history.start("ProcessPendingInventoryTransactionsJob")).thenReturn(7L);
        try (var context = createContext("-")) {
            verifyNoInteractions(history, useCase);

            context.publishEvent(new ApplicationReadyEvent(new SpringApplication(), new String[0],
                    context, Duration.ZERO));

            var order = inOrder(history, useCase);
            order.verify(history).start("ProcessPendingInventoryTransactionsJob");
            order.verify(useCase).execute();
            order.verify(history).complete(7L);
            verifyNoMoreInteractions(history, useCase);
        }
    }

    @Test
    void shouldRecordAndPropagateTheOriginalFailureFromTheReadyEvent() {
        when(history.start("ProcessPendingInventoryTransactionsJob")).thenReturn(7L);
        var failure = new IllegalStateException("Startup reconciliation failed");
        doThrow(failure).when(useCase).execute();
        try (var context = createContext("-")) {
            assertSame(failure, assertThrows(IllegalStateException.class, () ->
                    context.publishEvent(new ApplicationReadyEvent(new SpringApplication(), new String[0],
                            context, Duration.ZERO))));

            var order = inOrder(history, useCase);
            order.verify(history).start("ProcessPendingInventoryTransactionsJob");
            order.verify(useCase).execute();
            order.verify(history).fail(7L, failure);
            verify(history, never()).complete(anyLong());
        }
    }

    private AnnotationConfigApplicationContext createContext(String cron) {
        var context = new AnnotationConfigApplicationContext();
        context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("job-test",
                Map.of("jobs.inventory.process-pending.cron", cron)));
        context.registerBean(ProcessPendingInventoryTransactionsUseCase.class, () -> useCase);
        context.registerBean(JobExecutionHistory.class, () -> history);
        context.register(SchedulingConfiguration.class, ProcessPendingInventoryTransactionsJob.class);
        context.refresh();
        return context;
    }
}
