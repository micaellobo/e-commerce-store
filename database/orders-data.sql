ALTER SEQUENCE order_products_id_seq RESTART;
ALTER SEQUENCE orders_id_seq RESTART;

INSERT INTO orders (created_at, updated_at, user_id)
VALUES ('2023-07-10 10:00:00', '2023-07-10 10:30:00', 1),
       ('2023-07-15 11:00:00', '2023-07-15 11:15:00', 1),
       ('2023-07-20 12:00:00', '2023-07-20 12:30:00', 1),
       ('2023-07-25 13:00:00', '2023-07-25 13:30:00', 1),
       ('2023-07-28 15:00:00', '2023-07-28 15:30:00', 1),
       ('2023-07-30 16:00:00', '2023-07-30 16:30:00', 1),
       ('2023-08-01 17:00:00', '2023-08-01 17:30:00', 1),
       ('2023-08-05 10:00:00', '2023-08-05 10:30:00', 1),
       ('2023-08-10 11:00:00', '2023-08-10 11:15:00', 1),
       ('2023-08-15 13:00:00', '2023-08-15 13:30:00', 1),
       ('2023-08-20 14:00:00', '2023-08-20 14:30:00', 1),
       ('2023-08-25 16:00:00', '2023-08-25 16:30:00', 1),
       ('2023-07-05 10:00:00', '2023-07-05 10:30:00', 2),
       ('2023-07-12 11:00:00', '2023-07-12 11:15:00', 2),
       ('2023-07-22 13:00:00', '2023-07-22 13:30:00', 2),
       ('2023-07-25 14:00:00', '2023-07-25 14:30:00', 2),
       ('2023-08-01 18:00:00', '2023-08-01 18:30:00', 2),
       ('2023-07-10 12:00:00', '2023-07-10 12:30:00', 2);

INSERT INTO order_products (price, quantity, order_id, product_id)
VALUES (49.99, 1, 1, 4),    -- Mechanical Gaming Keyboard
       (19.99, 2, 1, 5),    -- Wireless Mouse
       (199.99, 1, 1, 6),   -- Noise-Canceling Headphones
       (39.95, 1, 2, 9),    -- Smartphone Tripod Stand
       (89.50, 1, 2, 10),   -- Foldable Laptop Cooling Pad
       (89.00, 3, 3, 15),   -- Adjustable Laptop Stand
       (39.95, 2, 3, 19),   -- Portable Bluetooth Speaker
       (29.50, 1, 3, 21),   -- Fitness Tracker Watch
       (14.95, 1, 3, 23),   -- Gaming Mousepad
       (199.99, 4, 4, 6),   -- Noise-Canceling Headphones
       (129.00, 1, 4, 8),   -- Portable Power Bank
       (39.95, 1, 5, 19),   -- Portable Bluetooth Speaker
       (89.00, 1, 5, 21),   -- Fitness Tracker Watch
       (24.50, 3, 5, 28),   -- Compact Wireless Mouse
       (49.95, 1, 5, 2),    -- 27-inch LED Monitor
       (39.95, 4, 5, 25),   -- USB-C Fast Charger
       (49.99, 1, 6, 4),    -- Mechanical Gaming Keyboard
       (19.99, 2, 6, 5),    -- Wireless Mouse
       (199.99, 1, 6, 6),   -- Noise-Canceling Headphones
       (89.00, 1, 7, 15),   -- Adjustable Laptop Stand
       (39.95, 2, 7, 19),   -- Portable Bluetooth Speaker
       (29.50, 1, 7, 21),   -- Fitness Tracker Watch
       (14.95, 1, 7, 23),   -- Gaming Mousepad
       (199.99, 1, 8, 6),   -- Noise-Canceling Headphones
       (129.00, 1, 8, 8),   -- Portable Power Bank
       (39.95, 1, 9, 19),   -- Portable Bluetooth Speaker
       (89.00, 1, 9, 21),   -- Fitness Tracker Watch
       (24.50, 1, 9, 28),   -- Compact Wireless Mouse
       (49.95, 1, 9, 2),    -- 27-inch LED Monitor
       (39.95, 1, 9, 25),   -- USB-C Fast Charger
       (49.99, 1, 10, 4),   -- Mechanical Gaming Keyboard
       (19.99, 2, 10, 5),   -- Wireless Mouse
       (199.99, 1, 10, 6),  -- Noise-Canceling Headphones
       (39.95, 1, 11, 9),   -- Smartphone Tripod Stand
       (89.50, 1, 11, 10),  -- Foldable Laptop Cooling Pad
       (89.00, 1, 12, 15),  -- Adjustable Laptop Stand
       (39.95, 2, 12, 19),  -- Portable Bluetooth Speaker
       (29.50, 1, 12, 21),  -- Fitness Tracker Watch
       (14.95, 2, 12, 23),  -- Gaming Mousepad
       (49.99, 1, 13, 4),   -- Mechanical Gaming Keyboard
       (19.99, 2, 13, 5),   -- Wireless Mouse
       (199.99, 1, 13, 6),  -- Noise-Canceling Headphones
       (89.00, 1, 13, 15),  -- Adjustable Laptop Stand
       (39.95, 1, 13, 19),  -- Portable Bluetooth Speaker
       (24.50, 2, 13, 28),  -- Compact Wireless Mouse
       (89.50, 1, 14, 10),  -- Foldable Laptop Cooling Pad
       (24.75, 2, 14, 7),   -- External SSD Drive
       (34.50, 1, 14, 8),   -- Portable Power Bank
       (49.95, 1, 15, 3),   -- Bluetooth Speaker
       (59.99, 2, 15, 11),  -- Portable Bluetooth Keyboard
       (29.50, 1, 15, 12),  -- Wireless Charging Pad
       (89.00, 1, 15, 21),  -- Fitness Tracker Watch
       (14.95, 2, 15, 23),  -- Gaming Mousepad
       (79.99, 1, 16, 27),  -- Gaming Headset with Microphone
       (49.95, 4, 16, 29),  -- Portable Laptop Power Adapter
       (24.50, 1, 16, 30),  -- Compact Wireless Mouse
       (29.99, 2, 16, 31),  -- Wireless Presenter Remote
       (39.95, 1, 17, 32),  -- Bluetooth In-Ear Headphones
       (49.00, 1, 17, 33),  -- External DVD Drive
       (19.99, 2, 17, 34),  -- Wireless Mouse
       (199.99, 1, 17, 6),  -- Noise-Canceling Headphones
       (119.00, 1, 17, 20), -- Portable External Hard Drive
       (89.50, 3, 17, 10),  -- Foldable Laptop Cooling Pad
       (54.50, 1, 17, 22),  -- Wireless In-Ear Sport Earphones
       (49.00, 3, 18, 33),  -- External DVD Drive
       (19.99, 2, 18, 34),  -- Wireless Mouse
       (199.99, 1, 18, 6),  -- Noise-Canceling Headphones
       (119.00, 1, 18, 20), -- Portable External Hard Drive
       (89.50, 1, 18, 10); -- Foldable Laptop Cooling Pad

