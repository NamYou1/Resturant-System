-- PostgreSQL POS Database Schema (CORRECTED)
-- Original: db-pos (MySQL) → Converted to PostgreSQL
-- Last Updated: 2026-05-06
-- Changes: Naming corrections, data type fixes, consistency improvements

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================================
-- BASE AUDIT FIELDS (Mixin)
-- ============================================================================
-- All tables should include these fields for audit trail
-- created_at: When record was created (system timestamp)
-- updated_at: When record was last modified (system timestamp)
-- created_by: User ID who created the record
-- updated_by: User ID who last modified the record
-- is_deleted: Soft delete flag (0=active, 1=deleted)
-- deleted_by: User ID who performed soft delete

-- ============================================================================
-- REFERENCE/CONFIGURATION TABLES
-- ============================================================================

-- Table: categories
-- Purpose: Product categories/groups
-- Key Fields:
--   - code: Unique category code
--   - name: Display name
--   - from_time/to_time: Operating hours for this category
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    code VARCHAR(200) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    display VARCHAR(255),
    image VARCHAR(100) DEFAULT 'no_image.png',
    from_time TIME,
    to_time TIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    updated_by INTEGER,
    is_deleted SMALLINT DEFAULT 0,
    deleted_by INTEGER
);

-- Table: subcategory
-- Purpose: Product sub-categories (nested under categories)
CREATE TABLE subcategory (
    id SERIAL PRIMARY KEY,
    section VARCHAR(255),
    category_name VARCHAR(255),
    category_id INTEGER REFERENCES categories(id),
    show_flag SMALLINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_deleted SMALLINT DEFAULT 0
);

-- Table: units
-- Purpose: Product measurement units (kg, liter, piece, etc.)
-- Key Fields:
--   - base_unit: Reference unit ID (for conversion hierarchy)
--   - operation: Conversion operation (*, /)
--   - operation_value: Conversion factor
CREATE TABLE units (
    id SERIAL PRIMARY KEY,
    base_unit INTEGER REFERENCES units(id),
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    operation VARCHAR(5), -- '*' for multiply, '/' for divide
    operation_value NUMERIC(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_deleted SMALLINT DEFAULT 0
);

-- Table: currency
-- Purpose: Exchange rates and currency configuration
CREATE TABLE currency (
    id SERIAL PRIMARY KEY,
    code VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255),
    operation VARCHAR(5), -- Conversion operation
    rate NUMERIC(11,2), -- Exchange rate
    symbol VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: stores
-- Purpose: Store/location configuration for multi-store setup
CREATE TABLE stores (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    logo VARCHAR(40),
    email VARCHAR(100),
    phone VARCHAR(25) NOT NULL,
    address1 VARCHAR(200),
    address2 VARCHAR(200),
    city VARCHAR(20),
    state VARCHAR(20),
    postal_code VARCHAR(8),
    country VARCHAR(25),
    currency_code VARCHAR(3) REFERENCES currency(code),
    receipt_header TEXT,
    receipt_footer TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: customer_groups
-- Purpose: Customer segmentation (VIP, Regular, Wholesale, etc.)
-- Key Fields:
--   - percentage: Discount percentage for this group
CREATE TABLE customer_groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    percentage NUMERIC(5,2), -- Discount percentage
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT DEFAULT 0
);

-- Table: customers
-- Purpose: Customer records
-- Key Fields:
--   - custom_field_1/2: Flexible fields for tenant-specific data
--   - table_group_id: Assignment to table group (for restaurant seating)
--   - price_group_id: Price tier for bulk pricing
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(55) NOT NULL,
    custom_field_1 VARCHAR(255),
    custom_field_2 VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),
    store_id INTEGER REFERENCES stores(id),
    address VARCHAR(255),
    table_group_id INTEGER,
    table_group_name VARCHAR(255),
    price_group_id INTEGER REFERENCES table_price_groups(id),
    price_group_name VARCHAR(255),
    customer_group_id INTEGER REFERENCES customer_groups(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    updated_by INTEGER,
    is_deleted SMALLINT DEFAULT 0,
    deleted_by INTEGER
);

-- Table: customer_groups (already defined above, but ordering matters)
-- Suppliers are similar to customers
CREATE TABLE suppliers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(55) NOT NULL,
    custom_field_1 VARCHAR(255),
    custom_field_2 VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_deleted SMALLINT DEFAULT 0
);

-- Table: price_groups
-- Purpose: Different price tiers for wholesale/bulk pricing
CREATE TABLE price_groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: groups (User Groups/Roles)
-- Purpose: User role definitions (Admin, Manager, Cashier, etc.)
CREATE TABLE groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: group_options
-- Purpose: Product option groups (e.g., "Size", "Color", "Spice Level")
CREATE TABLE group_options (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type INTEGER, -- Type identifier
    required BOOLEAN DEFAULT FALSE, -- Is this option mandatory?
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT DEFAULT 0
);

-- Table: options
-- Purpose: Individual options within a group
-- Example: "Small", "Medium", "Large" under "Size" group
CREATE TABLE options (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    group_id INTEGER REFERENCES group_options(id),
    price NUMERIC(10,3), -- Additional price for this option
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT DEFAULT 0
);

-- Table: group_tables
-- Purpose: Table groupings for restaurant seating
CREATE TABLE group_tables (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    number VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: discount
-- Purpose: Pre-defined discount rules
CREATE TABLE discount (
    id SERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL UNIQUE,
    percentage NUMERIC(5,2) NOT NULL,
    show_flag SMALLINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: expense_type
-- Purpose: Categorize expenses (Rent, Utilities, etc.)
CREATE TABLE expense_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT DEFAULT 0,
    deleted_by INTEGER
);

-- Table: bank
-- Purpose: Bank account configuration
CREATE TABLE bank (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    account_number VARCHAR(255),
    amount NUMERIC(12,4) DEFAULT 0,
    is_default BOOLEAN DEFAULT FALSE,
    statement VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- PRODUCT TABLES
-- ============================================================================

-- Table: products
-- Purpose: Product/SKU master data
-- Key Fields:
--   - barcode_symbology: Barcode format (code39, ean13, etc.)
--   - type: standard (single unit) or combo (bundle)
--   - default_sale_unit/purchase_unit: Default units for transactions
--   - alert_quantity: Minimum stock for low stock alerts
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    other_code VARCHAR(50),
    name VARCHAR(255) NOT NULL,
    other_name VARCHAR(500),
    category_id INTEGER NOT NULL REFERENCES categories(id) DEFAULT 1,
    price NUMERIC(25,4) NOT NULL,
    image VARCHAR(255) DEFAULT 'no_image.png',
    tax VARCHAR(20),
    cost NUMERIC(25,4),
    tax_method SMALLINT DEFAULT 1, -- 1=inclusive, 0=exclusive
    quantity NUMERIC(15,4) DEFAULT 0,
    barcode_symbology VARCHAR(20) DEFAULT 'code39', -- code39, ean13, code128, etc.
    type VARCHAR(20) DEFAULT 'standard', -- standard, combo
    details TEXT,
    alert_quantity NUMERIC(10,4) DEFAULT 0,
    default_sale_unit INTEGER REFERENCES units(id),
    default_purchase_unit INTEGER REFERENCES units(id),
    section INTEGER,
    unit INTEGER REFERENCES units(id),
    printer INTEGER,
    show_flag SMALLINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_deleted SMALLINT DEFAULT 0
);

-- Table: product_prices
-- Purpose: Multi-tier pricing by unit and customer group
CREATE TABLE product_prices (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES products(id),
    unit_id INTEGER REFERENCES units(id),
    price NUMERIC(12,4),
    group_id INTEGER REFERENCES price_groups(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: selling_prices
-- Purpose: Sale prices by unit
CREATE TABLE selling_prices (
    id SERIAL PRIMARY KEY,
    product_id INTEGER REFERENCES products(id),
    sale_unit INTEGER REFERENCES units(id),
    unit_price NUMERIC(12,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: product_store_qty
-- Purpose: Track product inventory per store
CREATE TABLE product_store_qty (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES products(id),
    store_id INTEGER NOT NULL REFERENCES stores(id),
    quantity NUMERIC(15,4) DEFAULT 0,
    price NUMERIC(25,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(product_id, store_id)
);

-- Table: costings
-- Purpose: Product cost tracking
CREATE TABLE costings (
    id SERIAL PRIMARY KEY,
    product_id INTEGER REFERENCES products(id),
    store_id INTEGER REFERENCES stores(id),
    total_selling_cost NUMERIC(18,4), -- Total cost of goods sold
    total_stock_cost NUMERIC(18,4), -- Total inventory value
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: combo_items
-- Purpose: Products that make up a combo/bundle
CREATE TABLE combo_items (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES products(id), -- Combo product
    item_code VARCHAR(20) NOT NULL, -- Component item code
    quantity NUMERIC(12,4) NOT NULL, -- Qty of component in combo
    price NUMERIC(25,4),
    cost NUMERIC(25,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: product_modify
-- Purpose: Product modifications/customizations
-- Example: Extra cheese (+$1), Large size (+$0.50)
CREATE TABLE product_modify (
    id SERIAL PRIMARY KEY,
    product_id INTEGER REFERENCES products(id),
    option_item_id INTEGER REFERENCES options(id),
    price NUMERIC(10,4), -- Price adjustment
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- PURCHASE TABLES
-- ============================================================================

-- Table: purchases
-- Purpose: Purchase orders from suppliers
-- Key Fields:
--   - purchase_status: pending, received, cancelled
--   - payment_status: unpaid, partial, paid
CREATE TABLE purchases (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(55) NOT NULL UNIQUE,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    note VARCHAR(1000),
    total NUMERIC(25,4),
    attachment VARCHAR(255),
    supplier_id INTEGER REFERENCES suppliers(id),
    received BOOLEAN DEFAULT FALSE,
    created_by INTEGER NOT NULL,
    store_id INTEGER NOT NULL REFERENCES stores(id) DEFAULT 1,
    product_discount NUMERIC(12,3), -- Discount on individual items
    order_discount NUMERIC(12,3), -- Discount on total
    total_discount NUMERIC(12,3),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_deleted SMALLINT DEFAULT 0,
    deleted_by INTEGER,
    updated_by INTEGER,
    no INTEGER,
    supplier VARCHAR(255),
    purchase_status VARCHAR(20), -- pending, received, cancelled
    grand_total NUMERIC(14,4),
    paid NUMERIC(14,4),
    payment_status VARCHAR(20) -- unpaid, partial, paid
);

-- Table: purchase_items
-- Purpose: Individual line items in a purchase order
CREATE TABLE purchase_items (
    id SERIAL PRIMARY KEY,
    purchase_id INTEGER NOT NULL REFERENCES purchases(id),
    product_id INTEGER NOT NULL REFERENCES products(id),
    quantity NUMERIC(15,4) NOT NULL,
    total_discount NUMERIC(13,4),
    item_discount NUMERIC(13,4),
    discount VARCHAR(20),
    unit_quantity NUMERIC(13,4),
    cost NUMERIC(25,4) NOT NULL,
    subtotal NUMERIC(25,4) NOT NULL,
    product_unit INTEGER REFERENCES units(id),
    transaction_unit INTEGER, -- Unit used for transaction
    operation_value NUMERIC(13,4), -- Conversion factor
    real_unit_cost NUMERIC(13,4),
    store_id INTEGER REFERENCES stores(id),
    quantity_balance VARCHAR(255), -- Outstanding quantity
    quantity_received VARCHAR(255), -- Quantity received
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- SALES TABLES
-- ============================================================================

-- Table: sales
-- Purpose: Sales/invoice records
-- Key Fields:
--   - grand_total: Total including tax and discount
--   - sale_status: completed, pending, cancelled
--   - pos: Point of Sale flag (1=POS, 0=Manual)
CREATE TABLE sales (
    id SERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    customer_id INTEGER NOT NULL REFERENCES customers(id),
    customer_name VARCHAR(55) NOT NULL,
    total NUMERIC(25,4) NOT NULL,
    product_discount NUMERIC(25,4), -- Item-level discounts
    order_discount_id VARCHAR(20),
    order_discount NUMERIC(25,4), -- Header-level discount
    total_discount NUMERIC(25,4),
    product_tax NUMERIC(25,4),
    order_tax_id VARCHAR(20),
    order_tax NUMERIC(25,4),
    total_tax NUMERIC(25,4),
    grand_total NUMERIC(25,4) NOT NULL,
    total_items INTEGER,
    total_quantity NUMERIC(15,4),
    paid NUMERIC(25,4),
    created_by INTEGER,
    updated_by INTEGER,
    updated_at TIMESTAMP,
    note VARCHAR(1000),
    status VARCHAR(20), -- completed, pending, cancelled
    rounding NUMERIC(10,4), -- Rounding adjustment
    store_id INTEGER NOT NULL REFERENCES stores(id) DEFAULT 1,
    hold_reference VARCHAR(255), -- Reference if sale was on hold
    no INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT DEFAULT 0,
    deleted_by INTEGER,
    pos SMALLINT DEFAULT 0, -- 1=POS transaction, 0=Manual
    sale_status VARCHAR(50), -- completed, pending, cancelled
    total_people VARCHAR(10), -- For restaurant
    client INTEGER REFERENCES customers(id),
    voucher_value NUMERIC(14,3),
    order_voucher VARCHAR(10),
    voucher_code INTEGER,
    print_count INTEGER DEFAULT 0,
    deposit NUMERIC(10,4), -- Advance payment/deposit
    waiting_number VARCHAR(50) -- Queue number
);

-- Table: sale_items
-- Purpose: Individual products in a sale
CREATE TABLE sale_items (
    id SERIAL PRIMARY KEY,
    sale_id INTEGER NOT NULL REFERENCES sales(id),
    product_id INTEGER NOT NULL REFERENCES products(id),
    quantity NUMERIC(15,4) NOT NULL,
    unit_price NUMERIC(25,4) NOT NULL,
    net_unit_price NUMERIC(25,4) NOT NULL, -- Price after discount
    discount VARCHAR(20),
    item_discount NUMERIC(25,4),
    tax INTEGER,
    item_tax NUMERIC(25,4),
    subtotal NUMERIC(25,4) NOT NULL,
    real_unit_price NUMERIC(25,4),
    cost NUMERIC(25,4) DEFAULT 0,
    product_code VARCHAR(50),
    product_name VARCHAR(50),
    comment VARCHAR(255),
    product_unit INTEGER REFERENCES units(id),
    unit_quantity NUMERIC(12,2),
    sale_unit INTEGER REFERENCES units(id),
    modify_name VARCHAR(255), -- Product modifications applied
    modify_id VARCHAR(100),
    modify_total NUMERIC(14,4), -- Total modification charges
    quantity_print NUMERIC(12,2),
    sale_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: suspended_sales
-- Purpose: On-hold/suspended sales (e.g., customer stepped away)
CREATE TABLE suspended_sales (
    id SERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    customer_id INTEGER NOT NULL REFERENCES customers(id),
    customer_name VARCHAR(55) NOT NULL,
    total NUMERIC(25,4) NOT NULL,
    product_discount NUMERIC(25,4),
    order_discount_id VARCHAR(20),
    order_discount NUMERIC(25,4),
    total_discount NUMERIC(25,4),
    product_tax NUMERIC(25,4),
    order_tax_id VARCHAR(20),
    order_tax NUMERIC(25,4),
    total_tax NUMERIC(25,4),
    grand_total NUMERIC(25,4) NOT NULL,
    total_items INTEGER,
    total_quantity NUMERIC(15,4),
    paid NUMERIC(25,4),
    created_by INTEGER,
    updated_by INTEGER,
    updated_at TIMESTAMP,
    note VARCHAR(1000),
    hold_reference VARCHAR(255),
    store_id INTEGER NOT NULL REFERENCES stores(id) DEFAULT 1,
    total_people VARCHAR(10),
    client INTEGER REFERENCES customers(id),
    print_bill INTEGER,
    waiting_number VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT DEFAULT 0
);

-- Table: suspended_items
-- Purpose: Items in suspended sales
CREATE TABLE suspended_items (
    id SERIAL PRIMARY KEY,
    suspend_id INTEGER NOT NULL REFERENCES suspended_sales(id),
    product_id INTEGER NOT NULL REFERENCES products(id),
    quantity NUMERIC(15,4) NOT NULL,
    unit_price NUMERIC(25,4) NOT NULL,
    net_unit_price NUMERIC(25,4) NOT NULL,
    discount VARCHAR(20),
    item_discount NUMERIC(25,4),
    tax INTEGER,
    item_tax NUMERIC(25,4),
    subtotal NUMERIC(25,4) NOT NULL,
    real_unit_price NUMERIC(25,4),
    cost NUMERIC(25,4) DEFAULT 0,
    product_code VARCHAR(50),
    product_name VARCHAR(50),
    comment VARCHAR(255),
    product_unit INTEGER REFERENCES units(id),
    unit_quantity NUMERIC(12,2),
    sale_unit INTEGER REFERENCES units(id),
    modify_name VARCHAR(255),
    modify_id VARCHAR(100),
    modify_total NUMERIC(14,4),
    quantity_print NUMERIC(12,2),
    is_print INTEGER DEFAULT 0,
    sale_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- PAYMENT TABLES
-- ============================================================================

-- Table: payments
-- Purpose: Payment transactions for sales and purchases
-- Key Fields:
--   - paid_by: cash, card, cheque, bank_transfer, etc.
--   - pos_paid: Amount actually paid at POS
--   - pos_balance: Outstanding balance
CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sale_id INTEGER REFERENCES sales(id),
    customer_id INTEGER REFERENCES customers(id),
    transaction_id VARCHAR(50),
    paid_by VARCHAR(20) NOT NULL, -- cash, card, cheque, bank, etc.
    cheque_no VARCHAR(20),
    credit_card_no VARCHAR(20),
    credit_card_holder VARCHAR(25),
    credit_card_month VARCHAR(2),
    credit_card_year VARCHAR(4),
    credit_card_type VARCHAR(20),
    amount NUMERIC(25,4) NOT NULL,
    currency VARCHAR(3),
    created_by INTEGER NOT NULL,
    attachment VARCHAR(55),
    note VARCHAR(1000),
    pos_paid NUMERIC(25,4) DEFAULT 0, -- Amount paid at POS
    pos_balance NUMERIC(25,4) DEFAULT 0, -- Outstanding amount
    gift_card_no VARCHAR(20),
    reference VARCHAR(50),
    updated_by INTEGER,
    updated_at TIMESTAMP,
    store_id INTEGER NOT NULL REFERENCES stores(id) DEFAULT 1,
    statement_id INTEGER NOT NULL REFERENCES statements(id),
    supplier_id INTEGER REFERENCES suppliers(id),
    purchase_id INTEGER REFERENCES purchases(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: bank_transactions
-- Purpose: Bank account transactions
CREATE TABLE bank_transactions (
    id SERIAL PRIMARY KEY,
    bank_id INTEGER REFERENCES bank(id),
    amount NUMERIC(12,4),
    transaction_date TIMESTAMP,
    created_by VARCHAR(255),
    created_at TIMESTAMP,
    type VARCHAR(20), -- debit, credit
    sale_id INTEGER REFERENCES sales(id),
    expense_id INTEGER REFERENCES expenses(id),
    purchase_id INTEGER REFERENCES purchases(id),
    date DATE,
    status VARCHAR(255),
    statement_id INTEGER REFERENCES statements(id)
);

-- Table: statements
-- Purpose: Account statements/reconciliation
CREATE TABLE statements (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES customers(id),
    date TIMESTAMP,
    amount NUMERIC(14,4),
    store_id INTEGER REFERENCES stores(id),
    supplier_id INTEGER REFERENCES suppliers(id),
    reference VARCHAR(100),
    note VARCHAR(255),
    no INTEGER NOT NULL,
    paid_by INTEGER NOT NULL,
    file VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT DEFAULT 0,
    deleted_by INTEGER,
    created_by INTEGER
);

-- ============================================================================
-- EXPENSE TABLES
-- ============================================================================

-- Table: expenses
-- Purpose: Operational expenses
CREATE TABLE expenses (
    id SERIAL PRIMARY KEY,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reference VARCHAR(50) NOT NULL UNIQUE,
    amount NUMERIC(25,4) NOT NULL,
    note VARCHAR(1000),
    created_by VARCHAR(55) NOT NULL,
    attachment VARCHAR(55),
    store_id INTEGER NOT NULL REFERENCES stores(id) DEFAULT 1,
    bank_id INTEGER REFERENCES bank(id),
    type INTEGER REFERENCES expense_type(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT DEFAULT 0
);

-- ============================================================================
-- ADJUSTMENT TABLES
-- ============================================================================

-- Table: adjustments
-- Purpose: Stock adjustments (inventory corrections)
CREATE TABLE adjustments (
    id SERIAL PRIMARY KEY,
    date TIMESTAMP,
    reference_no VARCHAR(100),
    status VARCHAR(255),
    total NUMERIC(14,4),
    store_id INTEGER REFERENCES stores(id),
    note VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    file VARCHAR(255),
    no INTEGER,
    is_deleted SMALLINT DEFAULT 0,
    deleted_by INTEGER
);

-- Table: adjustment_items
-- Purpose: Individual items in a stock adjustment
CREATE TABLE adjustment_items (
    id SERIAL PRIMARY KEY,
    adjust_id INTEGER REFERENCES adjustments(id),
    product_id INTEGER REFERENCES products(id),
    quantity NUMERIC(14,4),
    product_unit INTEGER REFERENCES units(id),
    transaction_unit INTEGER,
    unit_quantity NUMERIC(14,4),
    real_unit_cost NUMERIC(14,4),
    subtotal NUMERIC(14,4),
    quantity_per_unit NUMERIC(14,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- TRANSFER TABLES
-- ============================================================================

-- Table: transfers
-- Purpose: Stock transfers between stores
CREATE TABLE transfers (
    id SERIAL PRIMARY KEY,
    transfer_no VARCHAR(55) NOT NULL UNIQUE,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    from_store_id INTEGER NOT NULL REFERENCES stores(id),
    to_store_id INTEGER NOT NULL REFERENCES stores(id),
    note VARCHAR(250),
    total NUMERIC(25,4),
    grand_total NUMERIC(25,4),
    created_by VARCHAR(255),
    status VARCHAR(55) NOT NULL DEFAULT 'pending', -- pending, received, cancelled
    shipping NUMERIC(25,4) DEFAULT 0,
    attachment VARCHAR(55),
    is_deleted SMALLINT DEFAULT 0,
    no SMALLINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: transfer_items
-- Purpose: Items in a store transfer
CREATE TABLE transfer_items (
    id SERIAL PRIMARY KEY,
    transfer_id INTEGER NOT NULL REFERENCES transfers(id),
    product_id INTEGER NOT NULL REFERENCES products(id),
    product_code VARCHAR(255),
    product_name VARCHAR(255),
    quantity NUMERIC(14,4) NOT NULL,
    subtotal NUMERIC(14,4),
    cost NUMERIC(14,4),
    unit_price NUMERIC(14,4),
    product_unit INTEGER REFERENCES units(id),
    unit_quantity NUMERIC(15,4),
    transaction_unit INTEGER,
    from_store INTEGER REFERENCES stores(id),
    to_store INTEGER REFERENCES stores(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- TRANSACTION LOG TABLE
-- ============================================================================

-- Table: transactions
-- Purpose: Audit log of all inventory movements
-- Key Fields:
--   - type: sale, purchase, adjustment, transfer, return
--   - status: pending, completed, cancelled
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    store_id INTEGER REFERENCES stores(id),
    product_id INTEGER REFERENCES products(id),
    quantity NUMERIC(12,2),
    unit_id INTEGER REFERENCES units(id),
    unit_quantity NUMERIC(12,2),
    transaction_unit_id INTEGER,
    quantity_per_unit NUMERIC(12,2),
    sale_id INTEGER REFERENCES sales(id),
    adjust_id INTEGER REFERENCES adjustments(id),
    purchase_id INTEGER REFERENCES purchases(id),
    transfer_id INTEGER REFERENCES transfers(id),
    sale_return_id INTEGER,
    purchase_return_id INTEGER,
    status VARCHAR(20), -- pending, completed, cancelled
    created_by INTEGER,
    type VARCHAR(10), -- sale, purchase, adjust, transfer, return
    total_cost NUMERIC(12,4),
    total_price NUMERIC(12,4),
    total_discount NUMERIC(12,4),
    total_tax NUMERIC(12,4),
    date TIMESTAMP,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- RESTAURANT FEATURES (POS-specific)
-- ============================================================================

-- Table: bookings
-- Purpose: Table reservations
CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    date DATE,
    time TIME,
    customer_name VARCHAR(255),
    customer_phone VARCHAR(255),
    total_people DECIMAL(5,0),
    table_id INTEGER REFERENCES group_tables(id),
    note VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_deleted SMALLINT DEFAULT 0,
    deleted_by INTEGER
);

-- Table: deliveries
-- Purpose: Delivery orders
CREATE TABLE deliveries (
    id INTEGER PRIMARY KEY,
    date TIMESTAMP,
    reference_no VARCHAR(255),
    sales_reference VARCHAR(255),
    customer VARCHAR(255),
    status VARCHAR(255),
    delivered_by VARCHAR(255),
    received_by VARCHAR(255),
    address VARCHAR(255),
    note VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: promotions
-- Purpose: Sales promotions
CREATE TABLE promotions (
    id SERIAL PRIMARY KEY,
    date TIMESTAMP,
    from_date DATE,
    to_date DATE,
    promotion_type VARCHAR(255),
    value VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_deleted SMALLINT DEFAULT 0,
    deleted_by INTEGER,
    from_time TIME,
    to_time TIME,
    store INTEGER REFERENCES stores(id),
    status VARCHAR(20) -- active, inactive, expired
);

-- Table: promotion_items
-- Purpose: Products included in promotions
CREATE TABLE promotion_items (
    id SERIAL PRIMARY KEY,
    promotion_id INTEGER REFERENCES promotions(id),
    product_id INTEGER REFERENCES products(id),
    unit VARCHAR(255),
    price NUMERIC(14,3),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: gift_cards
-- Purpose: Gift card management
CREATE TABLE gift_cards (
    id SERIAL PRIMARY KEY,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    card_number VARCHAR(20) NOT NULL UNIQUE,
    value NUMERIC(25,4) NOT NULL,
    customer_id INTEGER REFERENCES customers(id),
    balance NUMERIC(25,4) NOT NULL,
    expiry DATE,
    created_by INTEGER,
    store_id INTEGER REFERENCES stores(id),
    is_active BOOLEAN DEFAULT TRUE
);

-- Table: vouchers
-- Purpose: Discount vouchers/coupons
CREATE TABLE vouchers (
    id SERIAL PRIMARY KEY,
    code VARCHAR(250) NOT NULL UNIQUE,
    type VARCHAR(250), -- percentage, fixed amount
    value VARCHAR(11) NOT NULL,
    start_date DATE NOT NULL,
    expire_date DATE NOT NULL,
    used SMALLINT DEFAULT 0,
    status VARCHAR(250), -- active, inactive, expired
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: waitings
-- Purpose: Queue management
CREATE TABLE waitings (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    number VARCHAR(3),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- USER & SECURITY TABLES
-- ============================================================================

-- Table: users
-- Purpose: System users with role-based permissions
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    last_ip_address INET,
    ip_address INET,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Should be hashed
    salt VARCHAR(40),
    email VARCHAR(100) NOT NULL UNIQUE,
    activation_code VARCHAR(40),
    forgotten_password_code VARCHAR(40),
    forgotten_password_time INTEGER,
    remember_code VARCHAR(40),
    created_on INTEGER NOT NULL,
    last_login INTEGER,
    active SMALLINT DEFAULT 1,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    company VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(55),
    gender VARCHAR(20),
    group_id INTEGER NOT NULL REFERENCES groups(id) DEFAULT 2,
    store_id INTEGER REFERENCES stores(id),
    show_all_records BOOLEAN DEFAULT TRUE, -- Can view all stores
    can_edit_selling_price BOOLEAN DEFAULT FALSE,
    can_apply_discount BOOLEAN DEFAULT FALSE,
    is_visible BOOLEAN DEFAULT TRUE,
    can_process_payment BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: user_logins
-- Purpose: Audit trail of user logins
CREATE TABLE user_logins (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    company_id INTEGER,
    ip_address INET NOT NULL,
    login VARCHAR(100) NOT NULL,
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: login_attempts
-- Purpose: Track failed login attempts (security)
CREATE TABLE login_attempts (
    id SERIAL PRIMARY KEY,
    ip_address INET NOT NULL,
    login VARCHAR(100) NOT NULL,
    time INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: permissions
-- Purpose: Role-based access control (RBAC)
CREATE TABLE permissions (
    id SERIAL PRIMARY KEY,
    group_id INTEGER NOT NULL REFERENCES groups(id),
    -- Product Management
    product_view BOOLEAN,
    product_add BOOLEAN,
    product_edit BOOLEAN,
    product_delete BOOLEAN,
    product_cost BOOLEAN,
    product_price BOOLEAN,
    product_adjustment BOOLEAN,
    product_import BOOLEAN,
    product_barcode BOOLEAN,
    -- Category Management
    category_view BOOLEAN,
    category_add BOOLEAN,
    category_edit BOOLEAN,
    category_delete BOOLEAN,
    category_import BOOLEAN,
    -- Sales
    sale_view BOOLEAN,
    sale_add BOOLEAN,
    sale_edit BOOLEAN,
    sale_delete BOOLEAN,
    pos BOOLEAN,
    sale_payment BOOLEAN,
    view_payment BOOLEAN,
    sale_return_add BOOLEAN,
    sale_return_view BOOLEAN,
    sale_list_bill BOOLEAN,
    -- Purchases
    purchase_view BOOLEAN,
    purchase_add BOOLEAN,
    purchase_edit BOOLEAN,
    purchase_delete BOOLEAN,
    purchase_return_add BOOLEAN,
    purchase_return BOOLEAN,
    purchase_payment BOOLEAN,
    list_payments BOOLEAN,
    -- Expenses
    add_expense BOOLEAN,
    list_expenses BOOLEAN,
    -- Customers
    customer_view BOOLEAN,
    customer_add BOOLEAN,
    customer_edit BOOLEAN,
    customer_delete BOOLEAN,
    -- Suppliers
    supplier_view BOOLEAN,
    supplier_add BOOLEAN,
    supplier_edit BOOLEAN,
    supplier_delete BOOLEAN,
    -- Units
    unit_view BOOLEAN,
    unit_add BOOLEAN,
    unit_edit BOOLEAN,
    unit_delete BOOLEAN,
    -- Bank
    bank_view BOOLEAN,
    bank_add BOOLEAN,
    bank_edit BOOLEAN,
    bank_delete BOOLEAN,
    -- Currency
    currency_view BOOLEAN,
    currency_add BOOLEAN,
    currency_edit BOOLEAN,
    currency_delete BOOLEAN,
    -- Reports
    reports_daily_sales BOOLEAN,
    reports_monthly_sales BOOLEAN,
    reports_sale_reports BOOLEAN,
    reports_sale_item_reports BOOLEAN,
    reports_payment_reports BOOLEAN,
    reports_stock_reports BOOLEAN,
    reports_bank_reports BOOLEAN,
    reports_register_reports BOOLEAN,
    reports_top_product_reports BOOLEAN,
    reports_purchase_reports BOOLEAN,
    reports_purchase_item_reports BOOLEAN,
    reports_stock_in_out_report BOOLEAN,
    reports_account_receivable_report BOOLEAN,
    reports_account_payable_report BOOLEAN,
    -- Product Options
    product_group_option BOOLEAN,
    product_option BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: registers
-- Purpose: POS register open/close records
CREATE TABLE registers (
    id SERIAL PRIMARY KEY,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER NOT NULL REFERENCES users(id),
    cash_in_hand NUMERIC(25,4) NOT NULL,
    status VARCHAR(10) NOT NULL, -- open, closed
    total_cash NUMERIC(25,4),
    total_cheques INTEGER,
    total_credit_card_slips INTEGER,
    total_cash_submitted NUMERIC(25,4),
    total_cheques_submitted INTEGER,
    total_credit_card_slips_submitted INTEGER,
    note TEXT,
    closed_at TIMESTAMP,
    transfer_opened_bills VARCHAR(50),
    closed_by INTEGER REFERENCES users(id),
    store_id INTEGER NOT NULL REFERENCES stores(id) DEFAULT 1
);

-- ============================================================================
-- ADMIN FEATURES
-- ============================================================================

-- Table: cart_admin
-- Purpose: Shopping cart for admin (manual sales entry)
CREATE TABLE cart_admin (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    product_id INTEGER REFERENCES products(id),
    quantity INTEGER,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    device_id VARCHAR(255),
    customer_id INTEGER REFERENCES customers(id),
    remark VARCHAR(255)
);

-- Table: cart_admin_detail
-- Purpose: Cart item modifications
CREATE TABLE cart_admin_detail (
    id SERIAL PRIMARY KEY,
    cart_id INTEGER REFERENCES cart_admin(id),
    modify_id INTEGER,
    modify_name VARCHAR(255),
    is_free BOOLEAN DEFAULT FALSE
);

-- Table: order_items
-- Purpose: Order queue for kitchen
CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    item_id INTEGER,
    quantity NUMERIC(10,2),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    table_id INTEGER REFERENCES group_tables(id)
);

-- Table: order_numbers
-- Purpose: Order number sequence
CREATE TABLE order_numbers (
    id SERIAL PRIMARY KEY,
    no INTEGER
);

-- Table: void_items
-- Purpose: Voided transaction items
CREATE TABLE void_items (
    id SERIAL PRIMARY KEY,
    item_id INTEGER,
    comments VARCHAR(255),
    user_id INTEGER REFERENCES users(id),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity NUMERIC(12,2),
    price NUMERIC(10,2)
);

-- ============================================================================
-- CONFIGURATION & SETTINGS
-- ============================================================================

-- Table: settings
-- Purpose: System-wide configuration
CREATE TABLE settings (
    setting_id INTEGER PRIMARY KEY DEFAULT 1,
    logo VARCHAR(255),
    site_name VARCHAR(55),
    tel VARCHAR(20),
    date_format VARCHAR(20), -- yyyy-mm-dd, mm/dd/yyyy, etc.
    time_format VARCHAR(20), -- 24h, 12h
    default_email VARCHAR(100),
    language VARCHAR(20), -- en, es, fr, etc.
    version VARCHAR(10) DEFAULT '1.0',
    theme VARCHAR(20), -- light, dark, etc.
    timezone VARCHAR(255) DEFAULT '0',
    protocol VARCHAR(20) DEFAULT 'mail', -- mail, smtp, etc.
    smtp_host VARCHAR(255),
    smtp_user VARCHAR(100),
    smtp_pass VARCHAR(255),
    smtp_port VARCHAR(10) DEFAULT '25',
    smtp_crypto VARCHAR(5), -- ssl, tls, none
    enable_mail SMALLINT DEFAULT 1,
    captcha SMALLINT DEFAULT 1,
    mail_path VARCHAR(55),
    currency_prefix VARCHAR(3),
    default_customer INTEGER REFERENCES customers(id),
    default_tax_rate VARCHAR(20),
    rows_per_page INTEGER DEFAULT 20,
    total_rows INTEGER,
    header VARCHAR(1000),
    footer VARCHAR(1000),
    default_style SMALLINT DEFAULT 1, -- Bootstrap style
    display_keyboard SMALLINT DEFAULT 1,
    default_category INTEGER REFERENCES categories(id),
    default_discount VARCHAR(20),
    item_addition SMALLINT DEFAULT 1,
    barcode_symbology VARCHAR(55), -- Default barcode type
    product_limit SMALLINT DEFAULT 1000,
    decimal_places SMALLINT DEFAULT 2,
    thousands_separator VARCHAR(2) DEFAULT ',',
    decimal_separator VARCHAR(2) DEFAULT '.',
    -- POS Keyboard Shortcuts
    focus_add_item VARCHAR(55),
    add_customer VARCHAR(55),
    toggle_category_slider VARCHAR(55),
    cancel_sale VARCHAR(55),
    suspend_sale VARCHAR(55),
    print_order VARCHAR(55),
    print_bill VARCHAR(55),
    finalize_sale VARCHAR(55),
    today_sale VARCHAR(55),
    open_hold_bills VARCHAR(55),
    close_register VARCHAR(55),
    -- Printer Configuration
    use_java_applet SMALLINT DEFAULT 0,
    receipt_printer VARCHAR(55),
    pos_printers VARCHAR(255),
    cash_drawer_codes VARCHAR(55),
    chars_per_line SMALLINT DEFAULT 42,
    -- Rounding
    enable_rounding SMALLINT DEFAULT 0,
    -- Security
    pin_code VARCHAR(20),
    -- Payment Gateway
    stripe_enabled SMALLINT DEFAULT 0,
    stripe_secret_key VARCHAR(100),
    stripe_publishable_key VARCHAR(100),
    -- License
    purchase_code VARCHAR(100),
    envato_username VARCHAR(50),
    -- UI
    theme_style VARCHAR(25) DEFAULT 'green',
    after_sale_page SMALLINT DEFAULT 1,
    -- Features
    allow_overselling SMALLINT DEFAULT 1,
    enable_multi_store SMALLINT DEFAULT 1,
    quantity_decimal_places SMALLINT DEFAULT 2,
    currency_symbol VARCHAR(55),
    enable_sac SMALLINT DEFAULT 0,
    display_currency_symbol SMALLINT DEFAULT 1,
    enable_remote_printing SMALLINT DEFAULT 1,
    default_printer INTEGER REFERENCES table_printers(id),
    order_printers VARCHAR(55),
    auto_print SMALLINT DEFAULT 0,
    enable_local_printers SMALLINT DEFAULT 1,
    enable_rtl SMALLINT DEFAULT 0, -- Right-to-left for Arabic, etc.
    print_logo SMALLINT DEFAULT 1,
    -- Barcode Configuration
    barcode_type VARCHAR(10) DEFAULT 'weight',
    barcode_chars SMALLINT,
    flag_chars SMALLINT,
    item_code_start SMALLINT,
    item_code_chars SMALLINT,
    price_start SMALLINT,
    price_chars SMALLINT,
    price_divide_by INTEGER,
    weight_start SMALLINT,
    weight_chars SMALLINT,
    weight_divide_by INTEGER,
    allow_edit BOOLEAN DEFAULT FALSE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: printers
-- Purpose: Printer configuration for POS
CREATE TABLE printers (
    id SERIAL PRIMARY KEY,
    title VARCHAR(55) NOT NULL,
    type VARCHAR(25) NOT NULL, -- thermal, inkjet, network, etc.
    profile VARCHAR(25) NOT NULL,
    chars_per_line SMALLINT,
    device_path VARCHAR(255),
    ip_address INET,
    port VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: sessions
-- Purpose: User session management
CREATE TABLE sessions (
    id VARCHAR(40) PRIMARY KEY,
    ip_address VARCHAR(45) NOT NULL,
    timestamp INTEGER NOT NULL DEFAULT 0,
    data BYTEA NOT NULL
);

-- Table: clients
-- Purpose: Client/company records
CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    group_id INTEGER,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- CREATE INDEXES FOR PERFORMANCE
-- ============================================================================

CREATE INDEX idx_adjustments_store_id ON adjustments(store_id);
CREATE INDEX idx_adjustment_items_adjust_id ON adjustment_items(adjust_id);
CREATE INDEX idx_adjustment_items_product_id ON adjustment_items(product_id);
CREATE INDEX idx_bank_transactions_bank_id ON bank_transactions(bank_id);
CREATE INDEX idx_bank_transactions_sale_id ON bank_transactions(sale_id);
CREATE INDEX idx_bookings_table_id ON bookings(table_id);
CREATE INDEX idx_cart_admin_user_id ON cart_admin(user_id);
CREATE INDEX idx_cart_admin_product_id ON cart_admin(product_id);
CREATE INDEX idx_cart_admin_detail_cart_id ON cart_admin_detail(cart_id);
CREATE INDEX idx_customers_table_group_id ON customers(table_group_id);
CREATE INDEX idx_customers_customer_group_id ON customers(customer_group_id);
CREATE INDEX idx_payments_sale_id ON payments(sale_id);
CREATE INDEX idx_payments_customer_id ON payments(customer_id);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_code ON products(code);
CREATE INDEX idx_product_store_qty_product_id ON product_store_qty(product_id);
CREATE INDEX idx_product_store_qty_store_id ON product_store_qty(store_id);
CREATE INDEX idx_purchases_supplier_id ON purchases(supplier_id);
CREATE INDEX idx_purchases_date ON purchases(date);
CREATE INDEX idx_purchase_items_purchase_id ON purchase_items(purchase_id);
CREATE INDEX idx_purchase_items_product_id ON purchase_items(product_id);
CREATE INDEX idx_sales_customer_id ON sales(customer_id);
CREATE INDEX idx_sales_store_id ON sales(store_id);
CREATE INDEX idx_sales_date ON sales(date);
CREATE INDEX idx_sales_status ON sales(status);
CREATE INDEX idx_sale_items_sale_id ON sale_items(sale_id);
CREATE INDEX idx_sale_items_product_id ON sale_items(product_id);
CREATE INDEX idx_suspended_items_suspend_id ON suspended_items(suspend_id);
CREATE INDEX idx_suspended_items_product_id ON suspended_items(product_id);
CREATE INDEX idx_suspended_sales_customer_id ON suspended_sales(customer_id);
CREATE INDEX idx_transactions_sale_id ON transactions(sale_id);
CREATE INDEX idx_transactions_product_id ON transactions(product_id);
CREATE INDEX idx_transactions_date ON transactions(transaction_date);
CREATE INDEX idx_transfers_from_store_id ON transfers(from_store_id);
CREATE INDEX idx_transfers_to_store_id ON transfers(to_store_id);
CREATE INDEX idx_transfer_items_transfer_id ON transfer_items(transfer_id);
CREATE INDEX idx_transfer_items_product_id ON transfer_items(product_id);
CREATE INDEX idx_users_group_id ON users(group_id);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_user_logins_user_id ON user_logins(user_id);
CREATE INDEX idx_customers_name ON customers(name);
CREATE INDEX idx_suppliers_name ON suppliers(name);

-- ============================================================================
-- CREATE DATABASE VIEWS
-- ============================================================================

-- View: Category dates
CREATE VIEW categories_view AS
SELECT 
    id,
    code,
    name,
    display,
    image,
    from_time,
    to_time,
    TO_CHAR(CURRENT_DATE + from_time, 'YYYY-MM-DD HH24:MI') AS from_datetime,
    CASE 
        WHEN from_time > to_time THEN TO_CHAR((CURRENT_DATE + INTERVAL '1 day') + to_time, 'YYYY-MM-DD HH24:MI')
        ELSE TO_CHAR(CURRENT_DATE + to_time, 'YYYY-MM-DD HH24:MI')
    END AS to_datetime
FROM categories
WHERE is_deleted = 0;

-- View: Promotion dates
CREATE VIEW promotions_view AS
SELECT 
    id,
    date,
    from_date,
    to_date,
    promotion_type,
    value,
    created_by,
    is_deleted,
    deleted_by,
    from_time,
    to_time,
    store,
    status,
    TO_CHAR(CURRENT_DATE + from_time, 'YYYY-MM-DD HH24:MI') AS from_datetime,
    CASE 
        WHEN from_time > to_time THEN TO_CHAR((CURRENT_DATE + INTERVAL '1 day') + to_time, 'YYYY-MM-DD HH24:MI')
        ELSE TO_CHAR(CURRENT_DATE + to_time, 'YYYY-MM-DD HH24:MI')
    END AS to_datetime
FROM promotions
WHERE is_deleted = 0;

-- View: Product stock by store
CREATE VIEW product_stock_view AS
SELECT 
    p.id,
    p.code,
    p.name,
    psq.store_id,
    psq.quantity,
    p.alert_quantity,
    CASE 
        WHEN psq.quantity <= p.alert_quantity THEN 'LOW'
        ELSE 'OK'
    END AS stock_status
FROM products p
LEFT JOIN product_store_qty psq ON p.id = psq.product_id
WHERE p.is_deleted = 0;

-- View: Stock movements
CREATE VIEW stock_movements_view AS
SELECT 
    u.id,
    u.base_unit,
    u.code,
    u.name,
    u.operation,
    u.operation_value,
    u.is_deleted,
    p.id AS product_id,
    p.name AS product_name
FROM products p
JOIN units u ON p.default_purchase_unit = u.id OR p.unit = u.id
WHERE p.type = 'standard' 
    AND p.unit <> p.default_purchase_unit 
    AND u.is_deleted = 0 
    AND u.base_unit > 0
GROUP BY p.id, u.id;

-- ============================================================================
-- COMMENTS AND DOCUMENTATION
-- ============================================================================

COMMENT ON TABLE products IS 'Master product/SKU data - core of inventory management';
COMMENT ON TABLE sales IS 'Sales transactions - main revenue table';
COMMENT ON TABLE purchases IS 'Purchase orders - main procurement table';
COMMENT ON TABLE transactions IS 'Inventory movement audit log - tracks all stock changes';
COMMENT ON TABLE users IS 'System users with role-based access control';
COMMENT ON TABLE payments IS 'Payment transactions for sales and purchases';
COMMENT ON TABLE stores IS 'Multi-store configuration for enterprise POS systems';
COMMENT ON COLUMN products.barcode_symbology IS 'Barcode format: code39, ean13, code128, qr, etc.';
COMMENT ON COLUMN sales.grand_total IS 'Final amount including tax and discounts';
COMMENT ON COLUMN sales.pos IS 'Flag: 1 for POS transaction, 0 for manual entry';
COMMENT ON COLUMN purchases.payment_status IS 'Payment progress: unpaid, partial, paid';
COMMENT ON COLUMN payments.pos_paid IS 'Amount actually collected at POS';
COMMENT ON COLUMN payments.pos_balance IS 'Outstanding/due amount';
