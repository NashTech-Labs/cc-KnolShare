
# DB SCRIPT FOR KNOLXSESSION TABLE
--------------------------------------------------------------------------------

CREATE DATABASE knolshare;

\c knolshare

ALTER USER Postgres WITH PASSWORD 'postgres';

CREATE TABLE knolxSession(
    id BIGSERIAL PRIMARY KEY,
    presentor varchar(100) NOT NULL,
    topic varchar(100),
    session_id INT,
    rating INT,
    scheduled_date DATE NOT NULL
);

INSERT INTO knolxSession values(1,'Geetika','xyz',1,4,'2017-12-21');
INSERT INTO knolxSession values(2,'Sangeeta','xyz',2,4,'2017-12-21');
INSERT INTO knolxSession values(3,'Akshay','xyz',1,4,'2017-12-28');
INSERT INTO knolxSession values(4,'Shivangi','xyz',2,4,'2017-12-28');