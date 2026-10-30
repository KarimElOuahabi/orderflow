CREATE TABLE orders (
    id           UUID           PRIMARY KEY,
    customer_id  VARCHAR(255)   NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    status       VARCHAR(255)   NOT NULL,
    created_at   TIMESTAMPTZ    NOT NULL
);