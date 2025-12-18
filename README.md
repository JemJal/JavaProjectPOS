# JavaProjectPOS - Point of Sale System

A desktop-based Point of Sale system built with Java Swing for managing retail operations including product inventory, user authentication, and sales transactions.

## Table of Contents
- [Setup Instructions](#setup-instructions)
- [Database Schema](#database-schema)
- [Project Structure](#project-structure)
- [Java Files Documentation](#java-files-documentation)
- [Database Setup](#database-setup)

## Setup Instructions

1. Download and install Window Builder on Eclipse Marketplace
2. Create a `Data` sub directory under your root.
3. Move `rs2xml.jar`, `sqlite.jar` and `sqlitejdbc.jar` files into the Data directory.
4. Open Google Chrome and install `SQLite Manager` extension from the Google Chrome Marketplace.
5. Create a new database on Chrome using the new extension and download it.
6. Move the file you downloaded to your Data sub-directory.
7. On Eclipse, right-click your root directory. Go to `Build Path` -> `Configure Build Path...`
8. Under Java Build Path go to Libraries and click on Modulepath. Click `Add JARs...` and choose your `sqlite.jar` and `sqlitejdbc.jar` files, click Ok. Now, click on Classpath, click `Add JARs...` choose the `rs2xml.jar` file, click Ok. Click `Apply and Close`.
9. Create a Package under src directory.
10. After you right-click the package, go to New -> Other.
11. Under Window Builder -> Swing Designer choose JFrame and click Next. You can start building your app now.
12. When you want to re-open a previously written JFrame code, right-click the file and choose Open With and choose WindowBuilder Editor.

## Database Schema

### Kullanici Table (Users)
```
HesapNo (INTEGER, PRIMARY KEY, AUTOINCREMENT)
├─ Automatically increments when a new user is created
├─ Starts at 1 and increments by 1 for each new record
└─ Uniquely identifies each user

KullaniciAdi (VARCHAR 30) - Username for login
AdiSoyadi (VARCHAR 30) - Full name of the user
PinKodu (INTEGER) - PIN/password for authentication
Gorev (VARCHAR 30) - User role: 'admin', 'super-admin', or 'kasiyer' (cashier)
```

### Urunler Table (Products)
```
UrunNo (INTEGER, PRIMARY KEY, AUTOINCREMENT)
├─ Automatically increments when a new product is added
├─ Starts at 1 and increments by 1 for each new record
└─ Uniquely identifies each product

Ad (VARCHAR 30) - Product name
Fiyat (DECIMAL 10,2) - Product price with 2 decimal places
Stok (INTEGER) - Current stock quantity
```

**How AUTOINCREMENT Works:**
- When you insert a record without specifying the ID, SQLite automatically assigns the next available number
- The sqlite_sequence table tracks the highest ID used for each table
- This ensures no duplicate IDs and maintains data integrity

## Project Structure

```
src/pos/
├── Baglanti.java           - Database connection manager
├── Giris.java              - Login screen (entry point)
├── AdminEkran.java         - Admin dashboard
├── SatisEkran.java         - Sales/cashier interface
├── UrunEkle.java           - Add new product
├── UrunSil.java            - Delete product
├── StokEkle.java           - Add stock to existing product
├── KullaniciEkle.java      - Add new user/cashier
├── KullaniciSil.java       - Delete user/cashier
├── KasiyerEkran.java       - Alternative cashier screen
└── KasiyerEkle.java        - Add new cashier
```

## Java Files Documentation

### Baglanti.java
**Purpose:** Database connection utility
- **Bagla() method:** Establishes connection to SQLite database
  - Loads SQLite JDBC driver: `org.sqlite.JDBC`
  - Connects to `Data/memory.db`
  - Returns Connection object or null if error occurs
  - Shows error dialog if connection fails
- **Usage:** Called by all other screens to get database connection

### Giris.java
**Purpose:** Login screen (application entry point)
- **Main Screen:** 600x500 pixel window with dark gray background
- **Input Fields:**
  - Username text field
  - Password text field
  - Displays default credentials as reference
- **Login Logic:**
  - Gets username and PIN from user input
  - Executes prepared statement: `SELECT * FROM Kullanici WHERE KullaniciAdi = ? AND PinKodu = ?`
  - Uses PreparedStatement to prevent SQL injection
  - Checks user role (Gorev) from database
- **Navigation:**
  - Admin/Super-admin (cnt=1) → Opens AdminEkran with username
  - Cashier (cnt=2) → Opens SatisEkran
  - Invalid credentials → Shows "Başarısız.." error message
- **Security:** Uses PreparedStatement with parameterized queries

### AdminEkran.java
**Purpose:** Admin dashboard for managing products and users
- **Constructor:** Accepts username parameter to track current logged-in admin
  - Stores username in `currentUsername` field for later use
  - Default constructor calls parameterized constructor with empty string
- **Menu Buttons:**
  - **Yeni Urun Ekle** → Opens UrunEkle screen
  - **Stok Ekle** → Opens StokEkle screen
  - **Urun Sil** → Opens UrunSil screen
  - **Yeni Kasiyer Ekle** → Opens KullaniciEkle screen
  - **Kasiyer Sil** → Opens KullaniciSil with current admin username (prevents self-deletion)
  - **Çıkış** → Returns to login screen
- **Access Control:** Only accessible to users with 'admin' or 'super-admin' role

### SatisEkran.java
**Purpose:** Sales/Cashier interface for completing transactions
- **Product Display:**
  - Loads all products with stock > 0 from database
  - Table columns: Ürün Adı, Fiyat, Stok, Satış Miktarı
  - Only "Satış Miktarı" column is editable
- **Transaction Logic:**
  - **Hesapla Button:** Calculates total price (fiyat × satisMiktarı) and updates label
  - **Kaydet Button:**
    - Validates that total > 0
    - Uses database transactions (setAutoCommit(false))
    - Checks if ordered quantity ≤ available stock for each item
    - If valid: Updates product stock and commits transaction
    - If invalid: Rolls back transaction and shows error
    - Shows confirmation dialog with total amount
- **Error Handling:** Automatic rollback on insufficient stock or database errors
- **Inventory Management:** Uses PreparedStatement to update stock atomically

### UrunEkle.java
**Purpose:** Add new products to inventory
- **Input Fields:**
  - Ürün Adı (Product name)
  - Fiyat (Product price) - converts comma to period for decimal
  - Miktar (Initial stock quantity)
- **Validation:**
  - Checks all fields are filled
  - Validates price and quantity are valid numbers
  - Shows error messages for invalid input
- **Database Operation:**
  - INSERT statement: `INSERT INTO Urunler (Ad, Fiyat, Stok) VALUES (?, ?, ?)`
  - Auto-incremented UrunNo is assigned by database
  - Clears form after successful insertion
- **Navigation:** Geri button returns to login screen

### UrunSil.java
**Purpose:** Delete products from inventory
- **Display:** JTable showing all products (UrunNo, Ad, Fiyat, Stok)
- **Deletion Process:**
  - User enters product UrunNo in text field
  - Shows confirmation dialog before deletion
  - DELETE statement: `DELETE FROM Urunler WHERE UrunNo = ?`
  - Refreshes table after successful deletion
  - Shows error if product number not found
- **Error Handling:** Validates input is numeric, shows appropriate error messages
- **Navigation:** Geri button returns to login screen

### StokEkle.java
**Purpose:** Add stock quantity to existing products
- **Display:** JTable with all products
- **Input Fields:**
  - Ürün No (product ID)
  - Eklenecek Miktar (quantity to add)
- **Update Logic:**
  - UPDATE statement: `UPDATE Urunler SET Stok = Stok + ? WHERE UrunNo = ?`
  - Validates quantity > 0
  - Validates product exists
  - Updates database and refreshes table
- **Validation:** Prevents negative stock additions, validates numeric input
- **Navigation:** Geri button returns to login screen

### KullaniciEkle.java
**Purpose:** Add new users/cashiers to the system
- **Input Fields:**
  - Kullanici Adi (Username)
  - Adi Soyadi (Full name)
  - Pin Kodu (PIN/Password)
  - Gorev (Role dropdown) - Options: "kasiyer", "admin"
- **Validation:**
  - All fields must be filled
  - PIN must be numeric
  - Shows appropriate error messages
- **Database Operation:**
  - INSERT statement: `INSERT INTO Kullanici (KullaniciAdi, AdiSoyadi, PinKodu, Gorev) VALUES (?, ?, ?, ?)`
  - Auto-incremented HesapNo assigned by database
  - Clears form after successful insertion
- **Access Control:** Only accessible from admin screen
- **Navigation:** Geri button returns to login screen

### KullaniciSil.java
**Purpose:** Delete users/cashiers from the system with safety checks
- **Constructor:** Accepts current admin username to prevent self-deletion
- **Display:** JTable showing all users (HesapNo, KullaniciAdi, AdiSoyadi, Gorev)
- **Deletion Safeguards:**
  - Prevents deletion of 'super-admin' role users
  - Prevents admin from deleting their own account
  - Requires HesapNo input and confirmation
- **Deletion Logic:**
  - Fetches user data first to validate existence and role
  - Shows confirmation dialog before deletion
  - DELETE statement: `DELETE FROM Kullanici WHERE HesapNo = ?`
  - Refreshes table after successful deletion
- **Error Handling:** Validates numeric input, checks user permissions
- **Navigation:** Geri button returns to login screen

### KasiyerEkran.java
**Purpose:** Alternative cashier/sales interface
- Similar functionality to SatisEkran
- Provides alternative screen layout for cashier operations

### KasiyerEkle.java
**Purpose:** Add new cashier users
- Similar functionality to KullaniciEkle
- Provides alternative interface for adding cashier accounts

## Database Setup

Use the SQL script below to set up clean tables with sample data:

```SQL
-- Drop and re-create Kullanici table

DROP TABLE Kullanici;

CREATE TABLE Kullanici (
HesapNo INTEGER PRIMARY KEY AUTOINCREMENT,
KullaniciAdi varchar(30),
AdiSoyadi varchar(30),
PinKodu Integer,
Gorev varchar(30)
);

INSERT INTO Kullanici (KullaniciAdi, AdiSoyadi, PinKodu, Gorev)
VALUES
	('admin', 'Admin', 9999, 'admin'),
	('ayse', 'Ayşe Kaya', 1234, 'kasiyer'),
	('mehmet', 'Mehmet Sönmez', 5678, 'kasiyer');

-- Drop and re-create Urunler table

DROP TABLE Urunler;

CREATE TABLE Urunler (
UrunNo INTEGER PRIMARY KEY AUTOINCREMENT,
Ad varchar(30),
Fiyat DECIMAL(10, 2),
Stok INTEGER
);

INSERT INTO Urunler (Ad, Fiyat, Stok)
VALUES
	('Süt', 35.50, 150),
	('Ekmek', 15.00, 200),
	('Domates', 32.90, 75),
	('Beyaz Peynir', 150.75, 60),
	('Makarna', 18.25, 300),
	('Zeytinyağı', 260.00, 40),
	('Yumurta', 45.00, 120),
	('Elma', 19.95, 95),
	('Tavuk', 110.50, 35),
	('Soda', 22.75, 180),
	('Yoğurt', 48.50, 110),
	('Pirinç', 52.00, 250),
	('Kıyma', 380.00, 25),
	('Patates', 17.50, 130),
	('Soğan', 14.00, 140),
	('Deterjan', 215.90, 55),
	('Çay', 145.50, 220),
	('Şeker', 38.75, 400),
	('Un', 42.25, 350),
	('Salça', 55.00, 160),
	('Kahve', 49.90, 85),
	('Limon', 25.00, 90),
	('Salatalık', 28.50, 80),
	('Sucuk', 295.00, 30),
	('Kaşar Peyniri', 210.25, 50),
	('Su', 24.50, 500),
	('Çikolata', 27.80, 450),
	('Bisküvi', 19.00, 400),
	('Tereyağı', 95.75, 70),
	('Mercimek', 58.50, 280);
```

## Testing Default Credentials

- **Admin Login:** Username: `admin` | PIN: `9999`
- **Cashier 1:** Username: `ayse` | PIN: `1234`
- **Cashier 2:** Username: `mehmet` | PIN: `5678`
