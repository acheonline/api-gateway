DROP TABLE IF EXISTS news.news;
CREATE SCHEMA IF NOT EXISTS news;
CREATE TABLE IF NOT EXISTS news.news
(
    id        UUID PRIMARY KEY                NOT NULL,
    provider  TEXT                            NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE UNIQUE NOT NULL,
    payload   TEXT UNIQUE                     NOT NULL
);