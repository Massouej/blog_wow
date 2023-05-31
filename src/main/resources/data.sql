INSERT INTO users (username, password, enabled)
VALUES ('admin', '{bcrypt}$2a$10$f/IQyqCfFzvVwJebCOqoHOBNtFaw.CKvEZeyKkUffT61tNghafy1q', 1);

INSERT INTO authorities (username,authority) VALUES ('admin', 'ROLE_ADMIN');