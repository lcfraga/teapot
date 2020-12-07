CREATE TABLE IF NOT EXISTS beverages
(
    id       VARCHAR(255) PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    settings JSONB        NOT NULL DEFAULT '{}'::JSONB
);

CREATE UNIQUE INDEX ON beverages (upper(name));


CREATE TABLE IF NOT EXISTS orders
(
    id         VARCHAR(255)                PRIMARY KEY,
    beverage   VARCHAR(255)                NOT NULL,
    settings   JSONB                       NOT NULL DEFAULT '{}'::JSONB,
    username   VARCHAR(255)                NOT NULL,
    size       VARCHAR(3)                  NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    served     BOOLEAN                     NOT NULL DEFAULT FALSE,
    served_at  TIMESTAMP WITHOUT TIME ZONE     NULL
);
