
CREATE TABLE IF NOT EXISTS PROJECT (
    project_id int AUTO_INCREMENT PRIMARY KEY,
    projectName varchar(255) UNIQUE NOT NULL,
    projectDescription varchar (255) NOT NULL,
    status varchar(255) NOT NULL,
    priority varchar(255) NOT NULL,
    estimatedTime int,
    actualTime int,
    startDate date,
    endDate date
);

CREATE TABLE IF NOT EXISTS SUBPROJECT (
    sub_project_id INT AUTO_INCREMENT PRIMARY KEY,
    subProjectName VARCHAR(255) NOT NULL,
    subProjectDescription TEXT,
    team VARCHAR(255),
    status VARCHAR(255),
    estimatedTime INT,
    actualTime INT,
    startDate DATE,
    endDate DATE,
    project_id INT NOT NULL,
    FOREIGN KEY (project_id) REFERENCES PROJECT(project_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TASK (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    taskName VARCHAR(255) NOT NULL,
    taskDescription TEXT,
    status VARCHAR(255),
    estimatedTime INT,
    actualTime INT,
    priority VARCHAR(255),
    startDate DATE,
    endDate DATE,
    sub_project_id INT NOT NULL,
    FOREIGN KEY (sub_project_id) REFERENCES SUBPROJECT(sub_project_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS SUBTASK (
    sub_task_id INT AUTO_INCREMENT PRIMARY KEY,
    subTaskName VARCHAR(255),
    subTaskDescription TEXT,
    status VARCHAR(255),
    estimatedTime INT,
    actualTime INT,
    priority VARCHAR(255),
    startDate DATE,
    endDate DATE,
    task_id INT NOT NULL,
    FOREIGN KEY (task_id) REFERENCES TASK (task_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS TEAM (
    team_id INT AUTO_INCREMENT PRIMARY KEY,
    teamName VARCHAR(255) NOT NULL,
    teamDescription TEXT,
    project_id INT,
    sub_project_id INT,
    task_id INT,
    FOREIGN KEY (project_id) REFERENCES PROJECT(project_id) ON DELETE SET NULL,
    FOREIGN KEY (sub_project_id) REFERENCES SUBPROJECT(sub_project_id) ON DELETE SET NULL,
    FOREIGN KEY (task_id) REFERENCES TASK(task_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS USERS (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(255) UNIQUE NOT NULL,
    userEmail VARCHAR(255) UNIQUE,
    userPassword VARCHAR(255) NOT NULL,
    userType VARCHAR(255) NOT NULL,
    devType VARCHAR(255),
    workTime INT,
    team_id INT,
    FOREIGN KEY (team_id) REFERENCES TEAM(team_id) ON DELETE SET NULL
);

-- Junction tabeller.
CREATE TABLE IF NOT EXISTS USERS_PROJECT (
    user_id INT NOT NULL,
    project_id INT NOT NULL,
    PRIMARY KEY (user_id, project_id),
    FOREIGN KEY (user_id) REFERENCES USERS (user_id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES PROJECT(project_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS USERS_SUBTASK (
    user_id INT NOT NULL,
    sub_task_id INT NOT NULL,
    PRIMARY KEY (user_id, sub_task_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    FOREIGN KEY (sub_task_id) REFERENCES SUBTASK(sub_task_id) ON DELETE CASCADE
    );

--
-- INSERT INTO USERS (userName, userPassword, userType)
-- SELECT 'ADMIN', 'admin123', 'ADMIN'
--     WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'ADMIN');
--
-- INSERT INTO USERS (userName, userPassword, userType)
-- SELECT 'PM', '123', 'PROJECTMANAGER'
--     WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'PM');
--
-- -- Insert 10 developers with different specializations and work hours
-- INSERT INTO USERS (userName, userPassword, userEmail, userType, devType, workTime)
-- SELECT 'DEV1', '123', 'dev1@test.com', 'DEV', 'FULLSTACK', 40
--     WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'DEV1');
--
-- INSERT INTO USERS (userName, userPassword, userEmail, userType, devType, workTime)
-- SELECT 'DEV2', '123', 'dev2@test.com', 'DEV', 'FRONTEND', 35
--     WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'DEV2');
--
-- INSERT INTO USERS (userName, userPassword, userEmail, userType, devType, workTime)
-- SELECT 'DEV3', '123', 'dev3@test.com', 'DEV', 'BACKEND', 38
--     WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'DEV3');
--
-- INSERT INTO USERS (userName, userPassword, userEmail, userType, devType, workTime)
-- SELECT 'DEV4', '123', 'dev4@test.com', 'DEV', 'FULLSTACK', 42
--     WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'DEV4');
--
-- INSERT INTO USERS (userName, userPassword, userEmail, userType, devType, workTime)
-- SELECT 'DEV5', '123', 'dev5@test.com', 'DEV', 'FRONTEND', 30
--     WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'DEV5');
--
-- INSERT INTO USERS (userName, userPassword, userEmail, userType, devType, workTime)
-- SELECT 'DEV6', '123', 'dev6@test.com', 'DEV', 'BACKEND', 37
--     WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'DEV6');
--
-- INSERT INTO USERS (userName, userPassword, userEmail, userType, devType, workTime)
-- SELECT 'DEV7', '123', 'dev7@test.com', 'DEV', 'FULLSTACK', 40
--     WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'DEV7');
--
-- INSERT INTO USERS (userName, userPassword, userEmail, userType, devType, workTime)
-- SELECT 'DEV8', '123', 'dev8@test.com', 'DEV', 'FRONTEND', 32
--     WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'DEV8');
--
-- INSERT INTO USERS (userName, userPassword, userEmail, userType, devType, workTime)
-- SELECT 'DEV9', '123', 'dev9@test.com', 'DEV', 'BACKEND', 36
--     WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'DEV9');
--
-- INSERT INTO USERS (userName, userPassword, userEmail, userType, devType, workTime)
-- SELECT 'DEV10', '123', 'dev10@test.com', 'DEV', 'FULLSTACK', 45
--     WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'DEV10');