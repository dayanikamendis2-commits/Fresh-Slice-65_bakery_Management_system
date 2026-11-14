-- Fresh Slice 65 Bakery Management System
-- Database Script for MySQL

-- Create Database
CREATE DATABASE IF NOT EXISTS bakery_db;
USE bakery_db;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;

-- Create Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL,
    fullname VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Category Table
CREATE TABLE category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Product Table
CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category_id INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

-- Create Stock Table
CREATE TABLE stock (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL UNIQUE,
    quantity INT NOT NULL DEFAULT 0,
    restock_level INT NOT NULL DEFAULT 10,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Insert Default Users
-- Manager: admin / admin123
-- Counter Assistant: assistant / assist123
INSERT INTO users (username, password, role, fullname) VALUES
('admin', 'admin123', 'Manager', 'System Administrator'),
('assistant', 'assist123', 'Assistant', 'Counter Assistant');

-- Insert Default Categories (Bread, Cakes, Pastries, Ingredients)
INSERT INTO category (name) VALUES
('Bread'),
('Cakes'),
('Pastries'),
('Ingredients');

-- Insert Sample Products
INSERT INTO product (name, category_id, price) VALUES
-- Bread
('White Bread', 1, 2.50),
('Whole Wheat Bread', 1, 3.00),
('Baguette', 1, 2.75),
('Sourdough Bread', 1, 4.00),
('Rye Bread', 1, 3.50),
-- Cakes
('Chocolate Cake', 2, 25.00),
('Vanilla Cake', 2, 22.00),
('Red Velvet Cake', 2, 28.00),
('Carrot Cake', 2, 24.00),
('Cheesecake', 2, 26.00),
-- Pastries
('Croissant', 3, 3.50),
('Danish Pastry', 3, 3.75),
('Eclair', 3, 4.00),
('Cinnamon Roll', 3, 3.25),
('Apple Turnover', 3, 3.50),
-- Ingredients
('All-Purpose Flour (5kg)', 4, 8.00),
('Sugar (2kg)', 4, 4.50),
('Butter (500g)', 4, 6.00),
('Eggs (Dozen)', 4, 5.00),
('Yeast (100g)', 4, 2.50);

-- Insert Sample Stock
INSERT INTO stock (product_id, quantity, restock_level) VALUES
(1, 50, 10),
(2, 30, 10),
(3, 25, 8),
(4, 20, 8),
(5, 15, 8),
(6, 10, 3),
(7, 12, 3),
(8, 8, 3),
(9, 6, 3),
(10, 5, 3),
(11, 40, 15),
(12, 35, 12),
(13, 25, 10),
(14, 30, 12),
(15, 20, 10),
(16, 5, 2),
(17, 8, 3),
(18, 10, 3),
(19, 24, 6),
(20, 15, 5);

-- Verify Data
SELECT 'Users:' AS '';
SELECT * FROM users;

SELECT 'Categories:' AS '';
SELECT * FROM category;

SELECT 'Products:' AS '';
SELECT p.id, p.name, c.name AS category, p.price 
FROM product p 
JOIN category c ON p.category_id = c.id;

SELECT 'Stock with Low Inventory Alert:' AS '';
SELECT p.name AS product, s.quantity, s.restock_level,
       CASE WHEN s.quantity <= s.restock_level THEN 'LOW STOCK - RESTOCK NEEDED' ELSE 'OK' END AS status
FROM stock s
JOIN product p ON s.product_id = p.id;
