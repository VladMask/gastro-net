--liquibase formatted sql

alter table payments
    add column stripe_payment_intent_id varchar(255);

-- rollback alter table payments
--     drop column stripe_payment_intent_id;