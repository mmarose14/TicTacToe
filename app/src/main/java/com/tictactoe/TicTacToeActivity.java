package com.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class TicTacToeActivity extends AppCompatActivity {

    private static final String TAG = TicTacToeActivity.class.getName();
    public static final char O_MARK = 'O';
    public static final char X_MARK = 'X';
    public static final char EMPTY = '\0';

    private char board[][] = new char[3][3];
    private boolean gameInProgress;

    private TableLayout ticTacToeBoard;
    private TextView message;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tic_tac_toe);
        initializeView();
        newGame();
    }

    private void initializeView() {
        ticTacToeBoard = (TableLayout) findViewById(R.id.tic_tac_toe_board);
        message = (TextView) findViewById(R.id.message);
        resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
    }

    //New game, everything is reset
    private void newGame() {
        message.setText(R.string.welcome_message);
        resetButton.setVisibility(View.INVISIBLE);
        board = new char[3][3];
        gameInProgress = true;

        //Clear the board and set it up for a new game
        int rowIndex = -1;
        for (int x = 0; x < ticTacToeBoard.getChildCount(); x++) {
            if (ticTacToeBoard.getChildAt(x) instanceof TableRow) {
                TableRow row = (TableRow) ticTacToeBoard.getChildAt(x);
                rowIndex++;
                final int locX = rowIndex;

                for (int y = 0; y < 3; y++) {
                    final int locY = y;
                    RelativeLayout cell = (RelativeLayout) row.getChildAt(y);

                    cell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (gameInProgress && board[locX][locY] == EMPTY) {
                                updateGameBoard(locX, locY, true);
                            }
                        }
                    });

                    ImageView image = (ImageView) cell.getChildAt(0);
                    image.setImageDrawable(getResources().getDrawable(R.drawable.empty));
                }
            }


        }
    }

    //Move made, update the board
    private void updateGameBoard(int locX, int locY, boolean isPlayer) {

        //Clear messages
        message.setText("");

        Log.d(TAG, String.format("Updating: %d %d", locX, locY));

        //Update the game state
        board[locX][locY] = isPlayer ? O_MARK : X_MARK;

        //Update the board to display the new move
        int rowIndex = -1;
        for (int x = 0; x < ticTacToeBoard.getChildCount(); x++) {
            if (ticTacToeBoard.getChildAt(x) instanceof TableRow) {
                rowIndex++;
                TableRow row = (TableRow) ticTacToeBoard.getChildAt(x);
                for (int y = 0; y < 3; y++) {
                    if (rowIndex == locX && y == locY) {
                        RelativeLayout cell = (RelativeLayout) row.getChildAt(y);
                        ImageView image = (ImageView) cell.getChildAt(0);
                        image.setImageDrawable(getResources().getDrawable(
                                isPlayer ? R.drawable.o_image : R.drawable.x_image));
                        break;
                    }
                }
            }
        }


        //If no moves are available, end the game with a tie
        if (!areMovesAvailable()) {
            endGame(false);
            return;
        }

        //If the AI Player has won, end the game
        if (AIPlayer.hasWon(board, AIPlayer.X_MARK)) {
            endGame(true);
            return;
        }

        //AI Player's takes a turn after human player
        if (isPlayer) {
            //Determine AI Player's next move with AIPlayer
            AIPlayer aiPlayer = new AIPlayer(board);
            int[] aiMove = aiPlayer.executeAIMove();
            updateGameBoard(aiMove[0], aiMove[1], false);
        }
    }

    //Game is over, update the views
    private void endGame(boolean aiWon) {
        int displayMessageRedId = aiWon ? R.string.player_lost : R.string.tie_game;
        message.setText(getString(displayMessageRedId));
        gameInProgress = false;
        resetButton.setVisibility(View.VISIBLE);
    }

    //Check for empty boxes
    private boolean areMovesAvailable() {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                if (board[row][col] == EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }


}
