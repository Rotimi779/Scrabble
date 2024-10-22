public class Board {
    //attributes
    private char[][] board;
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

    public char[][] getBoard() {
        return board;
    }

    public void display(){
        for (int i = 0; i < 15; i++){
            for (int j = 0; j < 15; j++){
                System.out.printf(board[i][j] + " ");
            }
            System.out.println();
        }
    }








}
