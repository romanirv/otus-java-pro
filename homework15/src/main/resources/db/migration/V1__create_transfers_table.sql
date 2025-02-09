create table transfers (
    id varchar(36) primary key,
    client_id varchar(10),
    target_client_id varchar(10),
    source_account varchar(20),
    target_account varchar(20),
    amount int,
    message varchar(255)
);

insert into transfers (id, client_id, target_client_id, source_account, target_account, amount, message) values
('bde76ffa-f133-4c23-9bca-03618b2a94b2', '1000000001', '1000000002', '000000000001', '000000000002', 100, 'Тестовый перевод'),
('32ebb2eb-ed35-4baa-b500-b7f6535e4c88', '1000000002', '1000000001', '000000000002', '000000000001', 50, 'Обратный тестовый перевод');