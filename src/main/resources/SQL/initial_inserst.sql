-- -----------------------------------------------------
-- Table order_status initial inserts
-- -----------------------------------------------------
-- Created by Nikolay Boyadzhiev
-- v1/24.12.2022
-- Purpose: Java Advanced final project

INSERT INTO order_status (status_id, status_name) VALUES (1, 'pending'), (2, 'confirmed'), (3, 'shipped'), (4, 'completed'), (5, 'cancelled');
INSERT INTO roles (role_id, role_name) VALUES (1, 'customer'), (2, 'staff'), (3, 'walkin');
INSERT INTO product_group (group_id, group_name, created_at) VALUES (1, 'group 1', Now());