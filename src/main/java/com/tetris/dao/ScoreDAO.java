package com.tetris.dao;

import com.tetris.model.Score;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {
    private DatabaseConnection dbConnection;
    
    public ScoreDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public Score saveScore(Score score) throws SQLException {
        String sql = "INSERT INTO scores (user_id, score, level, lines_cleared, date_time) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, score.getUserId());
            pstmt.setInt(2, score.getScore());
            pstmt.setInt(3, score.getLevel());
            pstmt.setInt(4, score.getLinesCleared());
            pstmt.setTimestamp(5, Timestamp.valueOf(score.getDateTime()));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Saving score failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    score.setId(generatedKeys.getInt(1));
                    return score;
                } else {
                    throw new SQLException("Saving score failed, no ID obtained.");
                }
            }
        }
    }
    
    public List<Score> getTopScores(int limit) throws SQLException {
        String sql = "SELECT s.*, u.username FROM scores s " +
                    "JOIN users u ON s.user_id = u.id " +
                    "ORDER BY s.score DESC LIMIT ?";
        
        List<Score> scores = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Score score = new Score(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("score"),
                        rs.getInt("level"),
                        rs.getInt("lines_cleared"),
                        rs.getTimestamp("date_time").toLocalDateTime()
                    );
                    scores.add(score);
                }
            }
        }
        return scores;
    }
    
    public List<Score> getScoresByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM scores WHERE user_id = ? ORDER BY score DESC";
        
        List<Score> scores = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Score score = new Score(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("score"),
                        rs.getInt("level"),
                        rs.getInt("lines_cleared"),
                        rs.getTimestamp("date_time").toLocalDateTime()
                    );
                    scores.add(score);
                }
            }
        }
        return scores;
    }
    
    public Score getBestScoreByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM scores WHERE user_id = ? ORDER BY score DESC LIMIT 1";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Score(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("score"),
                        rs.getInt("level"),
                        rs.getInt("lines_cleared"),
                        rs.getTimestamp("date_time").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }
} 