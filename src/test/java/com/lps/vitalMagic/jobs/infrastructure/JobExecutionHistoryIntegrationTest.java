package com.lps.vitalMagic.jobs.infrastructure;

import com.lps.vitalMagic.config.MySqlIntegrationTest;
import com.lps.vitalMagic.jobs.infrastructure.persistence.JobExecutionHistory;
import com.lps.vitalMagic.jobs.infrastructure.persistence.entity.JobExecutionEntity;
import com.lps.vitalMagic.jobs.infrastructure.persistence.repository.JobExecutionJpaRepository;
import com.lps.vitalMagic.jobs.inventory.ProcessPendingInventoryTransactionsJob;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.lps.vitalMagic.jobs.infrastructure.persistence.entity.JobExecutionEntity.Status.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=validate")
@Import(JobExecutionHistory.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class JobExecutionHistoryIntegrationTest extends MySqlIntegrationTest {
    @Autowired private JobExecutionHistory history;
    @Autowired private JobExecutionJpaRepository repository;
    @Autowired private PlatformTransactionManager transactionManager;
    private final List<Long> executionIds = new ArrayList<>();

    @AfterEach
    void cleanCommittedHistory() {
        repository.deleteAllById(executionIds);
    }

    @Test
    void shouldPersistRunningThenCompletedForEachExecutionOnTheSameDay() {
        var job = new ProcessPendingInventoryTransactionsJob(() -> {
            var running = captureRunningExecution();
            assertNotNull(running.getStartedAt());
            assertNotNull(running.getCreatedAt());
            assertNull(running.getCompletedAt());
            assertNull(running.getErrorMessage());
        }, history);

        job.execute();
        job.execute();

        assertEquals(2, executionIds.size());
        assertNotEquals(executionIds.get(0), executionIds.get(1));
        for (Long id : executionIds) {
            var completed = repository.findById(id).orElseThrow();
            assertEquals(COMPLETED, completed.getStatus());
            assertNotNull(completed.getCompletedAt());
            assertNull(completed.getErrorMessage());
        }
    }

    @Test
    void shouldPersistFailedAfterUseCaseRollbackAndRethrowOriginalFailure() {
        var failure = new IllegalStateException("Reconciliation failed");
        var transaction = new TransactionTemplate(transactionManager);
        var job = new ProcessPendingInventoryTransactionsJob(() -> transaction.executeWithoutResult(status -> {
            captureRunningExecution();
            throw failure;
        }), history);

        assertSame(failure, assertThrows(IllegalStateException.class, job::execute));

        var failed = repository.findById(executionIds.get(0)).orElseThrow();
        assertEquals(FAILED, failed.getStatus());
        assertNotNull(failed.getCompletedAt());
        assertEquals("java.lang.IllegalStateException: Reconciliation failed", failed.getErrorMessage());
    }

    @Test
    void shouldCommitHistoryIndependentlyOfAnOuterRollbackAndBoundErrorDetails() {
        var transaction = new TransactionTemplate(transactionManager);
        transaction.executeWithoutResult(status -> {
            Long id = history.start("History isolation test");
            executionIds.add(id);
            history.fail(id, new IllegalStateException("x".repeat(3000)));
            status.setRollbackOnly();
        });

        var failed = repository.findById(executionIds.get(0)).orElseThrow();
        assertEquals(FAILED, failed.getStatus());
        assertEquals(2000, failed.getErrorMessage().length());
        assertTrue(failed.getErrorMessage().startsWith("java.lang.IllegalStateException: "));
    }

    private JobExecutionEntity captureRunningExecution() {
        var running = repository.findAll().stream()
                .filter(execution -> execution.getStatus() == RUNNING
                        && execution.getJobName().equals("ProcessPendingInventoryTransactionsJob"))
                .findFirst().orElseThrow();
        executionIds.add(running.getId());
        return running;
    }
}
