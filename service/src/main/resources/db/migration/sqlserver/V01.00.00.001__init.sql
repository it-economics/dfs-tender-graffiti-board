CREATE TABLE message
(
    id         BIGINT IDENTITY (1,1) PRIMARY KEY,
    author     VARCHAR(100),
    message    VARCHAR(300) NOT NULL,
    created_at DATETIME2(0) NOT NULL
)