CREATE TABLE users(
  id BIGSERIAL PRIMARY KEY,
  username varchar(50) NOT NULL ,
  email varchar(50) NOT NULL ,
  password varchar(50) NOT NULL
);

INSERT INTO users values(1,'sangeeta','sang@gmail.com','1234')