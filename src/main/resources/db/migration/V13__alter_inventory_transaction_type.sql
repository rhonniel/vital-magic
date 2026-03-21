alter table inventory_transaction
RENAME COLUMN concept TO type,
DROP COLUMN operation;