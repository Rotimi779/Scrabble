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
        board = new Board();
        turn = 1;
        System.out.println("Welcome to the Game of Scrabble");
    }

    public void play(){
        boolean finished = false;
        while (!finished){
            for (Player player : players){
                player.playTurn();
                switchTurn();
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
}
