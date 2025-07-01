package com.tetris.gui;

import com.tetris.dao.ScoreDAO;
import com.tetris.model.Score;
import java.sql.SQLException;


import com.tetris.model.Game;
import com.tetris.model.User;
import com.tetris.utils.ResourceManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {
    private Game game;
    private GamePanel gamePanel;
    private Timer gameTimer;
    private Timer animationTimer;
    private boolean isPaused = false;
    private ResourceManager resourceManager;
    private JButton pauseBtn;
    private User currentUser;

    // Constructeur pour jouer avec un utilisateur
    public GameFrame(User user) {
        this.currentUser = user;
        this.game = new Game(user);
        this.gamePanel = new GamePanel(game);
        this.resourceManager = ResourceManager.getInstance();
        setTitle("Tetris - Jouer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setupUI();
        setupGameTimer();
        setupAnimationTimer();
        setupKeyBindings();
        
        pack();
        setLocationRelativeTo(null);
    }

    // Constructeur existant pour compatibilité
    public GameFrame(Game game) {
        this.game = game;
        this.currentUser = game.getUser();
        this.gamePanel = new GamePanel(game);
        this.resourceManager = ResourceManager.getInstance();
        
        setTitle("Tetris - Jouer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setupUI();
        setupGameTimer();
        setupAnimationTimer();
        setupKeyBindings();
        
        pack();
        setLocationRelativeTo(null);
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        
        // Panneau de contrôle
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBackground(new Color(240, 240, 245));
        
        JButton newGameBtn = createStyledButton("Nouvelle Partie", new Color(52, 152, 219));
        pauseBtn = createStyledButton("Pause", new Color(46, 204, 113));
        JButton quitBtn = createStyledButton("Quitter", new Color(231, 76, 60));
        
        newGameBtn.addActionListener(e -> startNewGame());
        pauseBtn.addActionListener(e -> togglePause());
        quitBtn.addActionListener(e -> quitGame());
        
        controlPanel.add(newGameBtn);
        controlPanel.add(pauseBtn);
        controlPanel.add(quitBtn);
        
        add(controlPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 35));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private void setupGameTimer() {
        gameTimer = new Timer(500, e -> {
            if (!game.isGameOver() && !isPaused) {
                game.update();
                gamePanel.repaint();
                // Ajuste la vitesse en fonction du niveau
                int newDelay = Math.max(100, 500 - (game.getLevel() - 1) * 60);
                gameTimer.setDelay(newDelay);
            } else if (game.isGameOver()) {
                gameTimer.stop();
                animationTimer.stop();
                resourceManager.playSound("game_over");
                showGameOverDialog();
            }
        });
        gameTimer.start();
    }
    
    private void setupAnimationTimer() {
        animationTimer = new Timer(16, e -> { // 60 FPS pour les animations
            gamePanel.updateAnimations();
            gamePanel.repaint();
        });
        animationTimer.start();
    }

    private void setupKeyBindings() {
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (game.isGameOver()) return;

                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_LEFT:
                        if (!isPaused) {
                            game.moveLeft();
                            resourceManager.playSound("move");
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_RIGHT:
                        if (!isPaused) {
                            game.moveRight();
                            resourceManager.playSound("move");
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_DOWN:
                        if (!isPaused) {
                            game.moveDown();
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_UP:
                        if (!isPaused) {
                            game.rotateCurrent();
                            resourceManager.playSound("rotate");
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_SPACE:
                        togglePause();
                        break;
                    case java.awt.event.KeyEvent.VK_N:
                        if (e.isControlDown()) {
                            startNewGame();
                        }
                        break;
                }
                gamePanel.repaint();
            }
        });
        setFocusable(true);
    }

    private void startNewGame() {
        // Demander confirmation si une partie est en cours (pas game over)
        if (!game.isGameOver()) {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment commencer une nouvelle partie ?\nLa partie actuelle sera perdue.",
                "Nouvelle Partie",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        // Arrêter les timers actuels
        gameTimer.stop();
        animationTimer.stop();
        
        // Créer une nouvelle partie
        this.game = new Game(currentUser);
        
        // Remplacer le GamePanel
        remove(gamePanel);
        this.gamePanel = new GamePanel(game);
        add(gamePanel, BorderLayout.CENTER);
        
        // Réinitialiser l'état de pause
        isPaused = false;
        pauseBtn.setText("Pause");
        pauseBtn.setBackground(new Color(46, 204, 113));
        
        // Masquer l'overlay de pause s'il était affiché
        hidePauseOverlay();
        
        // Redémarrer les timers
        setupGameTimer();
        setupAnimationTimer();
        
        // Remettre le focus sur la fenêtre
        requestFocus();
        
        // Afficher un message de confirmation
        JOptionPane.showMessageDialog(
            this,
            "Nouvelle partie commencée !\n\nContrôles :\n← → : Déplacer\n↑ : Rotation\n↓ : Descente rapide\nEspace : Pause\nCtrl+N : Nouvelle partie",
            "Nouvelle Partie",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void togglePause() {
        isPaused = !isPaused;
        
        if (isPaused) {
            gameTimer.stop();
            animationTimer.stop();
            pauseBtn.setText("Reprendre");
            pauseBtn.setBackground(new Color(155, 89, 182));
            
            // Afficher un overlay de pause
            showPauseOverlay();
        } else {
            gameTimer.start();
            animationTimer.start();
            pauseBtn.setText("Pause");
            pauseBtn.setBackground(new Color(46, 204, 113));
            
            // Masquer l'overlay de pause
            hidePauseOverlay();
        }
        
        // Remettre le focus sur la fenêtre
        requestFocus();
    }
    
    private void showPauseOverlay() {
        // Créer un overlay semi-transparent
        JPanel pauseOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fond semi-transparent
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Texte de pause
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 48));
                FontMetrics fm = g2d.getFontMetrics();
                String pauseText = "PAUSE";
                int textWidth = fm.stringWidth(pauseText);
                g2d.drawString(pauseText, (getWidth() - textWidth) / 2, getHeight() / 2);
                
                // Instructions
                g2d.setFont(new Font("Arial", Font.PLAIN, 16));
                String instructionText = "Appuyez sur Espace pour reprendre";
                textWidth = fm.stringWidth(instructionText);
                g2d.drawString(instructionText, (getWidth() - textWidth) / 2, getHeight() / 2 + 40);
            }
        };
        pauseOverlay.setOpaque(false);
        
        // Ajouter l'overlay au GamePanel
        gamePanel.setLayout(new BorderLayout());
        gamePanel.add(pauseOverlay, BorderLayout.CENTER);
        gamePanel.revalidate();
        gamePanel.repaint();
    }
    
    private void hidePauseOverlay() {
        // Retirer l'overlay de pause
        gamePanel.removeAll();
        gamePanel.revalidate();
        gamePanel.repaint();
    }
    
    private void showGameOverDialog() {
        // Enregistrement du score
        try {
            ScoreDAO scoreDAO = new ScoreDAO();
            Score newScore = new Score(
                0,
                currentUser.getId(),
                game.getScore(),
                game.getLevel(),
                game.getTotalLinesCleared(),
                java.time.LocalDateTime.now()
            );
            scoreDAO.saveScore(newScore);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erreur lors de l'enregistrement du score : " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }

        // Affichage du message Game Over
        String message = String.format(
            "Game Over!\n\nScore final : %d\nNiveau atteint : %d\nLignes complétées : %d",
            game.getScore(),
            game.getLevel(),
            game.getTotalLinesCleared()
        );

        Object[] options = {"Nouvelle Partie", "Retour au Menu", "Quitter"};
        int choice = JOptionPane.showOptionDialog(
            this,
            message,
            "Game Over",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );

        switch (choice) {
            case 0: // Nouvelle Partie
                startNewGame();
                break;
            case 1: // Retour au Menu
                quitGame();
                break;
            case 2: // Quitter
                System.exit(0);
                break;
        }
    }

    
    private void quitGame() {
        // Demander confirmation si une partie est en cours
        if (!game.isGameOver() && !isPaused) {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment quitter ?\nLa partie actuelle sera perdue.",
                "Quitter",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        // Arrêter tous les sons
        resourceManager.stopAllSounds();
        
        // Retourner a la page de connexion
        PostLoginFrame PostloginFrame = new PostLoginFrame(currentUser);
        PostloginFrame.setVisible(true);
        this.dispose();
    }

    public void repaint() {
        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }
} 