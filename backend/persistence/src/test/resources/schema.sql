CREATE TABLE users(
  id BIGSERIAL PRIMARY KEY,
  username varchar(100) NOT NULL ,
  email varchar(100) NOT NULL UNIQUE ,
  password varchar(100) NOT NULL,
  phone_num varchar(10)
);

INSERT INTO users values(1,'sangeeta','sang@gmail.com','1234','1234567890');

CREATE TABLE admin(
    id INT PRIMARY KEY,
    email varchar(100) NOT NULL,
    password varchar(100) NOT NULL
   );

INSERT INTO admin values(1,'shivangi@gmail.com','1234');

CREATE TABLE knolxSession(
    id BIGSERIAL PRIMARY KEY,
    presentor varchar(100) NOT NULL,
    topic varchar(100),
    session_id INT,
    rating INT,
    scheduled_date DATE NOT NULL
);

INSERT INTO knolxSession values(1,'Geetika','xyz',1,4,'2017-12-21');

CREATE TABLE video_store(
    id BIGSERIAL PRIMARY KEY,
    presentor varchar(100) NOT NULL,
    topic varchar(100) NOT NULL,
    video_url varchar(100) NOT NULL,
    rating INT NOT NULL
    );

INSERT INTO video_store values(1,'Sangeeta','xyz','Some_url',5);

-------------------------------7/07/2017-------------------------------------------------

CREATE TABLE userSessions(
id BIGSERIAL PRIMARY KEY ,
email varchar(100) NOT NULL UNIQUE ,
accessToken varchar(100) NOT NULL
);

INSERT INTO userSessions VALUES(1, 'test@test.com', 'accessToken')

-- ALTER TABLE video_store RENAME To videoStore ;

