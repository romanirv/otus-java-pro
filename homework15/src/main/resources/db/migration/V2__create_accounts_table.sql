create table accounts (
    id varchar(36) primary key,
    number varchar(20),
    client_id varchar(10),
    balance int,
    is_blocked boolean
);

insert into accounts (id, number, client_id, balance, is_blocked) values
('d7713809-d1a2-4c87-b265-1caddf20e5d8', '40817810500000000035', '1000000001', '100', false),
('bb22d69c-3f2e-4620-86e8-7931d4f7b701', '40817810500000000036', '1000000002', '120', false),
('3a3efa7f-5a41-428f-a018-3841cbbd36c4', '40817810500000000037', '1000000003', '110', true);
