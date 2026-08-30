INSERT INTO products (name, product_status)
VALUES ('Espresso', 'ACTIVE');

INSERT INTO products (name, product_status)
VALUES ('Cappuccino', 'DEPRECATED');


INSERT INTO orders (status, created_at)
VALUES ('CREATED', '2026-08-28 10:00:00');

INSERT INTO orders (status, created_at)
VALUES ('PREPARING', '2026-08-28 10:01:00');


INSERT INTO order_items (
    order_id,
    product_id,
    product_name
)
VALUES (
           1,
           1,
           'Espresso'
       );

INSERT INTO order_items (
    order_id,
    product_id,
    product_name
)
VALUES (
           2,
           2,
           'Cappuccino'
       );