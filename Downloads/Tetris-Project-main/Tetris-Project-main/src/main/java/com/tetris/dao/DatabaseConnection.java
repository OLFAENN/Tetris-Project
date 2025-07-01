package com.tetris.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Configuration pour base de données distante
    // Remplacez par votre IP publique ou nom de domaine
    private static final String HOST = "10.21.82.75"; // Votre IP locale
    private static final String PORT = "3306";
    private static final String DATABASE = "tetris_db";
    private static final String USERNAME = "tetris_user"; // Utilisateur créé pour les connexions distantes
    private static final String PASSWORD = "votre_mot_de_passe_securise";
    
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + 
                                     "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connexion à la base de données distante établie");
            } catch (SQLException e) {
                System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion à la base de données fermée");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Créer la table users
            String createUsersTable = 
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "email VARCHAR(100)," +
                "is_guest BOOLEAN DEFAULT FALSE," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
            
            // Créer la table scores
            String createScoresTable = 
                "CREATE TABLE IF NOT EXISTS scores (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT," +
                "score INT NOT NULL," +
                "level INT NOT NULL," +
                "lines_cleared INT NOT NULL," +
                "date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";
            
            conn.createStatement().execute(createUsersTable);
            conn.createStatement().execute(createScoresTable);
            
            System.out.println("Base de données distante initialisée avec succès");
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    // Méthode pour tester la connexion
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Test de connexion échoué: " + e.getMessage());
            return false;
        }
    }
} 