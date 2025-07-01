package com.tetris.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean isGuest;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isGuest = false;
    }

    public User(String username) {
        this.username = username;
        this.isGuest = true;
    }

    // Constructeur pour les utilisateurs chargés depuis la DB
    public User(int id, String username, String password, String email, boolean isGuest) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isGuest = isGuest;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public boolean isGuest() { return isGuest; }
    public void setGuest(boolean guest) { isGuest = guest; }
} 