public class Board {
    //attributes
    char[][] board;
    private Game game;




    //constructor
    public Board(Game game){
        this.game = game;
        board = new char[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                board[i][j] = '-';
            }
        }
    }



    //methods
    public boolean place(String word, char direction, int row, int column){
        for (int i=0; i< word.length(); i++){
            if (direction == 'H'){
                if (this.isValidPlacement(word, direction, row, column)){
                    board[row][column + i] = word.charAt(i);
                }else {
                    System.out.println("Invalid placement");
                    return false;
                }
            } else if (direction == 'V') {
                if (this.isValidPlacement(word, direction, row, column)){
                    board[row + i][column] = word.charAt(i);
                }else{
                    System.out.println("Invalid placement");
                    return false;
                }
            }
        }
        return true;
    }

    public void display(){
        for (int i = 0; i < 15; i++){
            for (int j = 0; j < 15; j++){
                System.out.printf(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean isValidPlacement(String word, char direction, int row, int column){

        // checking out of bounds
        if( direction == 'H' && (column + word.length()) > 15){
            return false;
        } else if ( direction == 'V' && (row + word.length()) > 15) {
            return false;
        }

        boolean hasAdjacent = false;
        int currentRound = game.getRound();

        for ( int i=0; i < word.length(); i++){

            char characterOnBoard;

            // setting the board character
            if (direction == 'H'){
                characterOnBoard = board[row][column + i];
            } else if (direction == 'V') {
                characterOnBoard = board[row + i][column];
            }else{
                return false;
            }

            // comparing the board character with word character
            if (characterOnBoard != '-'){
                if (word.charAt(i) != characterOnBoard){
                    return false;
                }
            }

            // checking for adjacent words
            if (currentRound > 1){
                if (direction == 'H'){
                    if (row > 0 && board[row - 1][column + i] != '-') {
                        hasAdjacent = true;
                    }
                    if (row < 14 && board[row + 1][column + i] != '-') {
                        hasAdjacent = true;
                    }
                } else if (direction == 'V') {
                    if (column > 0 && board[row + i][column - 1] != '-') {
                        hasAdjacent = true;
                    }
                    if (column < 14 && board[row + i][column + 1] != '-') {
                        hasAdjacent = true;
                    }
                }
            }

        }
        return currentRound <= 1 || hasAdjacent;
    }






}
