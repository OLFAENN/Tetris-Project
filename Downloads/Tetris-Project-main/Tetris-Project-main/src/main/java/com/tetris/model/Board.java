package com.tetris.model;

public class Board {
    // Grille de jeu (matrice)
    private final int[][] grid;
    private final int rows;
    private final int cols;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new int[rows][cols]; // 0 = vide
    }

    // Vérifie si une ligne est pleine
    public boolean isLineFull(int row) {
        for (int col = 0; col < cols; col++) {
            if (grid[row][col] == 0) {
                return false;
            }
        }
        return true;
    }

    // Supprime une ligne
    public void clearLine(int row) {
        // Décale toutes les lignes au-dessus de la ligne supprimée vers le bas
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = grid[i - 1][j];
            }
        }
        // La première ligne devient vide
        for (int j = 0; j < cols; j++) {
            grid[0][j] = 0;
        }
    }

    /**
     * Vérifie si un tétromino entre en collision avec les bords ou une autre pièce.
     * @param tetromino La pièce à vérifier.
     * @return true en cas de collision, sinon false.
     */
    public boolean isCollision(Tetromino tetromino) {
        int[][] shape = tetromino.getShape();
        int x = tetromino.getX();
        int y = tetromino.getY();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int boardX = x + j;
                    int boardY = y + i;

                    // Collision avec les bords
                    if (boardX < 0 || boardX >= cols || boardY < 0 || boardY >= rows) {
                        return true;
                    }

                    // Collision avec une autre pièce (si la case n'est pas vide)
                    if (grid[boardY][boardX] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Place un tétromino sur la grille (après que sa position finale soit validée).
     * @param tetromino La pièce à placer.
     */
    public void placeTetromino(Tetromino tetromino) {
        int[][] shape = tetromino.getShape();
        int x = tetromino.getX();
        int y = tetromino.getY();
        int typeId = tetromino.getType().ordinal() + 1; // +1 pour ne pas utiliser 0

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    grid[y + i][x + j] = typeId;
                }
            }
        }
    }

    /**
     * Supprime toutes les lignes complètes et retourne le nombre de lignes supprimées.
     * @return Le nombre de lignes supprimées.
     */
    public int clearFullLines() {
        int linesCleared = 0;
        for (int i = 0; i < rows; i++) {
            if (isLineFull(i)) {
                clearLine(i);
                linesCleared++;
                // Après avoir supprimé une ligne, il faut revérifier la même ligne
                // car les lignes du dessus sont descendues.
                i--; 
            }
        }
        return linesCleared;
    }

    // Accesseur pour la grille
    public int[][] getGrid() {
        return grid;
    }
}
