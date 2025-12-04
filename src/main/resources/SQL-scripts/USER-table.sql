
-- Basic tabeller.
CREATE TABLE IF NOT EXISTS USER (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(255) UNIQUE NOT NULL,
    userEmail VARCHAR(255),
    userPassword VARCHAR(255),
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
    team_id INT,
    status VARCHAR(255),
    estimatedTime INT,
    actualTime INT,
    priority VARCHAR(255),
    startDate DATE,
    endDate DATE,
    sub_project_id INT NOT NULL,
    FOREIGN KEY (sub_project_id) REFERENCES SUBPROJECT(sub_project_id) ON DELETE CASCADE,
    FOREIGN KEY (team_id) REFERENCES TEAM(team_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS SUBTASK (
    sub_task_id INT AUTO_INCREMENT PRIMARY KEY,
    subTaskName VARCHAR(255),
    subTaskDescription VARCHAR(255),
    subTaskTime INT,
    task_id INT NOT NULL,
    FOREIGN KEY (task_id) REFERENCES TASK (task_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TEAM (
    team_id INT AUTO_INCREMENT PRIMARY KEY,
    teamName VARCHAR(255) NOT NULL,
    teamDescription TEXT
);

-- Junction tabeller.
CREATE TABLE IF NOT EXISTS USER_PROJECT (
    user_id INT NOT NULL,
    project_id INT NOT NULL,
    PRIMARY KEY (user_id, project_id),
    FOREIGN KEY (user_id) REFERENCES USER (user_id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES PROJECT(project_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS USER_TEAM (
    user_id INT NOT NULL,
    team_id INT NOT NULL,
    PRIMARY KEY (user_id, team_id),
    FOREIGN KEY (user_id) REFERENCES USER(user_id) ON DELETE CASCADE,
    FOREIGN KEY (team_id) REFERENCES TEAM(team_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS USER_SUBTASK (
    user_id INT NOT NULL,
    sub_task_id INT NOT NULL,
    PRIMARY KEY (user_id, sub_task_id),
    FOREIGN KEY (user_id) REFERENCES USER(user_id) ON DELETE CASCADE,
    FOREIGN KEY (sub_task_id) REFERENCES SUBTASK(sub_task_id) ON DELETE CASCADE
);