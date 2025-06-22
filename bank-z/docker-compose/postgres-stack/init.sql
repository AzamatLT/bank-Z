CREATE TABLE IF NOT EXISTS clients ( id BIGSERIAL PRIMARY KEY, inn VARCHAR(255) UNIQUE, last_name VARCHAR(255) , first_name VARCHAR(255) , middle_name VARCHAR(255) );

CREATE TABLE IF NOT EXISTS client_history ( id BIGSERIAL PRIMARY KEY, last_name VARCHAR(255) , first_name VARCHAR(255) , middle_name VARCHAR(255), branch_code VARCHAR(255) , inn VARCHAR(255) , operation_date TIMESTAMP , id_operation INT );

CREATE TABLE IF NOT EXISTS settings ( id BIGSERIAL PRIMARY KEY, settings VARCHAR(255), value VARCHAR(255) );

CREATE TABLE IF NOT EXISTS branchs ( id BIGSERIAL PRIMARY KEY, branchs VARCHAR(255), addres_branchs VARCHAR(255) );

CREATE TABLE IF NOT EXISTS currency ( id BIGSERIAL PRIMARY KEY, currency VARCHAR(255), currency_russia VARCHAR(255) );

CREATE TABLE IF NOT EXISTS dictionary_fin_operations ( id BIGSERIAL PRIMARY KEY, operations VARCHAR(255) , operations_russia VARCHAR(255) );

CREATE TABLE IF NOT EXISTS dictionary_operations ( id BIGSERIAL PRIMARY KEY, operations VARCHAR(255) , operations_russia VARCHAR(255) );

CREATE TABLE IF NOT EXISTS cards ( id BIGSERIAL PRIMARY KEY, card_number VARCHAR(255) , date_burn TIMESTAMP , status boolean );

--CREATE TABLE IF NOT EXISTS accounts ( id BIGSERIAL PRIMARY KEY, accounts VARCHAR(255) , accounts_date TIMESTAMP , cards VARCHAR(255) , inn VARCHAR(255) , id_branchs int , id_currency int );

CREATE TABLE accounts ( id BIGSERIAL PRIMARY KEY, account_number VARCHAR(255) , accounts_date TIMESTAMP , card_number VARCHAR(255) , inn VARCHAR(255) , id_branchs INTEGER , id_currency INTEGER );