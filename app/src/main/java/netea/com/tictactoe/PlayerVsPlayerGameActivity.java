package netea.com.tictactoe;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;

public class PlayerVsPlayerGameActivity extends AppCompatActivity {

    //a few constants
    private final char player1Symbol = 'X';
    private final char player2Symbol = 'O';

    private final int gridX = 3;
    private final int gridY = 3;
    private final int winningNeeded = 3;


    //declare a custom data type to indicate a player
    private enum Player {Player1, Player2};
    Player nowPlaying;

    TableLayout ticTacToeGrid;
    TextView item00,item01,item02,item10, item11, item12, item20, item21, item22;
    TextView currentPlayingText;

    //a 2D array of TextViews. It allows us to easily traverse through the TextViews by using indexes.
    //Thus, we avoid code repetition
    TextView[][] textViewItems;


    //a 2D array of char. It contains only spaces at the beginning in order to indicate that the bord is empty.
    char[][] currentBoard = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_vs_player_game);

        //we randomly pick the player who is going to play first
        nowPlaying = (new Random().nextBoolean() ? Player.Player1 : Player.Player2);

        initializeUI();

        int counter = 0;
        for(int i=0; i<textViewItems.length; i++){
            for(TextView item:textViewItems[i]){
                //!!! add the number (0-8) of the grid item
                item.setTag(counter++);

                //register item clicks
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //only perform actions if the item has not already been selected by a player
                        if(((TextView)v).getText().toString().equals("")) {
                            //get the character to put now and move to the next player
                            char symbol = getSymbolToPut();
                            ((TextView) v).setText(String.valueOf(symbol));

                            //update the indication of whose turn it is
                            currentPlayingText.setText(getTextCurrentPlaying());

                            /*
                                get the row and column of the clicked element. We do
                                so by providing the number (0-8) - marked with !!!
                                above. We have the following mapping:
                                0 - [0][0],  1 - [0][1],  2 - [0][2]
                                3 - [1][0],  4 - [1][1],  5 - [1][2]
                                6 - [2][0],  7 - [2][1],  8 - [2][2]
                            */
                            int clicked[] = numberTo2DIndex((int)v.getTag());
                            currentBoard[clicked[0]][clicked[1]] = symbol;
                            //check if anybody is winning
                            if(isWinning(symbol)){
                                //if we have a winner, congratulate her/him on
                                showWinDialog(symbol);
                            }
                        }

                        Log.d("Tag", "tag " + v.getTag());
                    }
                });
            }
        }

    }

    private void showWinDialog(char symbol) {
        String text = (symbol == player1Symbol ? "Player 1 wins" : "Player 2 wins");
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                restartGame();
            }
        }.start();
    }

    private void initializeUI() {
        ticTacToeGrid = findViewById(R.id.tic_tac_toe_grid_view);
        currentPlayingText = findViewById(R.id.current_playing_txt);
        currentPlayingText.setText(getTextCurrentPlaying());
        item00 = findViewById(R.id.item_0_0);
        item01 = findViewById(R.id.item_0_1);
        item02 = findViewById(R.id.item_0_2);
        item10 = findViewById(R.id.item_1_0);
        item11 = findViewById(R.id.item_1_1);
        item12 = findViewById(R.id.item_1_2);
        item20 = findViewById(R.id.item_2_0);
        item21 = findViewById(R.id.item_2_1);
        item22 = findViewById(R.id.item_2_2);
        textViewItems = new TextView[][]{
                {item00, item01, item02},
                {item10, item11, item12},
                {item20, item21, item22}
        };
    }

    private char getSymbolToPut(){
        if(nowPlaying==Player.Player1){
            nowPlaying = Player.Player2;
            return player1Symbol;
        }else{
            nowPlaying = Player.Player1;
            return player2Symbol;
        }
    }

    private String getTextCurrentPlaying(){
        if(nowPlaying==Player.Player1){
            return getString(R.string.player_1_playing) + " " + player1Symbol;
        }
        return getString(R.string.player_2_playing)+ " " + player2Symbol;

    }

    private boolean isWinning(char c){
        return checkRows(c) || checkColumns(c) || checkPrimaryDiagonal(c) || checkSecondaryDiagonal(c);

    }

    int[] numberTo2DIndex(int number){
        return new int[]{number/gridX, number%gridY};
    }


    private boolean checkRows(char c){
        int elementsInRow = 0;

        //go through each row
        for(int i=0; i<gridX; i++){

            //go through each column
            for(int j=0; j<gridY; j++){

                //check if the element has been put by me. That is,
                //if I am player 1, I need an X and if I am player 2, I need a Y
                if(currentBoard[i][j]==c){
                    elementsInRow++;
                    if(elementsInRow==winningNeeded){
                        return true;
                    }
                }else{
                    break;
                }
            }
            elementsInRow=0;
        }

        return false;
    }


    private boolean checkColumns(char c){
        int elementsInRow = 0;

        //go through each column
        for(int i=0; i<gridY; i++){

            //go through each row
            for(int j=0; j<gridX; j++){

                //check if the element has been put by me. That is,
                //if I am player 1, I need an X and if I am player 2, I need a Y
                if(currentBoard[j][i]==c){
                    elementsInRow++;
                    if(elementsInRow==winningNeeded){
                        return true;
                    }
                }else{
                    break;
                }
            }
            elementsInRow=0;
        }

        return false;
    }

    private boolean checkPrimaryDiagonal(char c){
        int elementsInRow = 0;

        //checking the primary diagonal is simple - its indexes are [0, 0], [1, 1], [2, 2]
        //These are always two identical numbers
        for(int i=0; i<gridX; i++){

            //check if the element has been put by me. That is,
            //if I am player 1, I need an X and if I am player 2, I need a Y
                if(currentBoard[i][i]==c){
                    elementsInRow++;
                    if(elementsInRow==winningNeeded){
                        return true;
                    }
                }else{
                    break;
                }

        }

        return false;
    }

    private boolean checkSecondaryDiagonal(char c){
        int elementsInRow = 0;

        /*
            in oreder to check the secondary diagonal, we need to go
            from top to bottom and from right to left
            The indexes we are looking for are [0, 2], [1, 1], [2, 0]
            We notice that the sum of the two number is always 2.
            This means that the first number is i and the second number is
            gridX-i-1. We subtract 1 to take into account the fact
            that arrays in Java are 0-based.
        */
        for(int i=0; i<gridX; i++){

            //check if the element has been put by me. That is,
            //if I am player 1, I need an X and if I am player 2, I need a Y
            if(currentBoard[i][gridX-i-1]==c){
                elementsInRow++;
                if(elementsInRow==winningNeeded){
                    return true;
                }
            }else{
                break;
            }
        }

        return false;
    }

    private void restartGame(){
        currentBoard = new char[][]{
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };

        for(int i=0; i<textViewItems.length; i++){
            for(TextView item:textViewItems[i]){
                item.setText("");
            }
        }

    }
}
