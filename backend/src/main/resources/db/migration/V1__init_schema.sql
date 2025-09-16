-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(500),
    stock_quantity INTEGER DEFAULT 0,
    category VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Cart items table
CREATE TABLE cart_items (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, product_id)
);

-- Orders table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    payment_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Order items table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id),
    quantity INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

-- Insert sample products
INSERT INTO products (name, description, price, image_url, stock_quantity, category) VALUES
('iPhone 15', 'Latest Apple iPhone with advanced features', 79999.00, 'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=400', 50, 'Electronics'),
('Samsung Galaxy S24', 'Premium Android smartphone', 69999.00, 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400', 30, 'Electronics'),
('MacBook Pro', 'Powerful laptop for professionals', 199999.00, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400', 20, 'Electronics'),
('Nike Air Max', 'Comfortable running shoes', 8999.00, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', 100, 'Fashion'),
('Adidas T-Shirt', 'Premium cotton t-shirt', 1999.00, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400', 200, 'Fashion'),
('Sony Headphones', 'Noise cancelling headphones', 15999.00, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400', 75, 'Electronics');