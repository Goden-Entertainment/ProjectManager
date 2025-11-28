
-- Basic tabeller.
CREATE TABLE IF NOT EXISTS USER (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(255) UNIQUE NOT NULL,
    userEmail VARCHAR(255) UNIQUE,
    userPassword VARCHAR(255) NOT NULL,
    userType ENUM('ADMIN', 'PROJECTMANAGER', 'DEV') NOT NULL DEFAULT 'DEV',
    devType ENUM('FRONTEND', 'BACKEND', 'FULLSTACK'),
    workTime INT
);

CREATE TABLE IF NOT EXISTS PROJECT (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    projectName VARCHAR(255) NOT NULL,
    projectDescription TEXT,
    status VARCHAR(255),
    priority VARCHAR(255),
    estimatedTime INT,
    actualTime INT,
    startDate DATE,
    endDate DATE
);

CREATE TABLE IF NOT EXISTS SUBPROJECT (
    sub_project_id INT AUTO_INCREMENT PRIMARY KEY,
    subProjectName VARCHAR(255) UNIQUE,
    subProjectDescription VARCHAR(255),
    estimatedTime INT,
    actualTime INT,
    project_id INT NOT NULL,
    FOREIGN KEY (project_id) REFERENCES PROJECT(project_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TASK (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    taskName VARCHAR(255) UNIQUE,
    taskDescription VARCHAR(255),
    taskTime INT,
    sub_project_id INT NOT NULL,
    FOREIGN KEY (sub_project_id) REFERENCES SUBPROJECT (sub_project_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS SUBTASK (
    sub_task_id INT AUTO_INCREMENT PRIMARY KEY,
    subTaskName VARCHAR(255) UNIQUE,
    subTaskDescription VARCHAR(255),
    subTaskTime VARCHAR(255),
    task_id INT NOT NULL,
    FOREIGN KEY (task_id) REFERENCES TASK (task_id) ON DELETE CASCADE
);

-- Junction tabeller.
CREATE TABLE IF NOT EXISTS USER_PROJECT (
    user_id INT NOT NULL,
    project_id INT NOT NULL,
    PRIMARY KEY (user_id, project_id),
    FOREIGN KEY (user_id) REFERENCES USER (user_id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES PROJECT(project_id) ON DELETE CASCADE
);