CREATE TABLE IF NOT EXISTS beverages
(
    id       VARCHAR(255) PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    settings JSONB        NOT NULL DEFAULT '{}'::JSONB
);

CREATE UNIQUE INDEX ON beverages (upper(name));
