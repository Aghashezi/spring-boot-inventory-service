CREATE TABLE dealers (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    subscription_type VARCHAR(16) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE vehicles (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL,
    dealer_id UUID NOT NULL REFERENCES dealers(id),
    model VARCHAR(255) NOT NULL,
    price NUMERIC(19,2) NOT NULL,
    status VARCHAR(16) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
