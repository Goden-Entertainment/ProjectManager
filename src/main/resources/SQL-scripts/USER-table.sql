CREATE TABLE USER (id INT AUTO_INCREMENT PRIMARY KEY,
                   userName VARCHAR(255),
                   userEmail VARCHAR(255),
                   userPassword VARCHAR(255),
                   userRole ENUM('ADMIN', 'PROJECTMANAGER', 'DEV') NOT NULL DEFAULT 'DEV',
                   devType ENUM('FRONTEND', 'BACKEND', 'FULLSTACK'),
                   workTime INT);


INSERT INTO USER (userName, userPassword, userRole) VALUES ('ADMIN', 'admin123', 'ADMIN');
