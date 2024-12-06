--liquibase formatted sql
--changeset K.Kormilcev:task1 logicalFilePath:01.000.00/account.sql
CREATE TABLE IF NOT EXISTS account
(
    account_id      BIGSERIAL NOT NULL,
    payment_account VARCHAR   NOT NULL,
    balance         DECIMAL   NOT NULL,
    status          VARCHAR   NOT NULL,
    bic             VARCHAR   NOT NULL,
    currency        VARCHAR   NOT NULL,
    client_id       BIGSERIAL NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (account_id),
    CONSTRAINT uc_payment_account UNIQUE (payment_account),
    CONSTRAINT fk_accounts_on_client
        FOREIGN KEY (client_id) REFERENCES client(client_id)
        ON DELETE NO ACTION
        ON UPDATE CASCADE
);
