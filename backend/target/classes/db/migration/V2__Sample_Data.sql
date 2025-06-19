-- Sample data for E-commerce platform
-- This script inserts sample data for development and testing

-- Insert sample users
INSERT INTO users (first_name, last_name, email, password, role, is_active, is_verified) VALUES
('Admin', 'User', 'admin@ecommercehub.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN', true, true),
('John', 'Doe', 'john.doe@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'USER', true, true),
('Jane', 'Smith', 'jane.smith@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'USER', true, true),
('Bob', 'Johnson', 'bob.johnson@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'USER', true, false),
('Alice', 'Brown', 'alice.brown@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'MODERATOR', true, true)
ON CONFLICT (email) DO NOTHING;

-- Insert sample addresses
INSERT INTO addresses (user_id, name, street, city, state, zip_code, country, type, is_default) VALUES
(2, 'Home Address', '123 Main St', 'New York', 'NY', '10001', 'USA', 'HOME', true),
(2, 'Work Address', '456 Business Ave', 'New York', 'NY', '10002', 'USA', 'WORK', false),
(3, 'Home Address', '789 Oak Street', 'Los Angeles', 'CA', '90210', 'USA', 'HOME', true),
(3, 'Shipping Address', '321 Pine Road', 'Los Angeles', 'CA', '90211', 'USA', 'SHIPPING', false),
(4, 'Home Address', '555 Elm Street', 'Chicago', 'IL', '60601', 'USA', 'HOME', true),
(5, 'Home Address', '777 Maple Ave', 'Miami', 'FL', '33101', 'USA', 'HOME', true)
ON CONFLICT DO NOTHING;

-- Insert sample orders
INSERT INTO orders (order_number, user_id, status, payment_status, payment_method, subtotal, tax, shipping, discount, total, shipping_address, billing_address) VALUES
('ORD-2024-001', 2, 'DELIVERED', 'COMPLETED', 'CARD', 299.99, 30.00, 10.00, 0.00, 339.99, 
 '{"name": "Home Address", "street": "123 Main St", "city": "New York", "state": "NY", "zipCode": "10001", "country": "USA"}',
 '{"name": "Home Address", "street": "123 Main St", "city": "New York", "state": "NY", "zipCode": "10001", "country": "USA"}'),
('ORD-2024-002', 3, 'SHIPPED', 'COMPLETED', 'UPI', 149.99, 15.00, 5.00, 20.00, 149.99,
 '{"name": "Home Address", "street": "789 Oak Street", "city": "Los Angeles", "state": "CA", "zipCode": "90210", "country": "USA"}',
 '{"name": "Home Address", "street": "789 Oak Street", "city": "Los Angeles", "state": "CA", "zipCode": "90210", "country": "USA"}'),
('ORD-2024-003', 2, 'PROCESSING', 'COMPLETED', 'WALLET', 89.99, 9.00, 0.00, 10.00, 88.99,
 '{"name": "Work Address", "street": "456 Business Ave", "city": "New York", "state": "NY", "zipCode": "10002", "country": "USA"}',
 '{"name": "Home Address", "street": "123 Main St", "city": "New York", "state": "NY", "zipCode": "10001", "country": "USA"}'),
('ORD-2024-004', 4, 'PENDING', 'PENDING', 'COD', 199.99, 20.00, 15.00, 0.00, 234.99,
 '{"name": "Home Address", "street": "555 Elm Street", "city": "Chicago", "state": "IL", "zipCode": "60601", "country": "USA"}',
 '{"name": "Home Address", "street": "555 Elm Street", "city": "Chicago", "state": "IL", "zipCode": "60601", "country": "USA"}')
ON CONFLICT (order_number) DO NOTHING;

-- Insert sample order items
INSERT INTO order_items (order_id, product_id, product_name, product_sku, quantity, unit_price, total_price) VALUES
(1, 'prod_001', 'Wireless Bluetooth Headphones', 'WBH-001', 1, 299.99, 299.99),
(2, 'prod_002', 'Smartphone Case', 'SC-002', 2, 24.99, 49.98),
(2, 'prod_003', 'USB-C Cable', 'USB-003', 1, 19.99, 19.99),
(2, 'prod_004', 'Wireless Charger', 'WC-004', 1, 79.99, 79.99),
(3, 'prod_005', 'Bluetooth Speaker', 'BS-005', 1, 89.99, 89.99),
(4, 'prod_006', 'Laptop Stand', 'LS-006', 1, 199.99, 199.99)
ON CONFLICT DO NOTHING;

-- Insert sample cart items
INSERT INTO cart_items (user_id, product_id, quantity, price) VALUES
(2, 'prod_007', 1, 49.99),
(2, 'prod_008', 2, 29.99),
(3, 'prod_009', 1, 199.99),
(4, 'prod_010', 3, 15.99),
(5, 'prod_001', 1, 299.99)
ON CONFLICT (user_id, product_id) DO NOTHING;

-- Insert sample wishlist items
INSERT INTO wishlist_items (user_id, product_id) VALUES
(2, 'prod_011'),
(2, 'prod_012'),
(3, 'prod_013'),
(3, 'prod_014'),
(4, 'prod_015'),
(5, 'prod_016')
ON CONFLICT (user_id, product_id) DO NOTHING;

-- Insert sample product reviews
INSERT INTO product_reviews (user_id, product_id, rating, title, comment, is_verified, is_approved) VALUES
(2, 'prod_001', 5, 'Excellent sound quality!', 'These headphones have amazing sound quality and the battery life is fantastic. Highly recommended!', true, true),
(3, 'prod_001', 4, 'Good value for money', 'Great headphones for the price. The only downside is they can be a bit tight after long use.', true, true),
(2, 'prod_002', 5, 'Perfect fit for my phone', 'This case fits my phone perfectly and provides excellent protection. Love the design!', true, true),
(3, 'prod_003', 4, 'Reliable cable', 'Good quality USB-C cable. Fast charging and data transfer. No issues so far.', false, true),
(4, 'prod_005', 3, 'Average speaker', 'The sound is okay but not exceptional. Good for the price range though.', false, true),
(5, 'prod_001', 5, 'Best purchase ever!', 'I absolutely love these headphones. The noise cancellation is superb and they are very comfortable.', true, true)
ON CONFLICT (user_id, product_id) DO NOTHING;

-- Insert sample payments
INSERT INTO payments (order_id, payment_reference, amount, net_amount, currency, payment_method, status, gateway_transaction_id, processed_at) VALUES
(1, 'PAY-2024-001-ABC123', 339.99, 339.99, 'USD', 'CARD', 'COMPLETED', 'txn_1234567890', NOW() - INTERVAL '5 days'),
(2, 'PAY-2024-002-DEF456', 149.99, 149.99, 'USD', 'UPI', 'COMPLETED', 'upi_9876543210', NOW() - INTERVAL '3 days'),
(3, 'PAY-2024-003-GHI789', 88.99, 88.99, 'USD', 'WALLET', 'COMPLETED', 'wallet_5555666677', NOW() - INTERVAL '1 day')
ON CONFLICT (payment_reference) DO NOTHING;

-- Update order tracking numbers for shipped/delivered orders
UPDATE orders SET tracking_number = 'TRK123456789' WHERE order_number = 'ORD-2024-001';
UPDATE orders SET tracking_number = 'TRK987654321' WHERE order_number = 'ORD-2024-002';

-- Update estimated delivery dates
UPDATE orders SET estimated_delivery = NOW() + INTERVAL '2 days' WHERE status IN ('SHIPPED', 'OUT_FOR_DELIVERY');
UPDATE orders SET estimated_delivery = NOW() - INTERVAL '1 day' WHERE status = 'DELIVERED';
