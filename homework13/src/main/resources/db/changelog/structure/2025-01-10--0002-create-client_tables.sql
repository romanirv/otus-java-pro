CREATE TABLE clients (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(110) NOT NULL,
    address_id UUID NOT NULL REFERENCES addresses(id)
);
