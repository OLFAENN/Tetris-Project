package com.tetris.model;

import com.tetris.utils.Constants;
import com.tetris.utils.TetrominoFactory;

public class Game {
    private Board board;
    private Tetromino current;
    private int score;
    private int level;
    private int totalLinesCleared;
    private boolean gameOver;
    private User user;

    public Game() {
        board = new Board(Constants.ROWS, Constants.COLS);
        score = 0;
        level = 1;
        totalLinesCleared = 0;
        gameOver = false;
        spawnNewTetromino();
    }

    public Game(User user) {
        this();
        this.user = user;
    }

    private void spawnNewTetromino() {
        current = TetrominoFactory.createRandomTetromino();
        if (board.isCollision(current)) {
            gameOver = true;
        }
    }

    public void moveLeft() {
        current.setX(current.getX() - 1);
        if (board.isCollision(current)) {
            current.setX(current.getX() + 1); // Annuler le mouvement
        }
    }

    public void moveRight() {
        current.setX(current.getX() + 1);
        if (board.isCollision(current)) {
            current.setX(current.getX() - 1); // Annuler le mouvement
        }
    }

    public void moveDown() {
        current.setY(current.getY() + 1);
        if (board.isCollision(current)) {
            current.setY(current.getY() - 1); // Annuler le mouvement
            board.placeTetromino(current);
            
            int linesCleared = board.clearFullLines();
            if (linesCleared > 0) {
                score += linesCleared * Constants.SCORE_PER_LINE;
                totalLinesCleared += linesCleared;
                level = 1 + totalLinesCleared / 2; // Augmente le niveau toutes les 2 lignes
            }

            spawnNewTetromino();
        }
    }

    public void rotateCurrent() {
        int[][] originalShape = current.getShape();
        current.rotate();
        if (board.isCollision(current)) {
            current.setShape(originalShape); // Annuler la rotation
        }
    }

    /**
     * Méthode principale de la boucle de jeu. Fait descendre la pièce.
     */
    public void update() {
        if (!gameOver) {
            moveDown();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public int getTotalLinesCleared() {
        return totalLinesCleared;
    }

    public Board getBoard() {
        return board;
    }

    public Tetromino getCurrentTetromino() {
        return current;
    }

    public User getUser() {
        return user;
    }
}
