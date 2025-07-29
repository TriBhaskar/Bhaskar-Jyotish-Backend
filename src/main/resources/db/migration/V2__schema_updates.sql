-- remove birth_place column and add city, state, country, and postal_code columns

ALTER TABLE birth_details
    DROP COLUMN birth_place,
    ADD COLUMN city VARCHAR(100) NOT NULL,
    ADD COLUMN state VARCHAR(100) NOT NULL,
    ADD COLUMN country VARCHAR(100) NOT NULL,
    ADD COLUMN postal_code VARCHAR(20) NOT NULL;


ALTER TABLE bjyotish_users
    DROP COLUMN date_of_birth;