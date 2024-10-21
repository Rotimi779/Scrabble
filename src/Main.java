import java.util.ArrayList;
import java.util.Scanner;

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
        for (int i = 0; i < 7; i ++){
            player1.addTile(tilebag);
            player2.addTile(tilebag);
        }
        Board board = new Board();
        boolean play = true;
        int turn = 2;
        System.out.println("Welcome to the Game of Scrabble");

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

            //
            Scanner tilePick = new Scanner(System.in);
            System.out.print("Do you want to pick a tile? (yes/no): ");
            String userInput = tilePick.nextLine().trim().toLowerCase();
            while(true){
                if (userInput.equals("yes")) {
                    System.out.println("Pick a tile");
                    boolean validPick1 = false;
                    while(!(validPick1)){
                        currentPlayer.addTile(tilebag);
                        currentPlayer.displayTiles();
                        validPick1 = true;
                    }
                } else if (userInput.equals("no")) {
                    continue;
                } else {
                    System.out.println("Invalid input. Please respond with 'yes' or 'no'.");
                }
                tilePick.close();
            }

            play = false;

        }
    }



        

}