import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


public class Player {
    private int score;
    private List<Tiles> tiles;

    public Player(){
        tiles = new ArrayList<>();
        this.score = 0;
    }

    public void addTile(TileBag tileBag){
        tiles.add(tileBag.drawTile());
    }

    public int getScore(char letter){
        for (Tiles tile : tiles) {
            if (tile.getLetter() == letter) {
                return tile.getScore();
            }
        }
        return 0;
    }

    public void removeTile(char letter, int score){
        Iterator<Tiles> iterator = tiles.iterator();
        while (iterator.hasNext()) {
            Tiles tile = iterator.next();
            if ((letter == tile.getLetter()) && (tile.getScore() == score)) {
                iterator.remove();
                System.out.println("Removed: " + tile);
            }
        }
        System.out.println("No matching tile found for " + letter + "(" + score + ")");
    }

    public void displayTiles() {
        System.out.println("Player's Tiles:");
        for (Tiles tile : tiles) {
            System.out.println("Tile: " + tile.getLetter() + " Score: " + tile.getScore());
        }
    }

    public void updatePlayerScore(String word) {
        Iterator<Tiles> iterator = tiles.iterator();
        for (char i : word.toCharArray()) {
            while (iterator.hasNext()) {
                Tiles tile = iterator.next();
                if (Character.toLowerCase(tile.getLetter()) == i) {
                    score += tile.getScore();
                    iterator.remove();  // Use iterator to remove the tile
                    System.out.println("New Score: " + this.score);
                    break;
                }
            }
        }
    }

    public int getNumberOfTiles() {
        return tiles.size();
    }

    public int getPlayerScore(){
        return this.score;
    }

    public  boolean canFormWordFromTiles(String word) {
        // Step 1: Count the available tiles
        Map<Character, Integer> tileCount = new HashMap<>();
        for (Tiles tile : tiles) {
            char character = Character.toLowerCase(tile.getLetter());
            tileCount.put(character, tileCount.getOrDefault(character, 0) + 1);
        }



        // Step 2: Check each letter in the word
        for (char letter : word.toCharArray()) {
            if (tileCount.containsKey(letter) && tileCount.get(letter) > 0) {
                // Decrease count for the letter
                tileCount.put(letter, tileCount.get(letter) - 1);
            } else {
                // Letter not available or used up
                return false;
            }
        }

        // Step 3: The word can be formed
        return true;
    }
}
