package com.tetris.model;

public class Tetromino {
    private TetrominoType type;
    private int[][] shape;
    private int x;
    private int y;

    public Tetromino(TetrominoType type, int[][] shape, int x, int y) {
        this.type = type;
        this.shape = shape;
        this.x = x;
        this.y = y;
    }

    /**
     * Fait pivoter la forme du tétromino de 90 degrés dans le sens horaire.
     */
    public void rotate() {
        if (type == TetrominoType.O) return; // Le carré n'a pas besoin de tourner

        int rows = shape.length;
        int cols = shape[0].length;
        int[][] newShape = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newShape[j][rows - 1 - i] = shape[i][j];
            }
        }
        this.shape = newShape;
    }

    // Getters et setters
    public int[][] getShape() { return shape; }
    public int getX() { return x; }
    public int getY() { return y; }
    public TetrominoType getType() { return type; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setShape(int[][] shape) { this.shape = shape; }
}
