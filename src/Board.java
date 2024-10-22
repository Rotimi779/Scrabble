public class Board {
    //attributes
    char[][] board;



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

        //checking for intersection (does not account for parallel placement yet)
        for ( int i=0; i < word.length(); i++){

            char characterOnBoard;

            if (direction == 'H'){
                characterOnBoard = board[row][column + i];
            } else if (direction == 'V') {
                characterOnBoard = board[row + i][column];
            }else{
                return false;
            }

            if (characterOnBoard != '-'){
                if (word.charAt(i) != characterOnBoard){
                    return false;
                }
            }
        }
        return true;
    }






}
