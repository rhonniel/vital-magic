ALTER TABLE sale_item
DROP FOREIGN KEY fk_item_inventory_sale;

ALTER TABLE sale_item
RENAME COLUMN item_name TO product_name,
RENAME COLUMN item_inventory_id TO product_id;


ALTER TABLE sale_item
ADD CONSTRAINT fk_product_sale
FOREIGN KEY (product_id)
REFERENCES product(id)
ON DELETE CASCADE;
