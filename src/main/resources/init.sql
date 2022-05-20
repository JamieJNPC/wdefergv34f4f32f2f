CREATE DATABASE sensorapi;
USE sensorapi;
CREATE TABLE sensor (
                        id INT(6) PRIMARY KEY,
                        city VARCHAR(50) NOT NULL,
                        country VARCHAR(50) NOT NULL
);
CREATE USER 'sensorapi'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON sensorapi.sensor TO 'sensorapi'@'localhost';

CREATE DATABASE sensorapitest;
USE sensorapitest;
CREATE TABLE sensor (
                        id INT(6) PRIMARY KEY,
                        city VARCHAR(50) NOT NULL,
                        country VARCHAR(50) NOT NULL
);
CREATE USER 'sensorapitest'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON sensorapitest.sensor TO 'sensorapitest'@'localhost';