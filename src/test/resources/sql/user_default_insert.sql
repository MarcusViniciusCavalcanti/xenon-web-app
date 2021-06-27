INSERT INTO access_card (id, account_non_expired, account_non_locked, created_at,
                         credentials_non_expired, enabled, password, updated_at, username)
VALUES (1, true, true, '2019-10-01', true, true,
        '$2a$10$lNU7iTnSBhW7LdUAd/YWuO5v5rnfwd8wMfvMhrwQOFfk7cJyJvsDa', '2019-10-23',
        'beltrano_user@alunos.utfpr.edu.br');
INSERT INTO access_card (id, account_non_expired, account_non_locked, created_at,
                         credentials_non_expired, enabled, password, updated_at, username)
VALUES (153, true, true, '2019-10-02', true, true,
        '$2a$10$SLrYFBh/iwchBsaGPVz2yeSnS86KzpUwWcbvt50Gc5vXdoyRbE2Su', '2019-10-23',
        'beltrano_admin@admin.com');
INSERT INTO access_card (id, account_non_expired, account_non_locked, created_at,
                         credentials_non_expired, enabled, password, updated_at, username)
VALUES (154, true, true, '2019-10-03', true, true,
        '$2a$10$pnRtQY39UnQsnO6uuaEmFuQokyx6DxWPwoN6R0jlXHFJPaCUqnHOy', '2019-10-23',
        'beltrano_operator@operator.com');

INSERT INTO access_card (id, account_non_expired, account_non_locked, created_at,
                         credentials_non_expired, enabled, password, updated_at, username)
VALUES (200, true, true, '2019-10-04', true, true,
        '$2a$10$lNU7iTnSBhW7LdUAd/YWuO5v5rnfwd8wMfvMhrwQOFfk7cJyJvsDa', '2019-10-23',
        'beltrano_with_car@user.com');
INSERT INTO access_card (id, account_non_expired, account_non_locked, created_at,
                         credentials_non_expired, enabled, password, updated_at, username)
VALUES (201, true, true, '2019-10-25', true, true,
        '$2a$10$SLrYFBh/iwchBsaGPVz2yeSnS86KzpUwWcbvt50Gc5vXdoyRbE2Su', '2019-10-23',
        'beltrano_without_car@user.com');

INSERT INTO access_card_has_roles (access_card_id, profile_id)
VALUES (1, 1);

INSERT INTO access_card_has_roles (access_card_id, profile_id)
VALUES (153, 1);
INSERT INTO access_card_has_roles (access_card_id, profile_id)
VALUES (153, 2);
INSERT INTO access_card_has_roles (access_card_id, profile_id)
VALUES (153, 3);

INSERT INTO access_card_has_roles (access_card_id, profile_id)
VALUES (154, 1);
INSERT INTO access_card_has_roles (access_card_id, profile_id)
VALUES (154, 3);
INSERT INTO access_card_has_roles (access_card_id, profile_id)
VALUES (200, 1);
INSERT INTO access_card_has_roles (access_card_id, profile_id)
VALUES (201, 1);

INSERT INTO users (authorised_access, created_at, name, number_access, type, updated_at,
                   access_card_id)
VALUES (true, '2019-10-01', 'Beltrano', 1693228998, 'STUDENTS', '2019-10-23', 1);
INSERT INTO users (authorised_access, created_at, name, number_access, type, updated_at,
                   access_card_id)
VALUES (true, '2019-10-02', 'Beltrano Admin', 144412489, 'SERVICE', '2019-10-23', 153);
INSERT INTO users (authorised_access, created_at, name, number_access, type, updated_at,
                   access_card_id)
VALUES (true, '2019-10-03', 'Beltrano Operador', 99101783, 'SERVICE', '2019-10-23', 154);

INSERT INTO users (authorised_access, created_at, name, number_access, type, updated_at,
                   access_card_id, status_registry, avatar)
VALUES (true, '2019-10-23', 'Beltrano com carro', 1693228998, 'STUDENTS', '2019-10-23', 200, 100,
        'src/test/resources/test-file/avatar/200.png');
INSERT INTO users (authorised_access, created_at, name, number_access, type, updated_at,
                   access_card_id, status_registry)
VALUES (false, '2019-10-23', 'Beltrano sem carro', 144412489, 'STUDENTS', '2019-10-23', 201, 40);

INSERT INTO cars (id, created_at, last_access, document, model, plate, updated_at)
VALUES (200, '2019-10-23', '2019-10-04', 'src/test/resources/test-file/doc/200.jpg', 'Gol',
        'ABC9801', '2019-10-23');