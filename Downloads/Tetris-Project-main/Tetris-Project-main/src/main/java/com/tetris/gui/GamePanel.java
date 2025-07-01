package com.tetris.gui;

import com.tetris.model.Game;
import com.tetris.model.Board;
import com.tetris.model.Tetromino;
import com.tetris.utils.Constants;
import com.tetris.utils.ResourceManager;
import com.tetris.utils.AnimationManager;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final Game game;
    private static final int CELL_SIZE = 25;
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color GRID_COLOR = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color GRID_LINE_COLOR = new Color(240, 240, 240);
    
    private ResourceManager resourceManager;
    private AnimationManager animationManager;
    private int lastScore = 0;
    private int lastLevel = 1;
    private int lastLines = 0;

    public GamePanel(Game game) {
        this.game = game;
        this.resourceManager = ResourceManager.getInstance();
        this.animationManager = AnimationManager.getInstance();
        setPreferredSize(new Dimension(Constants.COLS * CELL_SIZE + 250, Constants.ROWS * CELL_SIZE + 100));
        setBackground(BACKGROUND_COLOR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Dessiner le fond avec image
        drawBackground(g2d);

        // Calcule la taille des cellules dynamiquement
        int cellSize = (int) Math.min((getWidth() - 300) / (double) Constants.COLS, (getHeight() - 100) / (double) Constants.ROWS);
        int boardWidth = cellSize * Constants.COLS;
        int boardHeight = cellSize * Constants.ROWS;
        // Centre la grille
        int xOffset = 50;
        int yOffset = 50;

        Board board = game.getBoard();
        int[][] grid = board.getGrid();
        Tetromino current = game.getCurrentTetromino();
        int[][] currentShape = current.getShape();
        int x = current.getX();
        int y = current.getY();

        // Dessiner le fond de la grille
        g2d.setColor(GRID_COLOR);
        g2d.fillRoundRect(xOffset - 3, yOffset - 3, boardWidth + 6, boardHeight + 6, 8, 8);
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(xOffset - 3, yOffset - 3, boardWidth + 6, boardHeight + 6, 8, 8);

        // Dessiner les lignes de grille
        g2d.setColor(GRID_LINE_COLOR);
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i <= Constants.ROWS; i++) {
            g2d.drawLine(xOffset, yOffset + i * cellSize, xOffset + boardWidth, yOffset + i * cellSize);
        }
        for (int j = 0; j <= Constants.COLS; j++) {
            g2d.drawLine(xOffset + j * cellSize, yOffset, xOffset + j * cellSize, yOffset + boardHeight);
        }

        // Dessiner les cases déjà posées avec images
        for (int i = 0; i < Constants.ROWS; i++) {
            for (int j = 0; j < Constants.COLS; j++) {
                if (grid[i][j] != 0) {
                    drawCellWithImage(g2d, xOffset + j * cellSize, yOffset + i * cellSize, cellSize, grid[i][j] - 1);
                }
            }
        }

        // Dessiner le tétromino courant avec image
        for (int i = 0; i < currentShape.length; i++) {
            for (int j = 0; j < currentShape[i].length; j++) {
                if (currentShape[i][j] != 0) {
                    int drawX = xOffset + (x + j) * cellSize;
                    int drawY = yOffset + (y + i) * cellSize;
                    drawCellWithImage(g2d, drawX, drawY, cellSize, current.getType().ordinal());
                }
            }
        }

        // Dessiner le panneau d'information
        drawInfoPanel(g2d, xOffset + boardWidth + 30, yOffset);
        
        // Dessiner les animations
        animationManager.render(g2d);
        
        // Vérifier les changements pour déclencher les animations
        checkForAnimations();
    }
    
    private void drawBackground(Graphics2D g2d) {
        // Utiliser l'image de fond si disponible
        java.awt.image.BufferedImage bgImage = resourceManager.getImage("background");
        if (bgImage != null) {
            g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            // Fallback au fond uni
            g2d.setColor(BACKGROUND_COLOR);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    private void drawCellWithImage(Graphics2D g2d, int x, int y, int size, int type) {
        // Essayer d'utiliser une image pour le tétromino
        String imageName = "tetromino_" + getTetrominoTypeName(type);
        java.awt.image.BufferedImage image = resourceManager.getImage(imageName);
        
        if (image != null) {
            // Redimensionner l'image à la taille de la cellule
            g2d.drawImage(image, x, y, size, size, null);
        } else {
            // Fallback au dessin coloré
            drawCell(g2d, x, y, size, getColorForType(type));
        }
    }
    
    private String getTetrominoTypeName(int type) {
        switch (type) {
            case 0: return "I";
            case 1: return "O";
            case 2: return "T";
            case 3: return "S";
            case 4: return "Z";
            case 5: return "J";
            case 6: return "L";
            default: return "I";
        }
    }
    
    private void checkForAnimations() {
        // Vérifier si le score a changé
        if (game.getScore() > lastScore) {
            int scoreDiff = game.getScore() - lastScore;
            animationManager.createScoreAnimation(300, 200, scoreDiff);
            
            // Vérifier combien de lignes ont été complétées
            int linesDiff = game.getTotalLinesCleared() - lastLines;
            
            if (linesDiff >= 3) {
                // 3 lignes ou plus d'un coup = AMAZING !
                resourceManager.playSound("amazing");
                // Créer une animation spéciale pour "AMAZING"
                createAmazingAnimation();
            } else if (linesDiff > 0) {
                // Lignes normales
                resourceManager.playSound("line_clear");
            }
            
            lastScore = game.getScore();
        }
        
        // Vérifier si le niveau a changé
        if (game.getLevel() > lastLevel) {
            animationManager.createLevelUpAnimation();
            resourceManager.playSound("game_over"); // Son spécial pour le niveau
            lastLevel = game.getLevel();
        }
        
        // Vérifier si des lignes ont été complétées
        if (game.getTotalLinesCleared() > lastLines) {
            int linesDiff = game.getTotalLinesCleared() - lastLines;
            // Créer une animation pour chaque ligne complétée
            for (int i = 0; i < linesDiff; i++) {
                animationManager.createLineClearAnimation(Constants.ROWS - 1 - i, 0, Constants.COLS);
            }
            lastLines = game.getTotalLinesCleared();
        }
    }
    
    private void createAmazingAnimation() {
        // Créer une animation spéciale "AMAZING"
        AnimationManager.AmazingAnimation amazingAnim = new AnimationManager.AmazingAnimation();
        animationManager.addAnimation(amazingAnim);
    }

    private void drawCell(Graphics2D g2d, int x, int y, int size, Color color) {
        // Cellule principale
        g2d.setColor(color);
        g2d.fillRoundRect(x + 1, y + 1, size - 2, size - 2, 3, 3);
        
        // Bordure
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(x + 1, y + 1, size - 2, size - 2, 3, 3);
    }

    private void drawInfoPanel(Graphics2D g2d, int x, int y) {
        // Fond du panneau
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(x, y, 200, 350, 10, 10);
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, 200, 350, 10, 10);

        // Titre
        g2d.setColor(new Color(50, 50, 50));
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("INFORMATIONS", x + 20, y + 30);

        // Score
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.setColor(new Color(100, 100, 100));
        g2d.drawString("Score:", x + 20, y + 70);
        g2d.setColor(new Color(50, 50, 50));
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString(String.valueOf(game.getScore()), x + 20, y + 95);

        // Niveau
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.setColor(new Color(100, 100, 100));
        g2d.drawString("Niveau:", x + 20, y + 130);
        g2d.setColor(new Color(50, 50, 50));
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString(String.valueOf(game.getLevel()), x + 20, y + 155);

        // Lignes
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.setColor(new Color(100, 100, 100));
        g2d.drawString("Lignes:", x + 20, y + 190);
        g2d.setColor(new Color(50, 50, 50));
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString(String.valueOf(game.getTotalLinesCleared()), x + 20, y + 215);

        // Séparateur
        g2d.setColor(new Color(220, 220, 220));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(x + 20, y + 240, x + 180, y + 240);

        // Contrôles
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(new Color(100, 100, 100));
        g2d.drawString("Contrôles:", x + 20, y + 270);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.setColor(new Color(150, 150, 150));
        g2d.drawString("← → : Déplacer", x + 20, y + 290);
        g2d.drawString("↑ : Rotation", x + 20, y + 305);
        g2d.drawString("↓ : Descente rapide", x + 20, y + 320);
        g2d.drawString("Espace : Pause", x + 20, y + 335);
    }

    private Color getColorForType(int type) {
        switch (type) {
            case 0: return new Color(0, 240, 240);    // I - Cyan
            case 1: return new Color(240, 240, 0);    // O - Jaune
            case 2: return new Color(160, 0, 240);    // T - Magenta
            case 3: return new Color(0, 240, 0);      // S - Vert
            case 4: return new Color(240, 0, 0);      // Z - Rouge
            case 5: return new Color(0, 0, 240);      // J - Bleu
            case 6: return new Color(240, 160, 0);    // L - Orange
            default: return Color.WHITE;
        }
    }
    
    // Méthode pour mettre à jour les animations
    public void updateAnimations() {
        animationManager.update();
    }
} 