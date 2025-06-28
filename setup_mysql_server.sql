-- Script de configuration MySQL pour connexions distantes
-- À exécuter sur votre serveur MySQL

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS tetris_db;
USE tetris_db;

-- Créer un utilisateur qui peut se connecter de n'importe où
CREATE USER IF NOT EXISTS 'tetris_user'@'%' IDENTIFIED BY 'votre_mot_de_passe_securise';

-- Donner tous les privilèges sur la base tetris_db
GRANT ALL PRIVILEGES ON tetris_db.* TO 'tetris_user'@'%';

-- Appliquer les changements
FLUSH PRIVILEGES;

-- Créer la table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    is_guest BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Créer la table des scores
CREATE TABLE IF NOT EXISTS scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    score INT NOT NULL,
    level INT NOT NULL,
    lines_cleared INT NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Créer quelques utilisateurs de test
INSERT IGNORE INTO users (username, password, email) VALUES 
('admin', 'admin123', 'admin@tetris.com'),
('joueur1', 'joueur123', 'joueur1@email.com'),
('test_user', 'test123', 'test@example.com');

-- Créer quelques scores de test
INSERT IGNORE INTO scores (user_id, score, level, lines_cleared) VALUES 
(1, 2500, 5, 25),
(1, 1800, 3, 18),
(2, 3200, 6, 32),
(2, 2100, 4, 21),
(3, 1500, 2, 15);

-- Vérifier que tout fonctionne
SELECT 'Configuration terminée avec succès!' as status;
SHOW TABLES;
SELECT COUNT(*) as nombre_utilisateurs FROM users;
SELECT COUNT(*) as nombre_scores FROM scores; 