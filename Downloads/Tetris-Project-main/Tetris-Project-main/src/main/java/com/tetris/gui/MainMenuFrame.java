package com.tetris.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuFrame extends JFrame {
    
    public MainMenuFrame() {
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 600);
        setLocationRelativeTo(null);
        
        setupUI();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout());
        
        // Panneau principal avec fond uni
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 245));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Titre principal
        JLabel titleLabel = new JLabel("TETRIS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 0));
        
        // Sous-titre
        JLabel subtitleLabel = new JLabel("Classic Puzzle Game");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        // Panneau des boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Boutons principaux
        ModernButton playButton = new ModernButton("JOUER", new Color(52, 152, 219));
        ModernButton scoresButton = new ModernButton("MEILLEURS SCORES", new Color(46, 204, 113));
        ModernButton settingsButton = new ModernButton("PARAMÈTRES", new Color(155, 89, 182));
        ModernButton quitButton = new ModernButton("QUITTER", new Color(231, 76, 60));
        
        // Actions des boutons
        playButton.addActionListener(e -> openLoginFrame());
        scoresButton.addActionListener(e -> showScores());
        settingsButton.addActionListener(e -> showSettings());
        quitButton.addActionListener(e -> System.exit(0));
        
        // Ajouter les boutons avec espacement
        buttonPanel.add(playButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(scoresButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(settingsButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(quitButton);
        
        // Assembler le panneau principal
        mainPanel.add(titleLabel);
        mainPanel.add(subtitleLabel);
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void openLoginFrame() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }
    
    private void showScores() {
        JOptionPane.showMessageDialog(this, 
            "Fonctionnalité des scores à implémenter", 
            "Meilleurs Scores", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showSettings() {
        JOptionPane.showMessageDialog(this, 
            "Fonctionnalité des paramètres à implémenter", 
            "Paramètres", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Classe pour les boutons modernes
    private static class ModernButton extends JButton {
        private Color hoverColor;
        private Color originalColor;
        
        public ModernButton(String text, Color color) {
            super(text);
            this.originalColor = color;
            this.hoverColor = color.brighter();
            
            setPreferredSize(new Dimension(250, 50));
            setMaximumSize(new Dimension(250, 50));
            setFont(new Font("Arial", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setBackground(color);
            setBorder(BorderFactory.createEmptyBorder());
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(originalColor);
                }
            });
        }
    }
} 