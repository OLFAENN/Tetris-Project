package com.tetris.dao;

import com.tetris.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DatabaseConnection dbConnection;
    private boolean isDatabaseAvailable = true;
    
    public UserDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public User createUser(String username, String password, String email) throws SQLException {
        if (!isDatabaseAvailable) {
            // Mode hors ligne : créer un utilisateur temporaire
            return new User(username, password, email);
        }
        
        String sql = "INSERT INTO users (username, password, email, is_guest) VALUES (?, ?, ?, false)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); // TODO: Hasher le mot de passe
            pstmt.setString(3, email);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new User(generatedKeys.getInt(1), username, password, email, false);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            isDatabaseAvailable = false;
            // Mode hors ligne : créer un utilisateur temporaire
            return new User(username, password, email);
        }
    }
    
    public User createGuestUser(String username) throws SQLException {
        if (!isDatabaseAvailable) {
            // Mode hors ligne : créer un utilisateur invité temporaire
            return new User(username);
        }
        
        String sql = "INSERT INTO users (username, password, email, is_guest) VALUES (?, '', '', true)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, username);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating guest user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new User(generatedKeys.getInt(1), username, "", "", true);
                } else {
                    throw new SQLException("Creating guest user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            isDatabaseAvailable = false;
            // Mode hors ligne : créer un utilisateur invité temporaire
            return new User(username);
        }
    }
    
    public User authenticateUser(String username, String password) throws SQLException {
        if (!isDatabaseAvailable) {
            // Mode hors ligne : authentification simulée
            return new User(username, password, "");
        }
        
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND is_guest = false";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); // TODO: Vérifier le hash du mot de passe
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getBoolean("is_guest")
                    );
                }
            }
        } catch (SQLException e) {
            isDatabaseAvailable = false;
            // Mode hors ligne : authentification simulée
            return new User(username, password, "");
        }
        return null;
    }
    
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getBoolean("is_guest")
                    );
                }
            }
        }
        return null;
    }
    
    public boolean usernameExists(String username) throws SQLException {
        if (!isDatabaseAvailable) {
            return false; // En mode hors ligne, on suppose que le nom n'existe pas
        }
        
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            isDatabaseAvailable = false;
            return false;
        }
        return false;
    }
} 