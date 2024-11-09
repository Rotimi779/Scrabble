import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Game {
    public static TileBag tilebag;
    public static WordDictionary wordDictionary;
    public static Board board;
    public static int turn;
    public ArrayList<Player> players;
    private Player currentPlayer;
    public static int round = 1;

    public Game() {
        // initializations
        tilebag = new TileBag();
        wordDictionary = new WordDictionary("https://www.mit.edu/~ecprice/wordlist.10000");
        this.players = new ArrayList<>();
        Player player1 = new Player();
        Player player2 = new Player();
        currentPlayer = player1;

        // Give the players their tiles
        for (int i = 0; i < 7; i ++){
            player1.addTile(tilebag);
            player2.addTile(tilebag);
        }

        players.add(player1);
        players.add(player2);

        // Initialize the board and start the game
        board = new Board(this);
        turn = 1;
        System.out.println("Welcome to the Game of Scrabble");
    }

    public void play(){
        boolean finished = false;

        while (!finished){ //
            currentPlayer.playTurn();
            switchTurn();
            if (tilebag.getBag().isEmpty()){
                for (Player player : players){
                    if (player.getTiles().isEmpty()){
                        finished = true;
                        break;
                    }
                }

            }
        }
        Player.close();
    }

    public void switchTurn(){
        round++;
        if (turn == 1){
            turn = 2;
            currentPlayer = players.get(1);
        } else if (turn == 2) {
            turn = 1;
            currentPlayer = players.get(0);
        }
    }

    public int getRound(){
        return this.round;
    }

    public static boolean isValidPlacement(String word, char direction, int row, int column){

        // checking out of bounds
        if( direction == 'H' && (column + word.length()) > 15){
            return false;
        } else if ( direction == 'V' && (row + word.length()) > 15) {
            return false;
        }

        boolean hasAdjacent = false;
        int currentRound = round;

        for ( int i=0; i < word.length(); i++){

            char characterOnBoard;

            // setting the board character
            if (direction == 'H'){
                characterOnBoard = board.getBoard()[row][column + i];
            } else if (direction == 'V') {
                characterOnBoard = board.getBoard()[row + i][column];
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
                    if (row > 0 && board.getBoard()[row - 1][column + i] != '-') {
                        hasAdjacent = true;
                    }
                    if (row < 14 && board.getBoard()[row + 1][column + i] != '-') {
                        hasAdjacent = true;
                    }
                } else if (direction == 'V') {
                    if (column > 0 && board.getBoard()[row + i][column - 1] != '-') {
                        hasAdjacent = true;
                    }
                    if (column < 14 && board.getBoard()[row + i][column + 1] != '-') {
                        hasAdjacent = true;
                    }
                }
            }

        }
        return currentRound <= 1 || hasAdjacent;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
