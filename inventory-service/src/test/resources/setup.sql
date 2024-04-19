INSERT INTO product (id, name, price, quantity, description)
VALUES (1, 'Apple iPhone 13 Pro', 999.00, 50, 'Latest iPhone with Pro features'),
       (2, 'Samsung Galaxy S21 Ultra', 899.99, 45, 'Premium Android phone with large screen'),
       (3, 'Microsoft Surface Pro 7', 799.00, 30, 'Powerful laptop with touch screen'),
       (4, 'Dell XPS 15', 1299.99, 20, 'High-performance gaming laptop'),
       (5, 'Sony WH-1000XM4', 348.00, 100, 'Noise-cancelling headphones'),
       (6, 'Logitech MX Master 3', 149.99, 80, 'Wireless mouse with advanced features'),
       (7, 'Razer DeathAdder V2', 79.99, 150, 'Gaming mouse with customizable buttons'),
       (8, 'Corsair Vengeance LPX 16GB', 119.99, 60, 'High-speed RAM for gaming and work'),
       (9, 'ASUS ROG Flow X13', 1299.99, 15, 'Gaming laptop with NVIDIA GeForce RTX 3080'),
       (10, 'Acer Predator Helios 300 PH315-52', 1499.99, 10, 'Gaming desktop with Intel Core i9'),
       (11, 'Ryzen 7 5800X', 499.99, 50, '8-core, 16-thread processor for gaming and work'),
       (12, 'Nvidia GeForce RTX 3080', 699.99, 30, 'High-end graphics card for gaming'),
       (13, 'Cooler Master MasterCase H500', 149.99, 70, 'Premium PC case with RGB lighting'),
       (14, 'Corsair RMx Series RM850x', 199.99, 40, '850W power supply for gaming PCs'),
       (15, 'Samsung 970 EVO Plus SSD', 249.99, 35, 'Fast NVMe SSD for gaming and work');

SELECT setval(pg_get_serial_sequence('product', 'id'), (SELECT MAX(id) FROM product));
