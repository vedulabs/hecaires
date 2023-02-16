CREATE DATABASE hecaires;
CREATE USER 'hecaires'@'localhost' IDENTIFIED BY '#Q1w2e3r4t5y6#';
GRANT ALL PRIVILEGES ON hecaires.* TO 'hecaires'@'localhost';

--INSERT INTO roles(name) VALUES('ROLE_USER');
--INSERT INTO roles(name) VALUES('ROLE_MAINTAINER');
--INSERT INTO roles(name) VALUES('ROLE_ADMIN');