CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       username VARCHAR(255) UNIQUE,
                       password VARCHAR(255),
                       birth_date DATE,
                       email VARCHAR(255) UNIQUE,
                       role VARCHAR(255),
                       connection_id BIGINT
);

CREATE TABLE connection (
                            id BIGSERIAL PRIMARY KEY,
                            user1_id BIGINT NOT NULL,
                            user2_id BIGINT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            status VARCHAR(255),
                            CONSTRAINT fk_connection_user1 FOREIGN KEY (user1_id) REFERENCES users(id),
                            CONSTRAINT fk_connection_user2 FOREIGN KEY (user2_id) REFERENCES users(id)
);

-- Add the foreign key constraint after both tables exist
ALTER TABLE users
    ADD CONSTRAINT fk_user_connection FOREIGN KEY (connection_id) REFERENCES connection(id);


CREATE TABLE important_date (
                                id BIGSERIAL PRIMARY KEY,
                                title VARCHAR(255),
                                note TEXT,
                                date DATE,
                                type VARCHAR(255),
                                should_be_notified BOOLEAN,
                                connection_id BIGINT,
                                CONSTRAINT fk_date_connection FOREIGN KEY (connection_id) REFERENCES connection(id)
);

CREATE TABLE preference (
                            id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT,
                            category VARCHAR(255),
                            value TEXT,
                            CONSTRAINT fk_preference_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE wishlist_item (
                               id BIGSERIAL PRIMARY KEY,
                               title VARCHAR(255),
                               description TEXT,
                               fulfilled BOOLEAN DEFAULT FALSE,
                               category VARCHAR(255),
                               user_id BIGINT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES users(id)
);
