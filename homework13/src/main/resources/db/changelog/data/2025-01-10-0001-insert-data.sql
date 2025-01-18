DO $$
DECLARE ADDRESS1_UUID UUID := gen_random_uuid();
DECLARE ADDRESS2_UUID UUID := gen_random_uuid();
DECLARE ADDRESS3_UUID UUID := gen_random_uuid();

DECLARE CLIENT1_UUID UUID := gen_random_uuid();

BEGIN
    INSERT INTO addresses(id, street) VALUES
    (ADDRESS1_UUID, 'Moscow, 1-Street, 10'),
    (ADDRESS2_UUID, 'Saint-Petersburg, 100'),
    (ADDRESS3_UUID, 'Kazan, 110');

    INSERT INTO clients(id, name, address_id) VALUES
    (CLIENT1_UUID, 'Client-1', ADDRESS1_UUID);

    INSERT INTO phones(id, number, client_id) VALUES
    (gen_random_uuid(), '+72341123344', CLIENT1_UUID),
    (gen_random_uuid(), '+73224321100', CLIENT1_UUID),
    (gen_random_uuid(), '+72345670980', CLIENT1_UUID);
END $$;
