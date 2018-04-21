package radoslav.com.tictactoe;

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

    int symbolsPut = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the layout - open this file to see the UI elements
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
                            symbolsPut++;

                            if(new Board(currentBoard).isWinning(symbol)){
                                //if we have a winner, congratulate her/him on
                                showWinDialog(symbol);
                                symbolsPut = 0;
                            }
                            else {
                                //increment the indication of symbols put
                                //if the board is full and there is no winner,
                                //restart and say it is a draw
                                if (symbolsPut == Constants.gridX * Constants.gridY) {
                                    Toast.makeText(PlayerVsPlayerGameActivity.this, "Draw", Toast.LENGTH_LONG).show();
                                    restartGame();
                                    symbolsPut = 0;
                                }
                            }
                        }
                    }
                });
            }
        }

    }

    private void showWinDialog(char symbol) {
        String text = (symbol == player1Symbol ? getString(R.string.player_1_wins) : getString(R.string.player_2_wins));
        //show a message to indicate the winning player
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        //wait 2 seconds before restarting the game
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
        /*
            findViewById will give us a reference to an UI view so we can use it in our code
            R is automatically generated class by the Android SDK. It contains references
            to the resources
        */
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
        //reverse the players and get the symbol to put
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



    int[] numberTo2DIndex(int number){
        /*
            create an array of two numbers - the two indexes of the 2D array
            //Take the number 7 for example. Recall that our grid starts from 0:
            0 1 2
            3 4 5
            6 7 8
            Therefore, it is in row 2, element 1 (keep in mind we are 0-based).
            If we divide 7/3 as integers (that is, we do NOT take the reminder into account),
            we get 2 (the number of row). If we get the reminder itself - 7%3 - we get 1
            (the number of the column). Here is how we find the reminder:
                7/3 = 2,
                2*3 + r = 7
                r = 1
        */

        return new int[]{number/Constants.gridX, number%Constants.gridY};
    }




    private void restartGame(){
        //set all the data to its initial state
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
