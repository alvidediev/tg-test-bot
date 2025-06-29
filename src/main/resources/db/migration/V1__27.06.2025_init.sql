CREATE TABLE IF NOT EXISTS users
(
    id      SERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL
);

COMMENT ON COLUMN users.id IS 'Уникальный id записей ';
COMMENT ON COLUMN users.chat_id IS 'Уникальный id записей ';

CREATE TABLE IF NOT EXISTS report
(
    id      SERIAL PRIMARY KEY,
    user_id SERIAL REFERENCES users (id),
    name    VARCHAR(100) NOT NULL,
    email   VARCHAR(20)  NOT NULL,
    score   INT          NOT NULL
);

COMMENT ON COLUMN report.id IS 'Уникальный id';
COMMENT ON COLUMN report.user_id IS 'id юзера';
COMMENT ON COLUMN report.name IS 'имя';
COMMENT ON COLUMN report.email IS 'почта';
COMMENT ON COLUMN report.score IS 'оценка';