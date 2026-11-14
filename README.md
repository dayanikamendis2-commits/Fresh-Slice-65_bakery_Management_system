# Fresh Slice 65 - Bakery Management System

## Description
Fresh Slice 65 is a comprehensive bakery management system that automates product entry, search, and inventory management. The system is designed for two types of users with role-based access control.

## Features

### User Role 1: Counter Assistant
- View product details
- Add new products
- Search items by name

### User Role 2: Production Manager
- Create user accounts
- Monitor stock levels
- Generate restock alerts
- Full access to all system features

## Product Service Categories
- **Bread** - Various bread products
- **Cakes** - Specialty cakes
- **Pastries** - Fresh pastries
- **Ingredients** - Raw baking ingredients

## System Requirements
- Java JDK 11 or higher
- MySQL 8.0 or higher
- MySQL Connector/J (JDBC driver)
- NetBeans IDE (recommended) or any Java IDE

## Database Setup

### 1. Install MySQL
Make sure MySQL is installed and running on your system.

### 2. Run Database Script
Execute the `database_script.sql` file to create the database and populate it with sample data:

```sql
mysql -u root -p < database_script.sql
```

Or open MySQL Workbench/phpMyAdmin and run the script manually.

### 3. Database Configuration
The database connection is configured in `src/database/DBConnection.java`:
- **Database URL**: `jdbc:mysql://localhost/bakery_db`
- **Username**: `root`
- **Password**: (empty by default)

Modify these settings if your MySQL configuration is different.

## Default Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Manager | admin | admin123 |
| Counter Assistant | assistant | assist123 |

## Project Structure

```
FreshSlice65/
├── src/
│   ├── controller/        # Business logic controllers
│   │   ├── CategoryController.java
│   │   ├── LoginController.java
│   │   ├── ProductController.java
│   │   ├── StockController.java
│   │   └── UserController.java
│   ├── dao/               # Data Access Objects
│   │   ├── CategoryDAO.java
│   │   ├── ProductDAO.java
│   │   ├── StockDAO.java
│   │   └── UserDAO.java
│   ├── database/          # Database utilities
│   │   ├── DBConnection.java
│   │   └── Validator.java
│   ├── model/             # Entity classes
│   │   ├── Category.java
│   │   ├── Product.java
│   │   ├── Stock.java
│   │   └── User.java
│   └── view/              # GUI frames
│       ├── AssistantFrame.java
│       ├── CategoryFrame.java
│       ├── DashboardFrame.java
│       ├── LoginFramej.java
│       ├── ManagerFrame.java
│       ├── ProductFrame.java
│       ├── StockFrame.java
│       └── UserManagementFrame.java
├── database_script.sql    # MySQL database setup script
└── README.md
```

## How to Run

### Using NetBeans IDE
1. Open the project in NetBeans
2. Make sure MySQL Connector/J is in the project libraries
3. Set up the database using the SQL script
4. Run the project (Right-click > Run)

### Using Command Line
1. Compile the project:
```bash
cd src
javac -cp .:mysql-connector-j-8.x.x.jar view/LoginFramej.java
```

2. Run the application:
```bash
java -cp .:mysql-connector-j-8.x.x.jar view.LoginFramej
```

## Features Overview

### Login Screen
- Secure authentication with username and password
- Role-based redirection to appropriate dashboard

### Manager Dashboard
- Quick access to Products, Categories, Stock, and User Management
- Real-time low stock alerts displayed on dashboard
- Visual indicators for stock status

### Counter Assistant Dashboard
- Product search functionality
- Add new products quickly
- View product details

### Product Management
- Add, edit, and delete products
- Assign products to categories
- Set product prices
- Search products by name

### Category Management
- Create product categories (Bread, Cakes, Pastries, Ingredients)
- Edit and delete categories

### Stock Management
- Track inventory levels
- Set restock thresholds
- View low stock alerts
- Color-coded status indicators (OK, LOW STOCK, OUT OF STOCK)

### User Management (Manager Only)
- Create new user accounts
- Assign roles (Manager or Assistant)
- Update and delete user accounts

## Restock Alerts
The system automatically identifies products that need restocking when:
- Quantity reaches or falls below the defined restock level
- Products are marked as "OUT OF STOCK" (quantity = 0)

Managers receive alerts on login and can view detailed reports.

## Technologies Used
- **Java Swing** - GUI framework
- **MySQL** - Database
- **JDBC** - Database connectivity
- **MVC Pattern** - Architecture

## Author
Fresh Slice 65 Bakery Management System © 2024

## License
This project is for educational purposes.
