
-- CREAR PERMISOS
INSERT INTO permission(permission_id, permission) VALUES (1, 'CREATE');
INSERT INTO permission(permission_id, permission) VALUES (2, 'READ');
INSERT INTO permission(permission_id, permission) VALUES (3, 'UPDATE');
INSERT INTO permission(permission_id, permission) VALUES (4, 'DELETE');

-- CREAR ROLES
INSERT INTO role(role_id, role) VALUES (1, 'USER');
INSERT INTO role(role_id, role) VALUES (2, 'ADMIN');

-- ASIGNAR PERMISOS A ROLE: USER
INSERT INTO role_permission(role_id, permission_id) VALUES (1, 2);

-- ASIGNAR PERMISOS A ROLE: ADMIN
INSERT INTO role_permission(role_id, permission_id) VALUES (2, 1);
INSERT INTO role_permission(role_id, permission_id) VALUES (2, 2);
INSERT INTO role_permission(role_id, permission_id) VALUES (2, 3);
INSERT INTO role_permission(role_id, permission_id) VALUES (2, 4);

-- CREAR SUPER-USUARIO
INSERT INTO user(user_id, first_name, last_name, username, password, is_account_no_expired, is_account_no_locked, is_credentials_no_expired, is_enabled)
VALUES (1, 'Felipe', 'Urtubia', 'fe.urtubia@gmail.com', '12345', true, true, true, true);

-- ASIGNAR ROLE A SUPER-USUARIO
INSERT INTO user_role(user_id, role_id) VALUES (1, 2);



