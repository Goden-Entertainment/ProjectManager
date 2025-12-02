CREATE TABLE IF NOT EXISTS USERS (
                                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                                    userName VARCHAR(255) UNIQUE NOT NULL,
    userEmail VARCHAR(255) UNIQUE,
    userPassword VARCHAR(255) NOT NULL,
    userType ENUM('ADMIN', 'PROJECTMANAGER', 'DEV') NOT NULL DEFAULT 'DEV',
    devType ENUM('FRONTEND', 'BACKEND', 'FULLSTACK'),
    workTime INT
    );