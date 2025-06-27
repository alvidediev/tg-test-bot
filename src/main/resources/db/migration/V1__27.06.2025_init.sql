CREATE TABLE IF NOT EXISTS users
(
    id      SERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL
);

COMMENT ON COLUMN users.id IS 'Уникальный id записей ';

CREATE TABLE IF NOT EXISTS report
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(20)  NOT NULL,
    cout  INT          NOT NULL
);