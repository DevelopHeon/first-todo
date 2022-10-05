CREATE TABLE TODO(
    id INTEGER primary key AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL ,
    content VARCHAR(255) NOT NULL,
    status VARCHAR(1) DEFAULT 'N',
    createDate DATE DEFAULT NOW()
);
INSERT INTO TODO
(title, content, status, createDate)
VALUES ('제목 1', '내용 1', default, default);
INSERT INTO TODO
(title, content, status, createDate)
VALUES ('제목 2', '내용 2', default, default);
INSERT INTO TODO
(title, content, status, createDate)
VALUES ('제목 3', '내용 3', default, default);
INSERT INTO TODO
(title, content, status, createDate)
VALUES ('제목 4', '내용 4', default, default);
INSERT INTO TODO
(title, content, status, createDate)
VALUES ('제목 5', '내용 5', default, default);