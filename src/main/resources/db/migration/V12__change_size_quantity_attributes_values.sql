alter table item_attribute
MODIFY COLUMN value INT;

alter table item
DROP COLUMN created_at,
DROP COLUMN created_by,
DROP COLUMN updated_at,
DROP COLUMN updated_by;


alter table item_inventory
DROP COLUMN created_at,
DROP COLUMN created_by,
DROP COLUMN updated_at,
DROP COLUMN updated_by;


alter table sale
DROP COLUMN created_at,
DROP COLUMN created_by,
DROP COLUMN updated_at,
DROP COLUMN updated_by;


alter table purchase
DROP COLUMN created_at,
DROP COLUMN created_by,
DROP COLUMN updated_at,
DROP COLUMN updated_by;


alter table product
DROP COLUMN created_at,
DROP COLUMN created_by,
DROP COLUMN updated_at,
DROP COLUMN updated_by;