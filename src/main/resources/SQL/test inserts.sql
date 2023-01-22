-- Test data insert
-- created by Nikolay Boyadzhiev
-- Date 30.12.2022

-- create test product_group
INSERT INTO product_group (group_name, created_at) VALUES ('group 1', NOW());

-- create test products
INSERT INTO products (product_name, product_desc, product_price, vat, created_at, available_units, group_id, detailed_desc) VALUES
('product 1', 'Description product 1', 1.25, 0.20, NOW(), 100, 1, 'detailed description of product 1. The best product ever !!!'),
('product 2', 'Description product 2', 2.25, 0.20, NOW(), 200, 1, 'detailed description of product 2. Dont''t buy it. Peace of shit'),
('product 3', 'Description product 3', 3.25, 0.20, NOW(), 300, 1, null),
('product 4', 'Description product 4', 4.25, 0.20, NOW(), 400, 1, null),
('product 5', 'Description product 5', 5.25, 0.20, NOW(), 500, 1, null),
('product 6', 'Description product 6', 6.25, 0.20, NOW(), 600, 1, null);


-- create test image paths
INSERT INTO images (image_path, product_id) VALUES ('path1', 1), ('path2', 2), ('path3', 3), ('path4', 4), ('path1', 5), ('path1', 5), ('path6', 6);
INSERT INTO images (image_path, product_id) VALUES ('path8', 1), ('path9', 2), ('path10', 3);
