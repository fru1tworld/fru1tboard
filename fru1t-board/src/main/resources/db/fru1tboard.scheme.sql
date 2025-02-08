CREATE DATABASE fru1t_board;

CREATE TABLE article (
    article_id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(5000) NOT NULL,
    board_id BIGINT NOT NULL,
    writer_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
);

CREATE TABLE comment (
    commnet_id BIGINT PRIMARY KEY,
    article_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    parent_comment_id BIGINT,
    write_id BIGINT NOT NULL,
    deleted BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL
);


CREATE TABLE user (
    user_id BIGINT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    user_name BIGINT NOT NULL UNIQUE,
    user_password VARCHAR(100) NOT NULL
);

CREATE TABLE admin (
    user_id int PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role JSON NOT NULL
);

TABLE comment (
    article_id BIGINT NOT NULL,
);