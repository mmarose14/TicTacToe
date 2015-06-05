package com.tictactoe;

import java.util.ArrayList;
import java.util.List;


/* This class is used to determine which move the AI Player will make next based on a given board state.
The code uses a "Minimax" search algorithm with heuristic board evaluation.
 */
public class AIPlayer {

    public static final char EMPTY = '\0';
    public static final char O_MARK = 'O';
    public static final char X_MARK = 'X';
    public static final String TYPE_ROWS = "ROWS";
    public static final String TYPE_COLS = "COLS";
    public static final String TYPE_DIAG = "DIAG";
    public static final String TYPE_ALT_DIAG = "ALT_DIAG";

    public static final int[][][] LINE_CHECK = {
            {new int[] {0,0}, new int[]{0,1},new int[]{0,2}},
            {new int[] {1,0}, new int[]{1,1},new int[]{1,2}},
            {new int[] {2,0}, new int[]{2,1},new int[]{2,2}},
            {new int[] {0,0}, new int[]{1,0},new int[]{2,0}},
            {new int[] {0,1}, new int[]{1,1},new int[]{2,1}},
            {new int[] {0,2}, new int[]{1,2},new int[]{2,2}},
            {new int[] {0,0}, new int[]{1,1},new int[]{2,2}},
            {new int[] {0,2}, new int[]{1,1},new int[]{2,0}}
    };

    private char[][] board;

    public AIPlayer(char board[][]) {
        this.board = board;
    }

    public int[] executeAIMove() {
        int[] result = minmax(2, X_MARK);
        return new int[]{result[1], result[2]};
    }

    private int[] minmax(int depth, char player) {
        List<int[]> nextMoves = generateMoves();

        int bestScore = (player == X_MARK) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            bestScore = evaluate();
        } else {
            for (int[] move : nextMoves) {
                board[move[0]][move[1]] = player;
                if (player == X_MARK) {
                    currentScore = minmax(depth - 1, O_MARK)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {
                    currentScore = minmax(depth - 1, X_MARK)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }

                board[move[0]][move[1]] = EMPTY;
            }
        }

        return new int[] {bestScore, bestRow, bestCol};
    }

    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<>();

        if (hasWon(board, O_MARK) || hasWon(board, X_MARK)) {
            return nextMoves;
        }

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                if (board[row][col] == EMPTY) {
                    nextMoves.add(new int[]{row, col});
                }
            }
        }
        return nextMoves;
    }

    private int evaluate() {
        int score = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 3; y++) {
                score += evaluateLine(new int[][] {LINE_CHECK[x][0], LINE_CHECK[x][1], LINE_CHECK[x][2]});
            }
        }

        return score;
    }

    private int evaluateLine(int[][] coords) {
        int score = 0;

        for (int i = 0; i < 3; i++) {
            if (board[coords[i][0]][coords[i][1]] == X_MARK) {
                if (score > 0) {
                    score *= 10;
                } else if (score < 0) {
                    score = 0;
                } else {
                    score = 1;
                }
            } else if (board[coords[i][0]][coords[i][1]] ==O_MARK) {
                if (score < 0) {
                    score *= 10;
                } else if (score > 1) {
                    score = 0;
                } else {
                    score = -1;
                }
            }
        }
        return score;
    }

    public static boolean hasWon(char[][] board, char player) {

        if (checkRowsOrCols(board, player, TYPE_ROWS)
                || checkRowsOrCols(board, player, TYPE_COLS)
                || checkDiagonals(board, player, TYPE_DIAG)
                || checkDiagonals(board, player, TYPE_ALT_DIAG)) {
            return true;
        }

        return false;
    }

    private static boolean checkRowsOrCols(char[][] board, char player, String type) {
        char mark = EMPTY;
        for (int x = 0; x < 3; x++) {
            int total = 0;
            for (int y = 0; y < 3; y++) {
                if (TYPE_ROWS.equalsIgnoreCase(type)) {
                    mark = board[x][y];
                } else if (TYPE_COLS.equalsIgnoreCase(type)){
                    mark = board[y][x];
                }

                if (mark == player) {
                    total++;
                }
            }
            if (total >= 3) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkDiagonals(char[][] board, char player, String type) {
        char mark;
        int total = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                mark = board[x][y];
                if (mark == player) {
                    if ((TYPE_DIAG.equalsIgnoreCase(type) && x == y)
                            || (TYPE_ALT_DIAG.equalsIgnoreCase(type) && x + y == 2)) {
                        total++;
                    }
                }
            }
        }
        if (total >= 3) {
            return true;
        }
        return false;
    }
}
