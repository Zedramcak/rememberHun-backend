CREATE TABLE preference_category (
                                     id BIGSERIAL PRIMARY KEY,
                                     name VARCHAR(255) UNIQUE NOT NULL,
                                     description TEXT
);

INSERT INTO preference_category (name) VALUES
('COLOR'), ('BRAND'), ('FLOWER'), ('FOOD'), ('MOVIE'), ('TEAM'), ('OTHER');

ALTER TABLE preference ADD COLUMN category_id BIGINT;

UPDATE preference p
SET category = pc.id
FROM preference_category pc
WHERE p.category = pc.name;

-- Add the foreign key constraint
ALTER TABLE preference
ADD CONSTRAINT fk_preference_category
FOREIGN KEY (category_id) REFERENCES preference_category(id);

ALTER TABLE preference
DROP COLUMN category;

-- Add role table

CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) UNIQUE NOT NULL
);

INSERT INTO role (name) VALUES ('USER'), ('ADMIN');

ALTER TABLE users ADD COLUMN role_id BIGINT;

UPDATE users u
SET role_id = r.id
FROM role r
WHERE u.role = r.name;

ALTER TABLE users
ADD CONSTRAINT fk_role
FOREIGN KEY (role_id) REFERENCES role(id);

ALTER TABLE users
DROP COLUMN role;

-- add connectionStatus table
CREATE TABLE connection_status (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(64) UNIQUE NOT NULL
);

INSERT INTO connection_status (status)
VALUES ('PENDING'), ('CONNECTED'),('DELETED');

ALTER TABLE connection ADD COLUMN connection_status_id BIGINT;

UPDATE connection c
SET connection_status_id = cs.id
FROM connection_status cs
WHERE c.status = cs.status;

ALTER TABLE connection
ADD CONSTRAINT fk_connection_status
FOREIGN KEY (connection_status_id) REFERENCES connection_status(id);

ALTER TABLE connection
DROP COLUMN status;

-- add importantDate table
CREATE TABLE important_date_category(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) UNIQUE NOT NULL
);

INSERT INTO important_date_category (name)
VALUES ('BIRTHDAY'), ('ANNIVERSARY'), ('FIRST_DATE'), ('CUSTOM');

ALTER TABLE important_date ADD COLUMN category_id BIGINT;

UPDATE important_date
SET category_id = idc.id
FROM important_date_category idc
WHERE important_date.type = idc.name;

ALTER TABLE important_date
ADD CONSTRAINT fk_important_date_category
FOREIGN KEY (category_id) REFERENCES important_date_category(id);

ALTER TABLE important_date
DROP COLUMN type;

-- add wishlistCategory table

CREATE TABLE wishlist_category(
      id BIGSERIAL PRIMARY KEY,
      name VARCHAR(64) UNIQUE NOT NULL
);

INSERT INTO wishlist_category (name)
VALUES ('EXPERIENCE'), ('GIFT'), ('ACTIVITY'), ('TRAVEL'), ('OTHER');

ALTER TABLE wishlist_item ADD COLUMN category_id BIGINT;

UPDATE wishlist_item wi
SET category_id = wc.id
FROM wishlist_category wc
WHERE wi.category = wc.name;

ALTER TABLE wishlist_item
ADD CONSTRAINT fk_wishlist_category
FOREIGN KEY (category_id) REFERENCES wishlist_category(id);

ALTER TABLE wishlist_item
DROP COLUMN category;