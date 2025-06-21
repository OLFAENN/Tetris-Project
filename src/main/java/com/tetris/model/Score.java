package com.tetris.model;

import java.time.LocalDateTime;

public class Score {
    private int id;
    private int userId;
    private int score;
    private int level;
    private int linesCleared;
    private LocalDateTime dateTime;

    public Score(int userId, int score, int level, int linesCleared) {
        this.userId = userId;
        this.score = score;
        this.level = level;
        this.linesCleared = linesCleared;
        this.dateTime = LocalDateTime.now();
    }

    // Constructeur pour les scores chargés depuis la DB
    public Score(int id, int userId, int score, int level, int linesCleared, LocalDateTime dateTime) {
        this.id = id;
        this.userId = userId;
        this.score = score;
        this.level = level;
        this.linesCleared = linesCleared;
        this.dateTime = dateTime;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public int getLinesCleared() { return linesCleared; }
    public void setLinesCleared(int linesCleared) { this.linesCleared = linesCleared; }
    
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
} 