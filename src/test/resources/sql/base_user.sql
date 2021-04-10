INSERT INTO access_card (id, account_non_expired, account_non_locked, created_at, credentials_non_expired, enabled, password, updated_at, username) VALUES (1, true, true, '2019-10-23', true, true, '$2a$10$lNU7iTnSBhW7LdUAd/YWuO5v5rnfwd8wMfvMhrwQOFfk7cJyJvsDa', '2019-10-23', 'beltrano_user@udrt.com');
INSERT INTO access_card (id, account_non_expired, account_non_locked, created_at, credentials_non_expired, enabled, password, updated_at, username) VALUES (153, true, true, '2019-10-23', true, true, '$2a$10$SLrYFBh/iwchBsaGPVz2yeSnS86KzpUwWcbvt50Gc5vXdoyRbE2Su', '2019-10-23', 'beltrano_admin@admin.com');
INSERT INTO access_card (id, account_non_expired, account_non_locked, created_at, credentials_non_expired, enabled, password, updated_at, username) VALUES (154, true, true, '2019-10-23', true, true, '$2a$10$pnRtQY39UnQsnO6uuaEmFuQokyx6DxWPwoN6R0jlXHFJPaCUqnHOy', '2019-10-23', 'beltrano_operator@operator.com');

INSERT INTO access_card_has_roles (access_card_id, profile_id) VALUES (1, 1);

INSERT INTO access_card_has_roles (access_card_id, profile_id) VALUES (153, 1);
INSERT INTO access_card_has_roles (access_card_id, profile_id) VALUES (153, 2);
INSERT INTO access_card_has_roles (access_card_id, profile_id) VALUES (153, 3);

INSERT INTO access_card_has_roles (access_card_id, profile_id) VALUES (154, 1);
INSERT INTO access_card_has_roles (access_card_id, profile_id) VALUES (154, 3);

INSERT INTO users (authorised_access, created_at, name, number_access, type, updated_at, access_card_id) VALUES (true, '2019-10-23', 'Beltrano', 1693228998, 'STUDENTS', '2019-10-23', 1);
INSERT INTO users (authorised_access, created_at, name, number_access, type, updated_at, access_card_id) VALUES (true, '2019-10-23', 'Beltrano Admin', 144412489, 'SERVICE', '2019-10-23', 153);
INSERT INTO users (authorised_access, created_at, name, number_access, type, updated_at, access_card_id) VALUES (true, '2019-10-23', 'Beltrano Operador', 99101783, 'SERVICE', '2019-10-23', 154);
