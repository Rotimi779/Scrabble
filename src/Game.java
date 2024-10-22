import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Game {
    private TileBag tilebag;
    private final WordDictionary wordDictionary;
    private Board board;
    private int turn;
    public ArrayList<Player> players;
    private Player currentPlayer;
    private Scanner input;
    private int round = 1;

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
        input = new Scanner(System.in);
    }

    public void play(){
        boolean finished = false;
        while (!finished){
            playTurn();
        }
        input.close();
    }

    public void playTurn(){
        System.out.println("Round " + round);
        System.out.println("Player " + turn  +"'s turn");
        currentPlayer.displayTiles();
        System.out.print("Type a word or enter P to pass turn: ");
        String userInput = input.nextLine().trim().toLowerCase();
        while((!wordDictionary.containsWord(userInput)) ||  (!currentPlayer.canFormWordFromTiles(userInput))){
            if (userInput.equals("p")){
                break;
            }else if(!currentPlayer.canFormWordFromTiles(userInput)){
                System.out.println("The word cannot be formed from the available tiles");
                System.out.println("Type a new word or enter P to pass turn: ");
                userInput = input.nextLine().trim().toLowerCase();
            }
            else if(!wordDictionary.containsWord(userInput)){
                System.out.println("The word is not in the dictionary");
                System.out.println("Type a new word or enter P to pass turn: ");
                userInput = input.nextLine().trim().toLowerCase();
            }
        }
        if (!userInput.equals("p")){
            placeWord(userInput);
        }

        currentPlayer.updatePlayerScore(userInput);
        currentPlayer.displayTiles();
        board.display();

        if (!userInput.equals("p")){
            pickTile();
        }

        switchTurn();
        round ++;
    }

    public void switchTurn(){
        if (turn == 1){
            turn = 2;
            currentPlayer = players.get(1);
        } else if (turn == 2) {
            turn = 1;
            currentPlayer = players.getFirst();
        }
    }

    public void placeWord(String word) {
        boolean placed = false;
        while(!placed){
            System.out.println("Where do you want to place the word? ");
            System.out.println("Row: ");
            int row = input.nextInt();
            input.nextLine();
            System.out.println("Column: ");
            int column = input.nextInt();
            input.nextLine();
            System.out.println("H (Horizontal) or V (Vertical)? ");
            String direction = input.nextLine().trim().toLowerCase();

            if (direction.equals("horizontal") || direction.equals("h")){
                placed = board.place(word,'H',row,column);

            }else if(direction.equals("vertical") || direction.equals("v")){
                placed = board.place(word,'V',row,column);
            }else{
                System.out.println("Invalid direction");
            }
        }
    }

    public void pickTile() {
        //For picking a tile
        System.out.print("Do you want to pick a tile? (yes/no): ");
        String tileInput = input.nextLine().trim().toLowerCase();
        while(true){
            if (tileInput.equals("yes") || tileInput.equals("y") && currentPlayer.getNumberOfTiles() < 7) {
                System.out.println("Pick a tile");
                boolean validPick1 = false;
                while(!(validPick1)){
                    currentPlayer.addTile(tilebag);
                    validPick1 = true;
                }
                break;
            } else if (tileInput.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input. Please respond with 'yes' or 'no'.");
            }

        }
    }

    public int getRound(){
        return this.round;
    }
}
