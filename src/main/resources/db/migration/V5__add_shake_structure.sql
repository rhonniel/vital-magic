CREATE TABLE  shake(
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(250) NOT NULL,
    abbr VARCHAR(3) NOT NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL
);

CREATE TABLE shake_ingredient (
    item_id BIGINT NOT NULL,
    shake_id BIGINT NOT NULL,

    quantity TINYINT NOT NULL,

    PRIMARY KEY (item_id, shake_id),

    CONSTRAINT fk_item_shake
        FOREIGN KEY (item_id)
        REFERENCES item(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_shake_ingredients
        FOREIGN KEY (shake_id)
        REFERENCES shake(id)
        ON DELETE RESTRICT
);