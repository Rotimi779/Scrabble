import java.util.ArrayList;

public class Main {
    public ArrayList<Player> players;
    int turns = 0;





    public static void main(String[] args) {
        TileBag tilebag = new TileBag();
        String url = "https://www.mit.edu/~ecprice/wordlist.10000";
        WordDictionary wordDictionary = new WordDictionary(url);
        Player player1 = new Player();
        Player player2 = new Player();
        Player currentPlayer = new Player();
        Board board = new Board();
        boolean play = true;
        int turn = 0;
        System.out.println("Welcome to the Game of Scrabble");

        while(play){
            System.out.println("Player 1's turn");
            currentPlayer =  player1;
            System.out.println("Pick a tile");
            boolean validPick = false;
            while(!(validPick)){
                currentPlayer.addTile(tilebag);
                currentPlayer.displayTiles();
                validPick = true;
            }
            board.display();
            play = false;
        }


        
    }
}