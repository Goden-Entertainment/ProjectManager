-- Initial data
INSERT INTO USER (userName, userPassword, userType)
SELECT 'ADMIN', 'admin123', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM USER WHERE userName = 'ADMIN');
