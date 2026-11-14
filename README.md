# Fresh Slice 65 – Bakery Management System

## Description
Fresh Slice 65 is a Java-based Bakery Management System designed to simplify bakery operations through product management, inventory tracking, and role-based user access.

## Features
- Product management (Add, Edit, Delete, Search)
- Category management
- Stock and inventory monitoring
- Automatic low-stock alerts
- User account management
- Role-based access control
- Manager and Counter Assistant dashboards

## User Roles

### Counter Assistant
- View product details
- Add new products
- Search products by name

### Production Manager
- Create and manage user accounts
- Monitor stock levels
- Generate restock alerts
- Access all system features

## Product Categories
- Bread
- Cakes
- Pastries
- Ingredients

## Technologies Used
- Java Swing
- MySQL
- JDBC
- MVC Architecture
- NetBeans IDE

## System Requirements
- Java JDK 11 or higher
- MySQL 8.0 or higher
- MySQL Connector/J
- NetBeans IDE or another Java IDE

## Database Setup

1. Install and start MySQL.
2. Execute `database_script.sql` to create the database and sample data.
3. Configure the database connection in `src/database/DBConnection.java` according to your MySQL settings.

## Default Login Credentials

| Role | Username | Password |
|---|---|---|
| Manager | admin | admin123 |
| Counter Assistant | assistant | assist123 |

*Change default credentials before using the system in a real environment.*

## How to Run

1. Open the project in NetBeans IDE.
2. Configure the MySQL database.
3. Add MySQL Connector/J to the project libraries.
4. Run the project using NetBeans.

## License
This project is developed for educational purposes.

## Author
Fresh Slice 65 Bakery Management System
