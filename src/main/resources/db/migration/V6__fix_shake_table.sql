ALTER TABLE shake_ingredient
DROP FOREIGN KEY fk_shake_ingredients;

ALTER TABLE shake
DROP column abbr,
MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT,
ADD COLUMN shake_type VARCHAR(100) NOT NULL,
ADD COLUMN shake_category VARCHAR(100) NOT NULL;

ALTER TABLE shake_ingredient
ADD CONSTRAINT fk_shake_ingredients
FOREIGN KEY (shake_id)
REFERENCES shake(id)
ON DELETE CASCADE;
