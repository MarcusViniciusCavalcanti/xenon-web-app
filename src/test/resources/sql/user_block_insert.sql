INSERT INTO access_card (id, account_non_expired, account_non_locked, created_at,
                         credentials_non_expired, enabled, password, updated_at, username)
VALUES (155, false, true, '2019-10-28', true, true,
        '$2a$10$lNU7iTnSBhW7LdUAd/YWuO5v5rnfwd8wMfvMhrwQOFfk7cJyJvsDa', '2019-10-23',
        'beltrano_expired_account@user.com');
INSERT INTO access_card (id, account_non_expired, account_non_locked, created_at,
                         credentials_non_expired, enabled, password, updated_at, username)
VALUES (156, true, false, '2019-10-29', true, true,
        '$2a$10$SLrYFBh/iwchBsaGPVz2yeSnS86KzpUwWcbvt50Gc5vXdoyRbE2Su', '2019-10-23',
        'beltrano_locked_account@user.com');
INSERT INTO access_card (id, account_non_expired, account_non_locked, created_at,
                         credentials_non_expired, enabled, password, updated_at, username)
VALUES (157, true, true, '2019-10-30', false, true,
        '$2a$10$pnRtQY39UnQsnO6uuaEmFuQokyx6DxWPwoN6R0jlXHFJPaCUqnHOy', '2019-10-23',
        'beltrano_expired_credentials@user.com');
INSERT INTO access_card (id, account_non_expired, account_non_locked, created_at,
                         credentials_non_expired, enabled, password, updated_at, username)
VALUES (158, true, true, '2019-10-27', true, false,
        '$2a$10$pnRtQY39UnQsnO6uuaEmFuQokyx6DxWPwoN6R0jlXHFJPaCUqnHOy', '2019-10-23',
        'beltrano_disable_account@user.com');

INSERT INTO access_card_has_roles (access_card_id, profile_id)
VALUES (155, 1);
INSERT INTO access_card_has_roles (access_card_id, profile_id)
VALUES (156, 1);
INSERT INTO access_card_has_roles (access_card_id, profile_id)
VALUES (157, 1);
INSERT INTO access_card_has_roles (access_card_id, profile_id)
VALUES (158, 1);

INSERT INTO users (authorised_access, created_at, name, number_access, type, updated_at,
                   access_card_id)
VALUES (true, '2019-10-05', 'Beltrano conta exipirada', 1693228998, 'STUDENTS', '2019-10-23', 155);
INSERT INTO users (authorised_access, created_at, name, number_access, type, updated_at,
                   access_card_id)
VALUES (true, '2019-10-06', 'Beltrano conta bloqueada', 144412489, 'STUDENTS', '2019-10-23', 156);
INSERT INTO users (authorised_access, created_at, name, number_access, type, updated_at,
                   access_card_id)
VALUES (true, '2019-10-07', 'Beltrano credenciais expiradas', 99101783, 'STUDENTS', '2019-10-23',
        157);
INSERT INTO users (authorised_access, created_at, name, number_access, type, updated_at,
                   access_card_id)
VALUES (true, '2019-10-08', 'Beltrano conta desativada', 99101783, 'STUDENTS', '2019-10-23', 158);
