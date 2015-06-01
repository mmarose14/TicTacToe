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

        score += evaluateLine(0, 0, 0, 1, 0, 2);
        score += evaluateLine(1, 0, 1, 1, 1, 2);
        score += evaluateLine(2, 0, 2, 1, 2, 2);
        score += evaluateLine(0, 0, 1, 0, 2, 0);
        score += evaluateLine(0, 1, 1, 1, 2, 1);
        score += evaluateLine(0, 2, 1, 2, 2, 2);
        score += evaluateLine(0, 0, 1, 1, 2, 2);
        score += evaluateLine(0, 2, 1, 1, 2, 0);
        return score;
    }

    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;

        if (board[row1][col1] == X_MARK) {
            score = 1;
        } else if (board[row1][col1] == O_MARK) {
            score = -1;
        }

        if (board[row2][col2] == X_MARK) {
            if (score == 1) {
                score = 10;
            } else if (score == -1) {
                return 0;
            } else {
                score = 1;
            }
        } else if (board[row2][col2] == O_MARK) {
            if (score == -1) {
                score = -10;
            } else if (score == 1) {
                return 0;
            } else {
                score = -1;
            }
        }

        if (board[row3][col3] == X_MARK) {
            if (score > 0) {
                score *= 10;
            } else if (score < 0) {
                return 0;
            } else {
                score = 1;
            }
        } else if (board[row3][col3] == O_MARK) {
            if (score < 0) {
                score *= 10;
            } else if (score > 1) {
                return 0;
            } else {
                score = -1;
            }
        }
        return score;
    }

    public static boolean hasWon(char[][] board, char player) {
        for (int x = 0; x < 3; x++) {
            int total = 0;
            for (int y = 0; y < 3; y++) {
                if (board[x][y] == player) {
                    total++;
                }
            }
            if (total >= 3) {
                return true;
            }
        }

        for (int y = 0; y < 3; y++) {
            int total = 0;
            for (int x = 0; x < 3; x++) {
                if (board[x][y] == player) {
                    total++;
                }
            }
            if (total >= 3) {
                return true;
            }
        }

        int total = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (x == y && board[x][y] == player) {
                    total++;
                }
            }
        }
        if (total >= 3) {
            return true;
        }

        total = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (x + y == 2 && board[x][y] == player) {
                    total++;
                }
            }
        }
        if (total >= 3) {
            return true;
        }

        return false;
    }
}
