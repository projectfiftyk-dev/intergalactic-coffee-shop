INSERT INTO products (name, product_status)
VALUES ('Espresso', 'ACTIVE');

INSERT INTO products (name, product_status)
VALUES ('Cappuccino', 'DEPRECATED');

INSERT INTO orders (product_id, product_name, status, created_at)
VALUES (1, 'Espresso', 'CREATED', '2026-08-28 10:00:00');

INSERT INTO orders (product_id, product_name, status, created_at)
VALUES (2, 'Cappuccino', 'PREPARING', '2026-08-28 10:01:00');