import java.util.ArrayList;

public class Main {
    public ArrayList<Player> players;
    int turns = 0;





    public static void main(String[] args) {
        TileBag tilebag = new TileBag();
        Player player1 = new Player();
        Player player2 = new Player();
        String url = "https://www.mit.edu/~ecprice/wordlist.10000";
        WordDictionary wordDictionary = new WordDictionary(url);
        System.out.println("Total words loaded: " + wordDictionary.getWordCount());

        System.out.println("Tiles remaining: " + tilebag.tilesRemaining());

    }
}