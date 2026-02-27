
CREATE TABLE purchase (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    total_amount DECIMAL(19 , 4 ) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL
);



CREATE TABLE purchase_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    purchase_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    item_name VARCHAR(50) NOT NULL,
    unit_cost DECIMAL(19 , 4 ) NOT NULL,
    quantity INT NOT NULL,
    subtotal DECIMAL(19 , 4 ) NOT NULL,
    CONSTRAINT fk_item_purchase FOREIGN KEY (item_id)
        REFERENCES item (id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_purchase_items FOREIGN KEY (purchase_id)
        REFERENCES purchase (id)
        ON DELETE RESTRICT
);

