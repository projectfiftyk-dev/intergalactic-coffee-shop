-- =========================================================
-- USERS / ROLES
-- =========================================================

INSERT INTO users (username, password, name)
VALUES ('admin', 'admin-password-hash', 'Admin User');

INSERT INTO users (username, password, name)
VALUES ('employee', 'employee-password-hash', 'Employee User');

INSERT INTO users (username, password, name)
VALUES ('john', 'john-password-hash', 'John Doe');


INSERT INTO user_roles (user_id, role_id)
SELECT
    u.id,
    r.id
FROM users u
         CROSS JOIN roles r
WHERE u.username = 'admin'
  AND r.name = 'ADMIN';


INSERT INTO user_roles (user_id, role_id)
SELECT
    u.id,
    r.id
FROM users u
         CROSS JOIN roles r
WHERE u.username = 'employee'
  AND r.name = 'EMPLOYEE';


INSERT INTO user_roles (user_id, role_id)
SELECT
    u.id,
    r.id
FROM users u
         CROSS JOIN roles r
WHERE u.username = 'john'
  AND r.name = 'USER';


-- =========================================================
-- PRODUCTS
-- =========================================================

INSERT INTO products (name, product_status)
VALUES ('Espresso', 'ACTIVE');

INSERT INTO products (name, product_status)
VALUES ('Cappuccino', 'DEPRECATED');


-- =========================================================
-- ORDERS
-- =========================================================

INSERT INTO orders (status, created_at)
VALUES ('CREATED', '2026-08-28 10:00:00');

INSERT INTO orders (status, created_at)
VALUES ('PREPARING', '2026-08-28 10:01:00');


-- =========================================================
-- ORDER ITEMS
-- =========================================================

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