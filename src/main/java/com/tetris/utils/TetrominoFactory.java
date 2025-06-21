package com.tetris.utils;

import com.tetris.model.Tetromino;
import com.tetris.model.TetrominoType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class TetrominoFactory {
    private static final Random random = new Random();
    private static final Map<TetrominoType, int[][]> SHAPES = new EnumMap<>(TetrominoType.class);

    static {
        SHAPES.put(TetrominoType.I, new int[][]{{1, 1, 1, 1}});
        SHAPES.put(TetrominoType.O, new int[][]{{1, 1}, {1, 1}});
        SHAPES.put(TetrominoType.T, new int[][]{{0, 1, 0}, {1, 1, 1}});
        SHAPES.put(TetrominoType.S, new int[][]{{0, 1, 1}, {1, 1, 0}});
        SHAPES.put(TetrominoType.Z, new int[][]{{1, 1, 0}, {0, 1, 1}});
        SHAPES.put(TetrominoType.J, new int[][]{{1, 0, 0}, {1, 1, 1}});
        SHAPES.put(TetrominoType.L, new int[][]{{0, 0, 1}, {1, 1, 1}});
    }

    /**
     * Crée un tétromino aléatoire en haut et au centre de la grille.
     * @return Un nouveau Tetromino.
     */
    public static Tetromino createRandomTetromino() {
        TetrominoType randomType = TetrominoType.values()[random.nextInt(TetrominoType.values().length)];
        int[][] shape = SHAPES.get(randomType);
        
        // Position initiale : en haut, au centre
        int x = (Constants.COLS / 2) - (shape[0].length / 2);
        int y = 0;

        return new Tetromino(randomType, shape, x, y);
    }
}
