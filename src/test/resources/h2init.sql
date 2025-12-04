CREATE TABLE IF NOT EXISTS `USER` (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(255) UNIQUE NOT NULL,
    userEmail VARCHAR(255) UNIQUE,
    userPassword VARCHAR(255) NOT NULL,
    userType VARCHAR(255) NOT NULL,
    devType VARCHAR(255),
    workTime INT
);

INSERT INTO USER (userName, userPassword, userType)
SELECT 'ADMIN', 'admin123', 'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'ADMIN');

INSERT INTO USER (userName, userPassword, userType)
SELECT 'PM', '123', 'PROJECTMANAGER'
    WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'PM');

-- Insert 10 developers with different specializations and work hours
INSERT INTO USER (userName, userPassword, userEmail, userType, devType, workTime)
SELECT 'DEV1', '123', 'dev1@test.com', 'DEV', 'FULLSTACK', 40
    WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'DEV1');

INSERT INTO USER (userName, userPassword, userEmail, userType, devType, workTime)
SELECT 'DEV2', '123', 'dev2@test.com', 'DEV', 'FRONTEND', 35
    WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'DEV2');

INSERT INTO USER (userName, userPassword, userEmail, userType, devType, workTime)
SELECT 'DEV3', '123', 'dev3@test.com', 'DEV', 'BACKEND', 38
    WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'DEV3');

INSERT INTO USER (userName, userPassword, userEmail, userType, devType, workTime)
SELECT 'DEV4', '123', 'dev4@test.com', 'DEV', 'FULLSTACK', 42
    WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'DEV4');

INSERT INTO USER (userName, userPassword, userEmail, userType, devType, workTime)
SELECT 'DEV5', '123', 'dev5@test.com', 'DEV', 'FRONTEND', 30
    WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'DEV5');

INSERT INTO USER (userName, userPassword, userEmail, userType, devType, workTime)
SELECT 'DEV6', '123', 'dev6@test.com', 'DEV', 'BACKEND', 37
    WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'DEV6');

INSERT INTO USER (userName, userPassword, userEmail, userType, devType, workTime)
SELECT 'DEV7', '123', 'dev7@test.com', 'DEV', 'FULLSTACK', 40
    WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'DEV7');

INSERT INTO USER (userName, userPassword, userEmail, userType, devType, workTime)
SELECT 'DEV8', '123', 'dev8@test.com', 'DEV', 'FRONTEND', 32
    WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'DEV8');

INSERT INTO USER (userName, userPassword, userEmail, userType, devType, workTime)
SELECT 'DEV9', '123', 'dev9@test.com', 'DEV', 'BACKEND', 36
    WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'DEV9');

INSERT INTO USER (userName, userPassword, userEmail, userType, devType, workTime)
SELECT 'DEV10', '123', 'dev10@test.com', 'DEV', 'FULLSTACK', 45
    WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'DEV10');