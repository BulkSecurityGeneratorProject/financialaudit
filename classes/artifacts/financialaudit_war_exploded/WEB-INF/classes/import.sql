INSERT INTO oauth_client_details(client_id, client_secret, scope, authorized_grant_types,web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information) VALUES   ('sampleClientId', 'secret', 'read,write,foo,bar',    'password,authorization_code,refresh_token', null, null, 36000, 36000, null);

INSERT INTO USERS (id, login, password, firstname, lastname, email,activated, data_status, lang_key, create_user, create_date, update_user, update_date, version) VALUES (1,'demo@demo.com','$2a$10$A3tZV9agcdp4VT1wTyeGY.pXdlvX5T0zANITox917cp70jRlVarT2','demo','demosurname','demo@demo.com',true, 'ACTIVE', 'tr', 'system', '2017-01-01', 'system', '2017-01-01', '0');

INSERT INTO authority (name) VALUES ('ROLE_ADMIN');

INSERT INTO authority (name) VALUES ('ROLE_USER');

INSERT INTO user_authority (user_id, authority_name) VALUES ('1', 'ROLE_ADMIN');