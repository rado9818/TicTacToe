package netea.com.tictactoe;

public class Board {

    private char[][] currentBoard;

    Board(char [][] currentBoard){
        this.currentBoard = currentBoard;
    }

    public boolean isWinning(char c){
        /*
        In order for somebody to win, he or she should satisfy at least one of the following (considering a 3X3 grid):
            * 3 identical symbols in a single row
            * 3 identical symbols in a single column
            * 3 identical symbols in the left-to-right diagonal (primary diagonal)
            * 3 identical symbols in the right-to-left diagonal (secondary diagonal)
        */

        return checkRows(c) || checkColumns(c) || checkPrimaryDiagonal(c) || checkSecondaryDiagonal(c);

    }

    private boolean checkRows(char c){
        int elementsInRow = 0;

        //go through each row
        for(int i=0; i<Constants.gridX; i++){

            //go through each column
            for(int j=0; j<Constants.gridY; j++){

                //check if the element has been put by me. That is,
                //if I am player 1, I need an X and if I am player 2, I need a Y
                if(currentBoard[i][j]==c){
                    elementsInRow++;
                    if(elementsInRow==Constants.winningNeeded){
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
        for(int i=0; i<Constants.gridY; i++){

            //go through each row
            for(int j=0; j<Constants.gridX; j++){

                //check if the element has been put by me. That is,
                //if I am player 1, I need an X and if I am player 2, I need a Y
                if(currentBoard[j][i]==c){
                    elementsInRow++;
                    if(elementsInRow==Constants.winningNeeded){
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
        for(int i=0; i<Constants.gridX; i++){

            //check if the element has been put by me. That is,
            //if I am player 1, I need an X and if I am player 2, I need a Y
            if(currentBoard[i][i]==c){
                elementsInRow++;
                if(elementsInRow==Constants.winningNeeded){
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
            in order to check the secondary diagonal, we need to go
            from top to bottom and from right to left
            The indexes we are looking for are [0, 2], [1, 1], [2, 0]
            We notice that the sum of the two number is always 2.
            This means that the first number is i and the second number is
            gridX-i-1. We subtract 1 to take into account the fact
            that arrays in Java are 0-based.
        */
        for(int i=0; i<Constants.gridX; i++){

            //check if the element has been put by me. That is,
            //if I am player 1, I need an X and if I am player 2, I need a Y
            if(currentBoard[i][Constants.gridX-i-1]==c){
                elementsInRow++;
                if(elementsInRow==Constants.winningNeeded){
                    return true;
                }
            }else{
                break;
            }
        }

        return false;
    }

}
