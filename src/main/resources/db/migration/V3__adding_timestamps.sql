ALTER TABLE users
    ADD COLUMN created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN last_active_at   TIMESTAMP WITH TIME ZONE,
    ADD COLUMN scheduled_deletion_date TIMESTAMP WITH TIME ZONE;

ALTER TABLE connection
ADD COLUMN last_update_at TIMESTAMP WITH TIME ZONE;

ALTER TABLE important_date
ADD COLUMN created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN last_update_at TIMESTAMP WITH TIME ZONE;

ALTER TABLE preference
    ADD COLUMN created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN last_update_at TIMESTAMP WITH TIME ZONE;

ALTER TABLE wishlist_item
    ADD COLUMN last_update_at TIMESTAMP WITH TIME ZONE;

CREATE TABLE user_detail(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    birth_date DATE,
    connection_id BIGINT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update_at TIMESTAMP WITH TIME ZONE,
    scheduled_deletion_date TIMESTAMP WITH TIME ZONE
);

ALTER TABLE users
ADD COLUMN user_details_id BIGINT;


ALTER TABLE users
    ADD CONSTRAINT fk_user_details
        FOREIGN KEY (user_details_id)
            REFERENCES user_detail (id);

INSERT INTO user_detail (first_name, last_name, birth_date, connection_id)
SELECT first_name, last_name, birth_date, connection_id
FROM users;

UPDATE users u
SET user_details_id = ud.id
FROM user_detail ud
WHERE u.first_name = ud.first_name
  AND u.last_name = ud.last_name;

ALTER TABLE users
    DROP COLUMN first_name,
    DROP COLUMN last_name,
    DROP COLUMN birth_date,
    DROP COLUMN connection_id;

CREATE TABLE consent_types(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    is_required BOOLEAN NOT NULL DEFAULT FALSE,
    version TIMESTAMP NOT NULL
);

CREATE TABLE user_consents(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    consent_type_id BIGINT NOT NULL REFERENCES consent_types(id),
    is_Accepted BOOLEAN NOT NULL,
    consented_at TIMESTAMP NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(512),
    UNIQUE (user_id, consent_type_id)
);

CREATE TABLE data_access_logs(
    id BIGSERIAL PRIMARY KEY,
    action VARCHAR(32) NOT NULL,
    entity_type VARCHAR(64) NOT NULL,
    entity_id BIGINT NOT NULL,
    user_id BIGINT,
    ip_address VARCHAR(45),
    timestamp TIMESTAMP NOT NULL DEFAULT now(),
    details TEXT
);

INSERT INTO consent_types (name, description, is_required, version)
VALUES
    ('terms_of_service', 'Terms of Service Agreement', true, NOW()),
    ('privacy_policy', 'Privacy Policy Agreement', true, NOW()),
    ('marketing_emails', 'Receive marketing emails and updates', false, NOW());
