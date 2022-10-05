CREATE TABLE TODO(
    id INTEGER primary key AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL ,
    content VARCHAR(255) NOT NULL,
    isComplated VARCHAR(1) DEFAULT 'N',
    createDate DATE DEFAULT NOW()
);