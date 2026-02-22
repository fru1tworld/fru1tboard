CREATE DATABASE fru1t_board;

CREATE TABLE article (
    article_id BIGINT PRIMARY KEY NOT NULL,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(5000) NOT NULL,
    board_id BIGINT NOT NULL,
    writer_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL
);

create index idx_board_id_article_id on article(board_id asc, article_id desc);

CREATE TABLE comment (
    comment_id BIGINT PRIMARY KEY NOT NULL,
    article_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    parent_comment_id BIGINT NOT NULL,
    writer_id BIGINT NOT NULL,
    deleted TINYINT(1) NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL
);

create index idx_article_id_comment_id on comment(article_id asc, comment_id desc);



CREATE TABLE user (
    user_id BIGINT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);


CREATE TABLE admin (
    user_id BIGINT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role JSON NOT NULL
);
