import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public ArrayList<Player> players;
    int turns = 0;





    public static void main(String[] args) {
        // initializations
        TileBag tilebag = new TileBag();
        String url = "https://www.mit.edu/~ecprice/wordlist.10000";
        WordDictionary wordDictionary = new WordDictionary(url);
        Player player1 = new Player();
        Player player2 = new Player();
        Player currentPlayer = new Player();

        // Give the players their tiles
        for (int i = 0; i < 7; i ++){
            player1.addTile(tilebag);
            player2.addTile(tilebag);
        }

        // Initialize the board and start the game
        Board board = new Board();
        boolean play = true;
        int turn = 2;
        System.out.println("Welcome to the Game of Scrabble");
        Scanner input = new Scanner(System.in);

        // game is in play
        while(play){
            if (turn == 1){
                turn = 2;
                currentPlayer = player2;
            }
            else if (turn == 2){
                turn = 1;
                currentPlayer = player1;
            }

            System.out.println("Player " + String.valueOf(turn)  +"'s turn");
            currentPlayer.displayTiles();
            System.out.print("Type a word ");
            String userInput = input.nextLine().trim().toLowerCase();
            while((!wordDictionary.containsWord(userInput)) ||  (!currentPlayer.canFormWordFromTiles(userInput))){
                if(!currentPlayer.canFormWordFromTiles(userInput)){
                    System.out.println("The word cannot be formed from the available tiles");
                    System.out.println("Type a new word ");
                    userInput = input.nextLine().trim().toLowerCase();
                }
                else if(!wordDictionary.containsWord(userInput)){
                    System.out.println("The word is not in the dictionary");
                    System.out.println("Type a new word ");
                    userInput = input.nextLine().trim().toLowerCase();
                }
            }
            board.place(userInput,'H',0,0);
            currentPlayer.updatePlayerScore(userInput);
            currentPlayer.displayTiles();
            board.display();


            //For picking a tile
            System.out.print("Do you want to pick a tile? (yes/no): ");
            String tileInput = input.nextLine().trim().toLowerCase();
            while(true){
                if (tileInput.equals("yes")) {
                    System.out.println("Pick a tile");
                    boolean validPick1 = false;
                    while(!(validPick1)){
                        currentPlayer.addTile(tilebag);
                        currentPlayer.displayTiles();
                        validPick1 = true;
                    }
                    break;
                } else if (tileInput.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please respond with 'yes' or 'no'.");
                }

            }

            //abplay = false;

        }
        input.close();
    }



        

}
