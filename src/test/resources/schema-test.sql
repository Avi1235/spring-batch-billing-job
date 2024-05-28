DROP TABLE IF EXISTS people ;

CREATE TABLE people  (
    person_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);