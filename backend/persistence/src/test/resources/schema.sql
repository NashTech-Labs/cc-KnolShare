CREATE TABLE users(
  id BIGSERIAL PRIMARY KEY,
  username varchar(50) NOT NULL ,
  email varchar(50) NOT NULL ,
  password varchar(50) NOT NULL,
  phone_num varchar(10)
);

INSERT INTO users values(1,'sangeeta','sang@gmail.com','1234','1234567890');

CREATE TABLE admin(
    id INT PRIMARY KEY,
    email varchar(50) NOT NULL,
    password varchar(20) NOT NULL
   );

INSERT INTO admin values(1,'shivangi@gmail.com','1234');

CREATE TABLE knolxSession(
    id BIGSERIAL PRIMARY KEY,
    presentor varchar(50) NOT NULL,
    topic varchar(50),
    session_id INT,
    rating INT,
    scheduledDate DATE NOT NULL
);

INSERT INTO knolxSession values(1,'Geetika','xyz',1,4,'2017-12-21');


