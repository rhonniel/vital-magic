alter table inventory_transaction
RENAME COLUMN item_inventory_id TO item_id
DROP FOREIGN KEY fk_inventory_transaction_inventory
ADD CONSTRAINT fk_inventory_transaction_item
    FOREIGN KEY (item_id)
    REFERENCES item(id)
    ON DELETE RESTRICT;