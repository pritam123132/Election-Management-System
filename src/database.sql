CREATE DATABASE IF NOT EXISTS election_management;
USE election_management;

CREATE TABLE IF NOT EXISTS users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN','USER') NOT NULL
);

INSERT INTO users(username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('user', 'user123', 'USER')
ON DUPLICATE KEY UPDATE username=username;

CREATE TABLE IF NOT EXISTS candidates(
    id INT PRIMARY KEY AUTO_INCREMENT,
    candidate_name VARCHAR(100) NOT NULL,
    party_name VARCHAR(100) NOT NULL,
    symbol VARCHAR(100) NOT NULL,
    constituency VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    votes INT DEFAULT 0
);
