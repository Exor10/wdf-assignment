CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(60) NOT NULL UNIQUE,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE INDEX idx_users_role ON users(role_id);

CREATE TABLE IF NOT EXISTS currency_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    source_currency VARCHAR(3) NOT NULL,
    target_currency VARCHAR(3) NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    exchange_rate DECIMAL(19,8) NOT NULL,
    converted_amount DECIMAL(19,4) NOT NULL,
    fee DECIMAL(19,4) NOT NULL,
    final_amount DECIMAL(19,4) NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_currency_tx_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_currency_tx_user ON currency_transactions(user_id);
CREATE INDEX idx_currency_tx_created_at ON currency_transactions(created_at);

CREATE TABLE IF NOT EXISTS investments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    investment_type VARCHAR(30) NOT NULL,
    principal_amount DECIMAL(19,2) NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_investment_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_investment_user ON investments(user_id);

CREATE TABLE IF NOT EXISTS investment_projections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    investment_id BIGINT NOT NULL,
    duration VARCHAR(20) NOT NULL,
    min_projection DECIMAL(19,2) NOT NULL,
    max_projection DECIMAL(19,2) NOT NULL,
    min_profit DECIMAL(19,2) NOT NULL,
    max_profit DECIMAL(19,2) NOT NULL,
    min_tax DECIMAL(19,2) NOT NULL,
    max_tax DECIMAL(19,2) NOT NULL,
    total_fees_min DECIMAL(19,2) NOT NULL,
    total_fees_max DECIMAL(19,2) NOT NULL,
    CONSTRAINT fk_projection_investment FOREIGN KEY (investment_id) REFERENCES investments(id)
);

CREATE INDEX idx_projection_investment ON investment_projections(investment_id);

CREATE TABLE IF NOT EXISTS error_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    source VARCHAR(100) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    details VARCHAR(4000) NOT NULL,
    created_at DATETIME NOT NULL
);

CREATE INDEX idx_error_logs_created ON error_logs(created_at);
