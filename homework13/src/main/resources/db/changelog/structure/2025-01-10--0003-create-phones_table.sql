CREATE TABLE phones (
    id UUID PRIMARY KEY NOT NULL,
    number VARCHAR(20) NOT NULL,
    client_id UUID NOT NULL REFERENCES clients(id)
);
