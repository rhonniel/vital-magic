CREATE TABLE  attribute(
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(250) NOT NULL,
    abbr VARCHAR(3) NOT NULL
);

CREATE TABLE item_attribute (
    item_id BIGINT NOT NULL,
    attribute_id BIGINT NOT NULL,

    value TINYINT NOT NULL,

    PRIMARY KEY (item_id, attribute_id),

    CONSTRAINT fk_item_attribute_item
        FOREIGN KEY (item_id)
        REFERENCES item(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_item_attribute_attribute
        FOREIGN KEY (attribute_id)
        REFERENCES attribute(id)
        ON DELETE RESTRICT
);

ALTER TABLE item
ADD COLUMN active TINYINT(1) NOT NULL DEFAULT 1;

CREATE TABLE item_inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    item_id BIGINT NOT NULL,

    unit_cost DECIMAL(19,4) NOT NULL,

    min_stock INT NOT NULL,
    current_stock INT NOT NULL,

    active TINYINT(1) NOT NULL DEFAULT 1,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,


    CONSTRAINT fk_item_inventory_item
        FOREIGN KEY (item_id)
        REFERENCES item(id)
        ON DELETE RESTRICT
);


CREATE TABLE inventory_transaction (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,

    item_inventory_id BIGINT NOT NULL,

    source_id BIGINT NULL,

    concept VARCHAR(50) NOT NULL,
    operation VARCHAR(50) NOT NULL,

    quantity INT NOT NULL,

    unit_cost DECIMAL(10,4) NOT NULL,

    process_at DATETIME NOT NULL,


    CONSTRAINT fk_inventory_transaction_inventory
        FOREIGN KEY (item_inventory_id)
        REFERENCES item_inventory(id)
        ON DELETE RESTRICT
);


