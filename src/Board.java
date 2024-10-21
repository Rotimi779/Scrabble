public class Board {
    //attributes
    char[][] board;
    String word;
    char direction;
    int row;
    int column;



    //constructor
    public Board(){
        board = new char[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                board[i][j] = '-';
            }
        }
    }



    //methods
    public void place(String word, char direction, int row, int column){
        for (int i=0; i< word.length(); i++){
            if (direction == 'H'){
                board[row][column + i] = word.charAt(i);
            } else if (direction == 'V') {
                board[row + i][column] = word.charAt(i);
            }
        }
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
        return true;
    }



}
