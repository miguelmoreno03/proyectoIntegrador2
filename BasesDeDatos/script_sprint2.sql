DROP DATABASE IF EXISTS digitalmoney;
CREATE DATABASE digitalmoney;
USE digitalmoney;

CREATE TABLE users (
id INT AUTO_INCREMENT NOT NULL,
first_name VARCHAR(250) ,
last_name  VARCHAR(250) ,
dni VARCHAR(250),
email VARCHAR(250) ,
phone  VARCHAR(250),
password TEXT(1000) NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE accounts (
id INT AUTO_INCREMENT NOT NULL,
alias VARCHAR(255) NOT NULL,
cvu VARCHAR(250) UNIQUE,
balance DOUBLE,
user_id INT UNIQUE,
PRIMARY KEY (id),
FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transactions (
id INT AUTO_INCREMENT NOT NULL,
account_id INT, 
date DATETIME,
description VARCHAR(255),
amount DOUBLE,
destination_cvu  VARCHAR(255),
origin_cvu VARCHAR(255),
type VARCHAR(255),
PRIMARY KEY (id),
FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE TABLE cards (
id INT AUTO_INCREMENT NOT NULL,
type VARCHAR(255),
balance DOUBLE,
account_id INT,
card_number VARCHAR(255),
account_holder VARCHAR(255),
expire_date DATETIME,
bank_entity VARCHAR(255),
PRIMARY KEY (id),
FOREIGN KEY (account_id) REFERENCES accounts (id)
);