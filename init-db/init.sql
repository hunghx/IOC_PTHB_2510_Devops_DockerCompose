-- =============================================
-- Init script: create databases and users
-- Runs automatically on first PostgreSQL start
-- =============================================

-- Auth Service
CREATE USER auth_user WITH PASSWORD 'auth_pass';
CREATE DATABASE authdb OWNER auth_user;
GRANT ALL PRIVILEGES ON DATABASE authdb TO auth_user;

-- Course Service
CREATE USER course_user WITH PASSWORD 'course_pass';
CREATE DATABASE coursedb OWNER course_user;
GRANT ALL PRIVILEGES ON DATABASE coursedb TO course_user;

-- Enrollment Service
CREATE USER enroll_user WITH PASSWORD 'enroll_pass';
CREATE DATABASE enrolldb OWNER enroll_user;
GRANT ALL PRIVILEGES ON DATABASE enrolldb TO enroll_user;

-- Notification Service
CREATE USER notification_user WITH PASSWORD 'notification_pass';
CREATE DATABASE notificationdb OWNER notification_user;
GRANT ALL PRIVILEGES ON DATABASE notificationdb TO notification_user;
