-- Initial data
INSERT INTO USERS (userName, userPassword, userType)
SELECT 'ADMIN', 'admin123', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM USERS WHERE userName = 'ADMIN');
