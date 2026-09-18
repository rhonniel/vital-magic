# Pending inventory reconciliation

`ProcessPendingInventoryTransactionsJob` runs daily at 2:00 AM by default.
Set `jobs.inventory.process-pending.cron` (or `INVENTORY_PROCESS_PENDING_CRON`)
to a Spring six-field cron expression. `-` disables the trigger. Cron uses the
application's default time zone; reconciliation itself has no business-date filter.

As an MVP convenience, `ApplicationReadyEvent` also invokes the same job method
once at startup, including execution history and failure handling. Disabling the
cron with `-` does not disable this startup execution. The listener is synchronous;
an exception is recorded and rethrown to Spring's startup caller. Later scheduled
runs ignore transactions already processed at startup.

The application service captures all transactions with `process_at IS NULL`,
aggregates signed quantities by item, applies one stock increment per item, and
marks exactly the captured IDs with one bulk update. Purchases add and sales
subtract. Transactions inserted after the captured query remain pending for the
next execution. The current schema has no transaction creation timestamp.

The existing repository ports and JPA adapters are reused. Aggregation remains
in Java to reuse the existing pending query and transaction-type operations.
This loads the pending set into memory; large backlogs may eventually need a
separately designed bounded processing approach. No per-transaction save is used.
Only stock is reconciled; unit cost and minimum stock retain their existing values.
As in `ItemCurrentStockService`, each item must have one active inventory.
Missing or ambiguous active inventory causes the entire reconciliation to fail.

One application `@Transactional` boundary covers stock increments and processing
markers. Failed writes or unexpected affected-row counts roll back both. The
processed timestamp provides sequential idempotency, independently of job history.
The bulk queries flush and clear the persistence context to avoid stale entities.

History writes use separate `REQUIRES_NEW` transactions: RUNNING is committed
before processing, and COMPLETED/FAILED is recorded after it returns or throws.
Errors retain the exception class and message, capped at 2,000 characters; full
exceptions go to application logs. If recording failure also fails, that error is
suppressed on the original exception, which is rethrown. If initial history cannot
be recorded, reconciliation does not start. A crash can leave RUNNING history;
history is operational evidence, never an inventory processing guard. A history
completion failure after a successful inventory commit cannot undo that commit;
retrying still ignores the already processed transactions.

Operate with **one active application/scheduler instance** and no overlapping
manual use-case calls. There is no distributed scheduling lock. Concurrent runs
can select the same pending set; conditional markers and affected-row checks
prevent a stale run from committing its stock changes, but overlaps can fail or
deadlock and have no automatic retry or coordination. Multi-instance scheduling
and concurrent stock-reader consistency are outside this change.
